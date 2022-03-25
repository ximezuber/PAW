package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AppointmentService;
import ar.edu.itba.paw.interfaces.service.UserService;
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
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#user, 'user')")
    public Response cancelAppointment(@QueryParam("user") final String email,
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