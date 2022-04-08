package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AppointmentService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Appointment;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exceptions.*;
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

        User user = userService.findUserByEmail(email);
        if(user == null) throw new EntityNotFoundException("user");

        List<AppointmentDto> appointments = appointmentService.getPaginatedAppointments(user, page)
                .stream().map(appointment -> AppointmentDto.fromAppointment(appointment, uriInfo))
                .collect(Collectors.toList());

        int maxPage = appointmentService.getMaxAvailablePage(user);

        String basePath = "/api/appointments?user=" + email;
        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxPage, true);
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
     * @param email
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
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#email, 'user')")
    public Response cancelAppointment(@QueryParam("user") final String email,
                                      @QueryParam("license") final String license,
                                      @QueryParam("year") final Integer year,
                                      @QueryParam("month") final Integer month,
                                      @QueryParam("day") final Integer day,
                                      @QueryParam("time") final Integer time) throws EntityNotFoundException {

        appointmentService.cancelUserAppointment(email, license, year, month, day, time);
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
        Appointment appointment = appointmentService.createAppointment(form.getLicense(), form.getClinic(),
                form.getPatient(), form.getYear(), form.getMonth(), form.getDay(),
                form.getTime());

        // TODO correct the URI
        String id = appointment.getDoctorClinic().getDoctor().getLicense()
                + "-" + appointment.getAppointmentKey().getDate().getYear()
                + "-" + appointment.getAppointmentKey().getDate().getMonthValue()
                + "-" + appointment.getAppointmentKey().getDate().getDayOfMonth()
                + "-" + appointment.getAppointmentKey().getDate().getHour();
        String encodedId = Base64.getUrlEncoder().encodeToString(id.getBytes(StandardCharsets.UTF_8));

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
        String decodedId = new String(Base64.getDecoder().decode(encodedId));

        Map<String, String> id = parseId(decodedId);

        Optional<Appointment> appointment = appointmentService.getAppointment(
                id.get("license"),
                Integer.parseInt(id.get("year")),
                Integer.parseInt(id.get("month")),
                Integer.parseInt(id.get("day")),
                Integer.parseInt(id.get("time"))
        );

        AppointmentDto dto = AppointmentDto.fromAppointment(
                appointment.orElseThrow(() -> new EntityNotFoundException("appointment")), uriInfo);

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


}