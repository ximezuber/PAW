package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.interfaces.service.LocationService;
import ar.edu.itba.paw.interfaces.service.PrepaidToClinicService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import ar.edu.itba.paw.webapp.caching.ClinicCaching;
import ar.edu.itba.paw.webapp.caching.PrepaidCaching;
import ar.edu.itba.paw.webapp.dto.ClinicDto;
import ar.edu.itba.paw.webapp.dto.PrepaidDto;
import ar.edu.itba.paw.webapp.form.ClinicForm;
import ar.edu.itba.paw.webapp.helpers.CacheHelper;
import ar.edu.itba.paw.webapp.helpers.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import java.util.stream.Collectors;

@Component
@Path("clinics")
public class ClinicController {

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private PrepaidToClinicService prepaidToClinicService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ClinicCaching clinicCaching;

    @Autowired
    private PrepaidCaching prepaidCaching;

    @Context
    private UriInfo uriInfo;

    /**
     * Returns paginated list of clinics for ADMIN user to manage.
     * @param page
     * @return list of Clinics
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getClinics(@QueryParam("page") @DefaultValue("0") Integer page,
                               @Context Request request) {
        page = (page < 0) ? 0 : page;

        List<ClinicDto> clinics = clinicService.getPaginatedObjects(page).stream()
                .map(c -> ClinicDto.fromClinic(c, uriInfo)).collect(Collectors.toList());
        int maxPage = clinicService.maxAvailablePage();

        URI basePath = uriInfo.getAbsolutePathBuilder().build();
        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxPage);
        Response.ResponseBuilder response = CacheHelper.handleResponse(clinics, clinicCaching,
                new GenericEntity<List<ClinicDto>>(clinics) {}, "clinics", request);
        if (!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }

        return response.build();

    }

    /**
     * Returns full list of clinics for DOCTOR to choose from
     * @return list of Clinics
     */
    @GET
    @Path("/all")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getAllClinics(@Context Request request) {
        List<ClinicDto> clinics = clinicService.getClinics().stream()
                .map(c -> ClinicDto.fromClinic(c, uriInfo)).collect(Collectors.toList());
        return CacheHelper.handleResponse(clinics, clinicCaching,
                        new GenericEntity<List<ClinicDto>>(clinics) {}, "clinics", request).build();
    }

