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
        int maxPage = clinicService.maxAvailablePage() - 1;

        String basePath = "/api/clinics?";
        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxPage, false);
        Response.ResponseBuilder response = CacheHelper.handleResponse(clinics, clinicCaching,
                new GenericEntity<List<ClinicDto>>(clinics) {}, "clinics", request);
        if (!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }

        return response.build();

    }

    /**
     *Returns full list of clinics for DOCTOR to choose from
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
        Location location = locationService.getLocationByName(clinicForm.getLocation());
        if (location == null) throw new EntityNotFoundException("location");
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
        Clinic clinic = clinicService.getClinicById(clinicId);
        if(clinic == null) throw new EntityNotFoundException("clinics");
        clinicService.updateClinic(clinicId, clinicForm.getName(), clinicForm.getAddress(), clinicForm.getLocation());
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
                              @Context Request request) {
        Clinic clinic = clinicService.getClinicById(clinicId);
        if(clinic != null) {
            ClinicDto dto = ClinicDto.fromClinic(clinic, uriInfo);
            Response.ResponseBuilder r = CacheHelper.handleResponse(dto, clinicCaching, "clinic", request);
            return r.build();
        }
        return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
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
        clinicService.deleteClinic(clinicId);
        return Response.noContent().build();
    }

    /**
     * Returns paginated list of a specific clinic's prepaids
     * "X-max-page" header: last page of prepaids
     * @param clinicId
     * @param page
     * @return list of Prepaids
     * @throws EntityNotFoundException
     */
    @GET
    @Path("{clinicId}/prepaid")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getClinicPrepaid(@PathParam("clinicId") final Integer clinicId,
                                      @QueryParam("page") @DefaultValue("0") Integer page,
                                      @Context Request request) throws EntityNotFoundException {
        page = (page < 0) ? 0 : page;

        Clinic clinic = clinicService.getClinicById(clinicId);
        if(clinic == null) throw new EntityNotFoundException("clinic");
        List<PrepaidDto> prepaids = prepaidToClinicService.getPrepaidsForClinic(clinicId, page)
                .stream().map(PrepaidDto::fromPrepaid).collect(Collectors.toList());
        int maxPage = prepaidToClinicService.maxAvailablePagePerClinic(clinicId);
        String basePath = "/api/clinics/" + clinicId + "/prepaids?";
        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxPage, false);
        Response.ResponseBuilder response = CacheHelper.handleResponse(prepaids, prepaidCaching, new GenericEntity<List<PrepaidDto>>(prepaids) {},
                "prepaids", request);

        if (!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }

        return response.build();

    }

    /**
     * Returns list of all prepaid of a specific clinic
     * @param clinicId
     * @return list of Prepaids
     * @throws EntityNotFoundException
     */
    @GET
    @Path("{clinicId}/prepaid/all")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getAllClinicPrepaid(@PathParam("clinicId") final Integer clinicId,
                                      @Context Request request) throws EntityNotFoundException {

        Clinic clinic = clinicService.getClinicById(clinicId);
        if(clinic == null) throw new EntityNotFoundException("clinic");
        List<PrepaidDto> prepaids = prepaidToClinicService.getPrepaidsForClinic(clinicId)
                .stream().map(PrepaidDto::fromPrepaid).collect(Collectors.toList());
        return CacheHelper.handleResponse(prepaids, prepaidCaching, new GenericEntity<List<PrepaidDto>>(prepaids) {},
                "prepaids", request).build();

    }

    /**
     * Lets ADMIN add a prepaid to a specific clinic
     * @param clinicId
     * @param prepaidId
     * @return
     * @throws EntityNotFoundException
     */
    @POST
    @Path("{clinicId}/prepaids/{prepaidId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response addPrepaidToClinic(@PathParam("clinicId") final Integer clinicId,
                                       @PathParam("prepaidId") final String prepaidId)
            throws EntityNotFoundException {

        prepaidToClinicService.addPrepaidToClinic(prepaidId, clinicId);
        return Response.created(uriInfo.getAbsolutePath()).build();

    }

    /**
     * Lets ADMIN remove a prepaid from a specific clinic
     * @param clinicId
     * @param prepaid
     * @return
     * @throws EntityNotFoundException
     */
    @DELETE
    @Path("{clinicId}/prepaids/{prepaidId}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response removePrepaidFromClinic(@PathParam("clinicId") final Integer clinicId,
                                            @PathParam("prepaidId") final String prepaid)
            throws EntityNotFoundException {
        prepaidToClinicService.deletePrepaidFromClinic(prepaid, clinicId);
        return Response.noContent().build();

    }

}
