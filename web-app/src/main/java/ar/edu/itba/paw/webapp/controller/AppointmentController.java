package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.*;
import ar.edu.itba.paw.model.exceptions.NotFoundException;
import ar.edu.itba.paw.webapp.caching.AppointmentCaching;
import ar.edu.itba.paw.webapp.dto.AppointmentDto;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import ar.edu.itba.paw.webapp.helpers.CacheHelper;
import ar.edu.itba.paw.webapp.helpers.PaginationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Path("appointments")
public class AppointmentController {

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private DoctorClinicService doctorClinicService;

    @Autowired
    private AppointmentCaching appointmentCaching;

    @Context
    private UriInfo uriInfo;

    // TODO see if there is a way to move this to doctorController and patientController
    /**
     * Returns a paginated list of a users appointments (doctor or patient)
     * @param email doctor's or patient's email
     * @return List of Appointment
     * @throws EntityNotFoundException
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#email, 'user')")
    public Response getUserAppointments(@NotNull @QueryParam("user") final String email,
                                        @QueryParam("page") @DefaultValue("0") Integer page,
                                        @Context Request request) throws EntityNotFoundException {
        page = (page < 0) ? 0 : page;

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user"));


        List<AppointmentDto> appointments = appointmentService.getPaginatedAppointments(user, page)
                .stream().map(appointment -> {
                    String encodedID = encodeId(appointment);
                    return AppointmentDto.fromAppointment(appointment,encodedID, uriInfo);
                })
                .collect(Collectors.toList());

        int maxPage = appointmentService.getMaxAvailablePage(user);

        URI basePath = uriInfo.getAbsolutePathBuilder().queryParam("user", email).build();

        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxPage);
        Response.ResponseBuilder response = CacheHelper.handleResponse(appointments, appointmentCaching,
                new GenericEntity<List<AppointmentDto>>(appointments) {}, "appointments",
                request);
        if (!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }

        return response.build();
    }

    /**
     * For doctor or user to cancel an appointment
     * @return
     * @throws EntityNotFoundException
     * @throws RequestEntityNotFoundException
     */
    @DELETE()
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response cancelAppointment(@PathParam("id") final String encodedId,
                                      @NotNull @QueryParam("user") final String email,
                                      @Context Request request) throws NotFoundException, MalformedIdException {

        Appointment appointment = getAppointmentByEncodedId(encodedId)
                .orElseThrow(() -> new NotFoundException("appointment"));
        appointmentService.cancelUserAppointment(
                email,
                appointment.getDoctorClinic().getDoctor().getLicense(),
                appointment.getAppointmentKey().getDate().getYear(),
                appointment.getAppointmentKey().getDate().getMonthValue(),
                appointment.getAppointmentKey().getDate().getDayOfMonth(),
                appointment.getAppointmentKey().getDate().getHour());
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
            DateInPastException, OutOfScheduleException, AppointmentAlreadyScheduledException,
            HasAppointmentException, EntityNotFoundException {
        Doctor doctor = doctorService.getDoctorByLicense(form.getLicense())
                .orElseThrow(() -> new EntityNotFoundException("doctor"));

        Clinic clinic = clinicService.getClinicById(form.getClinic())
                .orElseThrow(() -> new EntityNotFoundException("clinic"));

        DoctorClinic doctorClinic = doctorClinicService.getDoctorClinic(doctor, clinic)
                .orElseThrow(() -> new EntityNotFoundException("doctor-in-clinic"));

        User user = userService.findUserByEmail(form.getPatient())
                .orElseThrow(() -> new EntityNotFoundException("patient"));

        Appointment appointment = appointmentService.createAppointment(doctorClinic,
                user, form.getYear(), form.getMonth(), form.getDay(),
                form.getTime());

        String encodedId = encodeId(appointment);

        return Response.created(uriInfo.getBaseUriBuilder().path("appointments")
                .path(encodedId).queryParam("user", form.getPatient()).build()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#email, 'user')")
    public Response getUserAppointments(@PathParam("id") final String encodedId,
                                        @NotNull @QueryParam("user") final String email,
                                        @Context Request request) throws EntityNotFoundException, MalformedIdException {
        Optional<Appointment> appointment = getAppointmentByEncodedId(encodedId);

        AppointmentDto dto = AppointmentDto.fromAppointment(
                appointment.orElseThrow(() -> new EntityNotFoundException("appointment")), encodedId, uriInfo);

        Response.ResponseBuilder response = CacheHelper.handleResponse(dto, appointmentCaching,
                 "appointments", request);

        return response.build();
    }

    private Map<String, String> parseId(String decodedId) throws MalformedIdException {
        Map<String, String> map = new HashMap<>();
        String[] id = decodedId.split("-");
        if (id.length < 5) throw new MalformedIdException("appointment");
        map.put("license", id[0]);
        map.put("year", id[1]);
        map.put("month", id[2]);
        map.put("day", id[3]);
        map.put("time", id[4]);
        return map;
    }

    private String encodeId (Appointment appointment) {
        String id = appointment.getDoctorClinic().getDoctor().getLicense()
            + "-" + appointment.getAppointmentKey().getDate().getYear()
            + "-" + appointment.getAppointmentKey().getDate().getMonthValue()
            + "-" + appointment.getAppointmentKey().getDate().getDayOfMonth()
            + "-" + appointment.getAppointmentKey().getDate().getHour();
        return Base64.getUrlEncoder().encodeToString(id.getBytes(StandardCharsets.UTF_8));
    }

    private Optional<Appointment> getAppointmentByEncodedId(String encodedId) throws MalformedIdException,
            EntityNotFoundException {
        String decodedId = new String(Base64.getDecoder().decode(encodedId));

        Map<String, String> id = parseId(decodedId);

        Doctor doctor = doctorService.getDoctorByLicense(id.get("license"))
                .orElseThrow(() -> new EntityNotFoundException("doctor"));

        return  appointmentService.getAppointment(
                doctor,
                Integer.parseInt(id.get("year")),
                Integer.parseInt(id.get("month")),
                Integer.parseInt(id.get("day")),
                Integer.parseInt(id.get("time"))
        );
    }


}