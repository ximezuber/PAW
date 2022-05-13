package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.interfaces.service.FavoriteService;
import ar.edu.itba.paw.interfaces.service.PatientService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.exceptions.BadRequestException;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import ar.edu.itba.paw.model.exceptions.FavouriteExistsException;
import ar.edu.itba.paw.model.exceptions.NoPrepaidNumberException;
import ar.edu.itba.paw.webapp.caching.DoctorCaching;
import ar.edu.itba.paw.webapp.caching.PatientCaching;
import ar.edu.itba.paw.webapp.dto.DoctorDto;
import ar.edu.itba.paw.webapp.dto.PatientDto;
import ar.edu.itba.paw.webapp.form.PersonalInformationForm;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import ar.edu.itba.paw.webapp.helpers.CacheHelper;
import ar.edu.itba.paw.webapp.helpers.PaginationHelper;
import ar.edu.itba.paw.webapp.helpers.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("patients")
public class PatientController {

    @Context
    UriInfo uriInfo;

    @Autowired
    private PatientService patientService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientCaching patientCaching;

    @Autowired
    private DoctorCaching doctorCaching;

    @GET
    @Path("{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response getPatient(@PathParam("id") final String patientEmail,
                               @Context Request request) throws EntityNotFoundException {
        Patient patient = patientService.getPatientByEmail(patientEmail)
                .orElseThrow(() -> new EntityNotFoundException("patient"));

        PatientDto dto = PatientDto.fromPatient(patient, uriInfo);
        return CacheHelper.handleResponse(dto, patientCaching, "patient", request).build();

    }

    @DELETE
    @Path("{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response deletePatient(@PathParam("id") final String patientEmail) throws EntityNotFoundException {
        Patient patient = patientService.getPatientByEmail(patientEmail)
                .orElseThrow(() -> new EntityNotFoundException("patient"));

        patientService.deletePatient(patient);
        return Response.noContent().build();
    }

    @PUT
    @Path("{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response updatePatient(@PathParam("id") final String patientEmail,
                                  PersonalInformationForm form) throws EntityNotFoundException, NoPrepaidNumberException {
        Patient patient = patientService.getPatientByEmail(patientEmail)
                .orElseThrow(() -> new EntityNotFoundException("patient"));


        patientService.updatePatientProfile(patient, patientEmail, SecurityHelper.processNewPassword(form.getNewPassword(),
                passwordEncoder, userService, patientEmail), form.getFirstName(),
                form.getLastName(), form.getPrepaid(), form.getPrepaidNumber());
        return Response.noContent().build();

    }

    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPatient(final SignUpForm form) throws DuplicateEntityException, EntityNotFoundException,
            BadRequestException {
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        patientService.create(form.getId(), form.getPrepaid(),
                form.getPrepaidNumber(), form.getFirstName(),
                form.getLastName(), encodedPassword, form.getEmail());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(form.getEmail()).build()).build();
    }

    /**
     * Returns patient's favorite Doctors list.
     * If filtered by a license, it returns OK if Doctor is favourite or NOT FOUND if it is not.
     * @param patientEmail
     * @param page
     * @param license
     * @param request
     * @return
     * @throws EntityNotFoundException
     */
    @GET
    @Path("{id}/favorites")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response getPatientFavorites(@PathParam("id") String patientEmail,
                                        @QueryParam("page") @DefaultValue("0") Integer page,
                                        @QueryParam("license") String license,
                                        @Context Request request) throws EntityNotFoundException {
        page = (page < 0) ? 0 : page;

        Patient patient = patientService.getPatientByEmail(patientEmail)
                .orElseThrow(() -> new EntityNotFoundException("patient"));

        if (license != null) {
            Doctor doc = doctorService.getDoctorByLicense(license)
                    .orElseThrow(() -> new EntityNotFoundException("doctor"));
            if (favoriteService.getFavorite(doc, patient).isPresent()) {
                DoctorDto dto = DoctorDto.fromDoctor(doc, uriInfo);
                List<DoctorDto> docDto = Collections.singletonList(dto);
                return CacheHelper.handleResponse(docDto, doctorCaching,
                        new GenericEntity<List<DoctorDto>>(docDto) {},
                        "favoritesFiltered", request).build();
            } else {
                return Response.ok().build();
            }
        }

        List<DoctorDto> favorites = favoriteService.getPaginatedObjects(page, patient)
                .stream().map(f -> DoctorDto.fromDoctor(f.getDoctor(), uriInfo)).collect(Collectors.toList());
        int maxPage = favoriteService.maxAvailablePage(patient);

        URI basePath = uriInfo.getAbsolutePathBuilder().build();
        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxPage);
        Response.ResponseBuilder response = CacheHelper.handleResponse(favorites, doctorCaching,
                new GenericEntity<List<DoctorDto>>(favorites) {},
                "favorites", request);
        if(!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }

        return response.build();
        // return Response.ok(new GenericEntity<List<DoctorDto>>(favorites) {}).build();

    }

    @DELETE
    @Path("{id}/favorites")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response removeFromFavorites(@PathParam("id") final String patientEmail,
                                        @QueryParam("license") String doctorLicense) throws EntityNotFoundException {
        Patient patient = patientService.getPatientByEmail(patientEmail)
                .orElseThrow(() -> new EntityNotFoundException("patient"));
        Doctor doc = doctorService.getDoctorByLicense(doctorLicense)
                .orElseThrow(() -> new EntityNotFoundException("doctor"));

        patientService.deleteFavorite(patient, doc);
        return Response.noContent().build();
    }

    @POST
    @Path("{id}/favorites")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response addFavorite(@PathParam("id") final String patientEmail,
                                @QueryParam("license") String doctorLicense)
            throws EntityNotFoundException, FavouriteExistsException {

        Patient patient = patientService.getPatientByEmail(patientEmail)
                .orElseThrow(() -> new EntityNotFoundException("patient"));
        Doctor doc = doctorService.getDoctorByLicense(doctorLicense)
                .orElseThrow(() -> new EntityNotFoundException("doctor"));

        patientService.addFavorite(patient, doc);
        return Response.created(uriInfo.getAbsolutePath()).build();
    }

}