    /**
     * Lets ADMIN add new clinics to the application (Requires clinicForm)
     * @param clinicForm
     * @return
     * @throws EntityNotFoundException
     * @throws DuplicateEntityException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response createClinic(final ClinicForm clinicForm) throws EntityNotFoundException, DuplicateEntityException {
        Location location = locationService.getLocationByName(clinicForm.getLocation())
                .orElseThrow(() -> new EntityNotFoundException("location"));
        Clinic clinic = clinicService.createClinic(clinicForm.getName(), clinicForm.getAddress(), location);

        return Response.created(uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(clinic.getId())).build()).build();
    }

    /**
     * Lets ADMIN edit clinic
     * @param clinicId
     * @param clinicForm
     * @return
     * @throws EntityNotFoundException
     */
    @PUT
    @Path("{clinicId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response updateClinic(@PathParam("clinicId") final Integer clinicId, final ClinicForm clinicForm)
            throws EntityNotFoundException {

        Clinic clinic = clinicService.getClinicById(clinicId).orElseThrow(() -> new EntityNotFoundException("clinic"));
        Location location = clinicForm.getLocation() == null || clinicForm.getLocation().equals("")  ?
                null : locationService.getLocationByName(clinicForm.getLocation())
                .orElseThrow(() -> new EntityNotFoundException("location"));

        clinicService.updateClinic(clinic, clinicForm.getName(), clinicForm.getAddress(), location);
        return Response.noContent().build();
    }

    /**
     * Returns specific clinic information
     * @param clinicId
     * @return Clinic
     */
    @GET
    @Path("{clinicId}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getClinic(@PathParam("clinicId") final Integer clinicId,
                              @Context Request request) throws EntityNotFoundException {
        Clinic clinic = clinicService.getClinicById(clinicId).orElseThrow(() -> new EntityNotFoundException("clinic"));

        ClinicDto dto = ClinicDto.fromClinic(clinic, uriInfo);
        return CacheHelper.handleResponse(dto, clinicCaching, "clinic", request).build();
    }

    /**
     * Lets ADMIN delete a clinic
     * @param clinicId
     * @return
     * @throws EntityNotFoundException
     */
    @DELETE
    @Path("{clinicId}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response deleteClinic(@PathParam("clinicId") final Integer clinicId) throws EntityNotFoundException {
        Clinic clinic = clinicService.getClinicById(clinicId).orElseThrow(() -> new EntityNotFoundException("clinic"));
        clinicService.deleteClinic(clinic);
        return Response.noContent().build();
    }

    /**
     * Returns paginated list of a specific clinic's prepaid
     * "X-max-page" header: last page of prepaid
     * @param clinicId
     * @param page
     * @return list of Prepaid
     * @throws EntityNotFoundException
     */
    @GET
    @Path("{clinicId}/prepaid")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getClinicPrepaid(@PathParam("clinicId") final Integer clinicId,
                                      @QueryParam("page") @DefaultValue("0") Integer page,
                                      @Context Request request) throws EntityNotFoundException {
        page = (page < 0) ? 0 : page;

       clinicService.getClinicById(clinicId).orElseThrow(() -> new EntityNotFoundException("clinic"));

        List<PrepaidDto> prepaid = prepaidToClinicService.getPrepaidForClinic(clinicId, page)
                .stream().map(PrepaidDto::fromPrepaid).collect(Collectors.toList());
        int maxPage = prepaidToClinicService.maxAvailablePagePerClinic(clinicId);
        URI basePath = uriInfo.getAbsolutePathBuilder().build();
        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxPage);
        Response.ResponseBuilder response = CacheHelper.handleResponse(prepaid, prepaidCaching,
                new GenericEntity<List<PrepaidDto>>(prepaid) {},
                "prepaid", request);

        if (!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }

        return response.build();

    }

    /**
     * Returns list of all prepaid of a specific clinic
     * @param clinicId
     * @return list of Prepaid
     * @throws EntityNotFoundException
     */
    @GET
    @Path("{clinicId}/prepaid/all")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getAllClinicPrepaid(@PathParam("clinicId") final Integer clinicId,
                                      @Context Request request) throws EntityNotFoundException {

        clinicService.getClinicById(clinicId).orElseThrow(() -> new EntityNotFoundException("clinic"));

        List<PrepaidDto> prepaid = prepaidToClinicService.getPrepaidForClinic(clinicId)
                .stream().map(PrepaidDto::fromPrepaid).collect(Collectors.toList());
        return CacheHelper.handleResponse(prepaid, prepaidCaching, new GenericEntity<List<PrepaidDto>>(prepaid) {},
                "prepaid", request).build();

    }

    /**
     * Lets ADMIN add a prepaid to a specific clinic
     * @param clinicId
     * @param prepaidId
     * @return
     * @throws EntityNotFoundException
     */
    @PUT
    @Path("{clinicId}/prepaid/{prepaidId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response addPrepaidToClinic(@PathParam("clinicId") final Integer clinicId,
                                       @PathParam("prepaidId") final String prepaidId)
            throws EntityNotFoundException {

        prepaidToClinicService.addPrepaidToClinic(prepaidId, clinicId);
        return Response.noContent().build();

    }

    /**
     * Lets ADMIN remove a prepaid from a specific clinic
     * @param clinicId
     * @param prepaid
     * @return
     * @throws EntityNotFoundException
     */
    @DELETE
    @Path("{clinicId}/prepaid/{prepaidId}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response removePrepaidFromClinic(@PathParam("clinicId") final Integer clinicId,
                                            @PathParam("prepaidId") final String prepaid)
            throws EntityNotFoundException {
        prepaidToClinicService.deletePrepaidFromClinic(prepaid, clinicId);
        return Response.ok().build();

    }

}
