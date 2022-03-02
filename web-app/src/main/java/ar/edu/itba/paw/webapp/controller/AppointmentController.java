package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AppointmentService;
import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.*;
import ar.edu.itba.paw.webapp.caching.AppointmentCaching;
import ar.edu.itba.paw.webapp.dto.AppointmentDto;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import ar.edu.itba.paw.webapp.helpers.CacheHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("appointments")
public class AppointmentController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentCaching appointmentCaching;

    @Context
    private UriInfo uriInfo;

    /**
     * Returns a paginated list of a users appointments (doctor or patient)
     * @param email
     * @return List of Appointment
     * @throws EntityNotFoundException
     */
    @GET
    @Path("{user}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#email, 'user')")
    public Response getUserAppointments(@PathParam("user") final String email,
                                        @QueryParam("page") @DefaultValue("0") Integer page,
                                        @Context Request request) throws EntityNotFoundException {
        page = (page < 0) ? 0 : page;

        User user = userService.findUserByEmail(email);
        if(user == null) throw new EntityNotFoundException("user");

        List<AppointmentDto> appointments = appointmentService.getPaginatedAppointments(user, page)
                .stream().map(appointment -> AppointmentDto.fromAppointment(appointment, uriInfo))
                .collect(Collectors.toList());

        int maxPage = appointmentService.getMaxAvailablePage(user);
        return CacheHelper.handleResponse(appointments, appointmentCaching,
                new GenericEntity<List<AppointmentDto>>(appointments) {}, "appointments",
                request)
                .header("Access-Control-Expose-Headers", "X-max-page")
                .header("X-max-page", maxPage).build();


    }

    /**
     * Returns a list of all available Appointments from today to 9 weeks in the future, to check doctor's
     * availability
     * @param license
     * @return List of Appointments
     * @throws EntityNotFoundException
     */
    @GET
    @Path("available/{license}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getDoctorAvailableAppointments(@PathParam("license") final String license,
                                                   @Context Request request) throws EntityNotFoundException {
        Doctor doc = doctorService.getDoctorByLicense(license);
        if (doc == null) throw new EntityNotFoundException("doctor");

        List<AppointmentDto> appointments = appointmentService.getDoctorsAvailableAppointments(doc)
                .stream().map(appointment -> AppointmentDto.fromAppointment(appointment, uriInfo))
                .collect(Collectors.toList());
        return CacheHelper.handleResponse(appointments, appointmentCaching,
                new GenericEntity<List<AppointmentDto>>(appointments) {}, "appointments",
                request).build();
    }

    /**
     * For doctor or user to cancel an appointment
     * @param email
     * @param clinic
     * @param license
     * @param year
     * @param month
     * @param day
     * @param time
     * @return
     * @throws EntityNotFoundException
     * @throws RequestEntityNotFoundException
     */
    @DELETE
    @Path("{email}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#email, 'user')")
    public Response cancelAppointment(@PathParam("email") final String email,
                                      @QueryParam("clinic") final Integer clinic,
                                      @QueryParam("license") final String license,
                                      @QueryParam("year") final Integer year,
                                      @QueryParam("month") final Integer month,
                                      @QueryParam("day") final Integer day,
                                      @QueryParam("time") final Integer time) throws EntityNotFoundException,
            RequestEntityNotFoundException {

        appointmentService.cancelUserAppointment(email, license, clinic, year, month, day, time);
        return Response.noContent().build();


    }

    @GET
    @Path("{userId}/{clinicId}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#email, 'user')")
    public Response getUserAppointmentsForClinic(@PathParam("userId") final String email,
                                                 @PathParam("clinicId") final Integer clinicId,
                                                 @Context Request request) {
        User user = userService.findUserByEmail(email);
        Clinic clinic = clinicService.getClinicById(clinicId);
        if(user != null && clinic != null) {
            List<AppointmentDto> appointments = appointmentService.getUserAppointmentsForClinic(user, clinic)
                    .stream().map(appointment -> AppointmentDto.fromAppointment(appointment, uriInfo))
                    .collect(Collectors.toList());
            return CacheHelper.handleResponse(appointments, appointmentCaching,
                    new GenericEntity<List<AppointmentDto>>(appointments) {},
                    "appointments", request).build();
        }
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).build();
    }

    @DELETE
    @Path("{userId}/{clinicId}")
    @PreAuthorize("hasPermission(#email, 'user')")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response cancelAppointmentInClinic(@PathParam("userId") final String email,
                                      @PathParam("clinicId") final Integer clinic,
                                      @QueryParam("license") final String license,
                                      @QueryParam("year") final Integer year,
                                      @QueryParam("month") final Integer month,
                                      @QueryParam("day") final Integer day,
                                      @QueryParam("time") final Integer time) throws EntityNotFoundException,
            RequestEntityNotFoundException {

        appointmentService.cancelUserAppointment(email, license, clinic, year, month, day, time);
        return Response.noContent().build();


    }


    /**
     * For USER to make an appointment
     * @param form
     * @return
     */
    @POST
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("hasPermission(#form.patient, 'user')")
    public Response createAppointment(final AppointmentForm form) throws
            DateInPastException, OutOfScheduleException, AppointmentAlreadyScheduledException, HasAppointmentException {
        appointmentService.createAppointment(form.getLicense(), form.getClinic(),
                form.getPatient(), form.getYear(), form.getMonth(), form.getDay(),
                form.getTime());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(form.getPatient())
                .path(String.valueOf(form.getClinic())).build()).build();
    }
}