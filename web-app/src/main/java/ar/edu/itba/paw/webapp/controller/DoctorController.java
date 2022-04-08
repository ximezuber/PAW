package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.*;
import ar.edu.itba.paw.webapp.caching.*;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.helpers.CacheHelper;
import ar.edu.itba.paw.webapp.helpers.PaginationHelper;
import ar.edu.itba.paw.webapp.helpers.SecurityHelper;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

@Component
@Path("doctors")
public class DoctorController {

    @Autowired
    private DoctorClinicService doctorClinicService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SpecialtyService specialtyService;

    @Autowired
    private DoctorCaching doctorCaching;

    @Autowired
    private ImageCaching imageCaching;

    @Autowired
    private DoctorClinicCaching doctorClinicCaching;

    @Autowired
    private ScheduleCaching scheduleCaching;

    @Autowired
    private AppointmentCaching appointmentCaching;

    @Context
    private UriInfo uriInfo;

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 megas

    private static final List<String> SUPPORTED_IMAGES_TYPES = Arrays.asList(".jpg", ".jpeg", ".png");

    /**
     * Returns a list of doctors (who are subscribed to a clinic and their schedule on that clinic is
     * not empty) optionally filtered by the params: location, specialty, firstName, lastName, price,
     * prepaid
     * @param page
     * @param location
     * @param specialty
     * @param firstName
     * @param lastName
     * @param consultPrice
     * @param prepaid
     * @return list of Doctors
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getAvailableDoctors(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("location") @DefaultValue("") final String location,
            @QueryParam("specialty") @DefaultValue("") final String specialty,
            @QueryParam("firstName") @DefaultValue("") final String firstName,
            @QueryParam("lastName") @DefaultValue("") final String lastName,
            @QueryParam("consultPrice") @DefaultValue("0") final Integer consultPrice,
            @QueryParam("prepaid") @DefaultValue("") final String prepaid,
            @Context Request request) {

        page = (page < 0) ? 0 : page;

        List<Doctor> docs = doctorClinicService.getPaginatedFilteredDoctorClinics(new Location(location),
                new Specialty(specialty), firstName, lastName, new Prepaid(prepaid), consultPrice, page);
        int maxAvailablePage = doctorClinicService.maxAvailableFilteredDoctorClinicPage(new Location(location),
                new Specialty(specialty), firstName, lastName, new Prepaid(prepaid), consultPrice);

        List<DoctorDto> doctors = docs
                .stream().map(d -> DoctorDto.fromDoctor(d, uriInfo)).collect(Collectors.toList());

        URI basePath = uriInfo.getAbsolutePathBuilder()
                .queryParam("location", location)
                .queryParam("specialty", specialty)
                .queryParam("firstName", firstName)
                .queryParam("lastName", lastName)
                .queryParam("consultPrice", consultPrice)
                .queryParam("prepaid", prepaid).build();
        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxAvailablePage);
        Response.ResponseBuilder response =  CacheHelper.handleResponse(doctors, doctorCaching, new GenericEntity<List<DoctorDto>>(doctors) {},
                        "doctors", request);
        if (!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }

        return response.build();
    }

    /**
     * Returns paginated list of doctors for ADMIN to manage.
     * @param page
     * @return list of Doctors
     */
    @GET
    @Path("/all")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getAllDoctorsPaginated(@QueryParam("page") @DefaultValue("0") Integer page,
                                           @Context Request request) {
        page = (page < 0) ? 0 : page;

        List<String> licenses = doctorService.getDoctors().stream().map(Doctor::getLicense).collect(Collectors.toList());
        int maxAvailablePage = doctorService.getMaxAvailableDoctorsPage(licenses);

        URI basePath = uriInfo.getAbsolutePathBuilder().build();
        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, maxAvailablePage);

        List<DoctorDto> doctors = doctorService.getPaginatedDoctors(licenses, page)
                .stream().map(d -> DoctorDto.fromDoctor(d, uriInfo)).collect(Collectors.toList());

        Response.ResponseBuilder response =  CacheHelper.handleResponse(doctors, doctorCaching, new GenericEntity<List<DoctorDto>>(doctors) {},
                        "doctors", request);
        if(!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }
        return response.build();
    }

    /**
     * Lets USER access doctor's information
     * @param license
     * @return Doctor
     * @throws NotFoundException
     */
    @GET
    @Path("/{license}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getDoctor(@PathParam("license") final String license,
                              @Context Request request) throws NotFoundException {
        Doctor doctor = doctorService.getDoctorByLicense(license);
        if(doctor != null) {
            DoctorDto dto = DoctorDto.fromDoctor(doctor, uriInfo);
            return CacheHelper.handleResponse(dto, doctorCaching, "doctor", request).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Returns doctor's information by email. Used when doctor is logged in, to access its
     * information.
     * @param email
     * @return Doctor
     * @throws EntityNotFoundException
     */
    @GET
    @Path("email/{email}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getDoctorByEmail(@PathParam("email") final String email,
                                     @Context Request request) throws EntityNotFoundException {
        Doctor doctor = doctorService.getDoctorByEmail(email);
        if (doctor == null) throw new EntityNotFoundException("doctor");
        DoctorDto dto = DoctorDto.fromDoctor(doctor, uriInfo);
        return CacheHelper.handleResponse(dto, doctorCaching, "doctor", request).build();
    }

    /**
     * Lets ADMIN delete a doctor from application
     * @param license
     * @return
     * @throws EntityNotFoundException
     */
    @DELETE
    @Path("/{license}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response deleteDoctor(@PathParam("license") final String license) throws EntityNotFoundException {
        doctorService.deleteDoctor(license);
        return Response.noContent().build();
    }

    /**
     * Lets DOCTOR edit its information
     * @param license
     * @param form
     * @return
     * @throws EntityNotFoundException
     */
    @PUT
    @Path("/{license}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("hasPermission(#license, 'doctor')")
    public Response updateDoctor(@PathParam("license") final String license, @Valid EditDoctorProfileForm form)
            throws EntityNotFoundException {
        Doctor doctor = doctorService.getDoctorByLicense(license);
        if(doctor == null) throw new EntityNotFoundException("doctor");
            doctorService.updateDoctorProfile(
                    doctor.getEmail(),
                    SecurityHelper.processNewPassword(form.getNewPassword(), passwordEncoder, userService, doctor.getEmail()),
                    form.getFirstName(),form.getLastName(),
                    form.getPhoneNumber(),form.getSpecialty());
            return Response.noContent().build();
    }

    /**
     * Lets ADMIN add doctor to application
     * @param form
     * @return
     * @throws DuplicateEntityException
     * @throws EntityNotFoundException
     * @throws ar.edu.itba.paw.model.exceptions.BadRequestException
     */
    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDoctor(final DoctorForm form)
            throws DuplicateEntityException, EntityNotFoundException,
            ar.edu.itba.paw.model.exceptions.BadRequestException {
        if (!form.getPassword().equals(form.getRepeatPassword()))
            throw new ar.edu.itba.paw.model.exceptions.BadRequestException("password-mismatch");
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        Specialty specialty = specialtyService.getSpecialtyByName(form.getSpecialty());
        if (specialty == null) throw new EntityNotFoundException("specialty");
        doctorService.createDoctor(specialty, form.getLicense(), form.getPhoneNumber()
                ,form.getFirstName(), form.getLastName(), encodedPassword, form.getEmail());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(form.getLicense()).build()).build();
    }

    /**
     * Returns profile image for a specific doctor
     * @param license
     * @return Image
     * @throws EntityNotFoundException
     */
    @GET
    @Path("/{license}/image")
    @Produces(value = { MediaType.MULTIPART_FORM_DATA })
    public Response getProfileImage(@PathParam("license") final String license,
                                    @Context Request request) throws EntityNotFoundException {
        Doctor d = doctorService.getDoctorByLicense(license);
        if(d == null) throw new EntityNotFoundException("doctor");

        Image img = imageService.getProfileImage(d.getLicense());
        if (img == null) throw new EntityNotFoundException("image");

        ImageDto dto = ImageDto.fromImage(img.getImage());
        return CacheHelper.handleResponse(dto.getImage(), imageCaching, "profileImage", request).build();
    }

    /**
     * Lets DOCTOR delete its profile image
     * @param license
     * @return
     * @throws EntityNotFoundException
     */
    @DELETE
    @Path("/{license}/image")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#license, 'doctor')")
    public Response deleteProfileImage(@PathParam("license") final String license) throws EntityNotFoundException {
        Doctor doctor = doctorService.getDoctorByLicense(license);
        if(doctor == null) throw new EntityNotFoundException("doctor");
        imageService.deleteProfileImage(license);
        return Response.noContent().build();
    }

    /**
     * Lets DOCTOR upload its profile image
     * @param license
     * @param fileInputStream
     * @param fileMetaData
     * @return
     * @throws EntityNotFoundException
     * @throws ar.edu.itba.paw.model.exceptions.BadRequestException
     * @throws ImageTooLargeException
     * @throws UnsupportedMediaTypeException
     * @throws IOException
     */
    @POST
    @Path("/{license}/image")
    @Consumes("multipart/form-data")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#license, 'doctor')")
    public Response uploadProfileImage(@PathParam("license") final String license,
                                       @FormDataParam("profileImage") InputStream fileInputStream,
                                       @FormDataParam("profileImage") FormDataContentDisposition fileMetaData)
            throws EntityNotFoundException, ar.edu.itba.paw.model.exceptions.BadRequestException,
            ImageTooLargeException, UnsupportedMediaTypeException, IOException {
        Doctor doctor = doctorService.getDoctorByLicense(license);
        if (doctor == null) throw new EntityNotFoundException("doctor");
        if (fileInputStream == null || fileMetaData == null) throw new ImageInfoMissingException();
        String extension = fileMetaData.getFileName().substring(fileMetaData.getFileName().lastIndexOf('.'));
        if (!SUPPORTED_IMAGES_TYPES.contains(extension))
            throw new UnsupportedMediaTypeException("image-type-not-supported");

        byte[] array = IOUtils.toByteArray(fileInputStream);

        if (array.length > MAX_FILE_SIZE) throw new ImageTooLargeException();

        if (imageService.getProfileImage(doctor.getLicense()) == null)
            imageService.createProfileImage(array, doctor);
        else {
            imageService.deleteProfileImage(license);
            imageService.createProfileImage(array, doctor);
        }
        return Response.created(uriInfo.getAbsolutePath()).build();

    }

    /**
     * Returns paginated list of clinics a specific doctor is subscribed to.
     * @param license
     * @param page
     * @param request
     * @return list of DoctorClinics
     * @throws EntityNotFoundException
     */
    @GET
    @Path("/{license}/clinics")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getDoctorPage(@PathParam("license") final String license,
                                  @QueryParam("page") @DefaultValue("0") Integer page,
                                  @Context Request request) throws EntityNotFoundException {
        page = (page < 0) ? 0 : page;

        Doctor doctor = doctorService.getDoctorByLicense(license);
        if(doctor == null) throw new EntityNotFoundException("doctor");
        final List<DoctorClinicDto> doctorClinics = doctorClinicService.getPaginatedDoctorsClinics(doctor, page)
                .stream().map(dc -> DoctorClinicDto.fromDoctorClinic(dc, uriInfo))
                .collect(Collectors.toList());
        int max = doctorClinicService.maxAvailablePage(doctor);

        URI basePath = uriInfo.getAbsolutePathBuilder().build();
        String linkValue = PaginationHelper.linkHeaderValueBuilder(basePath, page, max);
        Response.ResponseBuilder response = CacheHelper.handleResponse(doctorClinics, doctorClinicCaching,
                new GenericEntity<List<DoctorClinicDto>>(doctorClinics) {},
                "doctorsClinics", request);

        if (!linkValue.isEmpty()) {
            response.header("Link", linkValue);
        }
        return response.build();
    }

    /**
     * Returns list of all clinics a specific doctor is suscribed to.
     * @param license
     * @param week
     * @return list of DoctorClinics
     * @throws EntityNotFoundException
     */
    @GET
    @Path("/{license}/clinics/all")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getAllDoctorsClinics(@PathParam("license") final String license,
                                         @QueryParam("week") @DefaultValue("1") final Integer week,
                                         @Context Request request) throws EntityNotFoundException {
        Doctor doctor = doctorService.getDoctorByLicense(license);
        if(doctor == null) throw new EntityNotFoundException("doctor");
        final List<DoctorClinicDto> doctorClinics = doctorClinicService.getDoctorsSubscribedClinics(doctor)
                .stream().map(dc -> DoctorClinicDto.fromDoctorClinic(dc, uriInfo))
                .collect(Collectors.toList());
        return CacheHelper.handleResponse(doctorClinics, doctorClinicCaching,
                new GenericEntity<List<DoctorClinicDto>>(doctorClinics) {},
                "doctorsClinics", request).build();
    }

    /**
     * Lets DOCTOR suscribe to a clinic
     * @param license
     * @param form
     * @return
     * @throws EntityNotFoundException
     */
    @POST
    @Path("/{license}/clinics")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#license, 'doctor')")
    public Response createDoctorClinic(@PathParam("license") final String license,
                                       final DoctorClinicForm form) throws EntityNotFoundException,
            DuplicateEntityException {
        Doctor doctor = doctorService.getDoctorByLicense(license);
        if(doctor == null) throw new EntityNotFoundException("doctor");
        doctorClinicService.createDoctorClinic(doctor.getEmail(), form.getClinic(), form.getConsultPrice());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(form.getClinic())).build()).build();
    }

    /**
     * Lets DOCTOR unsubscribe from clinic
     * @param license
     * @param clinic
     * @return
     * @throws EntityNotFoundException
     */
    @DELETE
    @Path("/{license}/clinics/{clinic}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#license, 'doctor')")
    public Response deleteDoctorClinic(@PathParam("license") final String license,
                                       @PathParam("clinic") final Integer clinic) throws EntityNotFoundException {

        doctorClinicService.deleteDoctorClinic(license, clinic);
        return Response.noContent().build();
    }

    /**
     * Lets DOCTOR edit consult price for specific clinic
     * @param license
     * @param clinic
     * @param price
     * @return
     * @throws EntityNotFoundException
     */
    @PUT
    @Path("/{license}/clinics/{clinic}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#license, 'doctor')")
    public Response editDoctorClinicPrice (@PathParam("license") final String license,
                                           @PathParam("clinic") final Integer clinic,
                                           @QueryParam("price") final Integer price) throws EntityNotFoundException {
        doctorClinicService.editPrice(license, clinic, price);
        return Response.noContent().build();
    }


    @GET
    @Path("/{license}/clinics/{clinic}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getDoctorClinic(@PathParam("license") final String license,
                                    @PathParam("clinic") final Integer clinic,
                                    @QueryParam("week") @DefaultValue("1") final Integer week,
                                    @Context Request request) {
        DoctorClinic dc = doctorClinicService.getDoctorClinic(license,clinic);

        if(dc != null) {
            DoctorClinicDto dto = DoctorClinicDto.fromDoctorClinic(dc, uriInfo);
            return CacheHelper.handleResponse(dto, doctorClinicCaching, "doctorsClinic", request)
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
    }

    /**
     * Returns doctors schedule for a specific clinic
     * @param license
     * @param clinic
     * @return List if Schedules
     */
    @GET
    @Path("/{license}/clinics/{clinic}/schedules")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getDoctorClinicSchedules(@PathParam("license") final String license,
                                             @PathParam("clinic") final Integer clinic,
                                             @Context Request request) throws EntityNotFoundException {
        DoctorClinic dc = doctorClinicService.getDoctorClinic(license, clinic);
        if(dc == null) throw new EntityNotFoundException("doctor-clinic");
        List<ScheduleDto> schedules = scheduleService.getDoctorClinicSchedule(dc)
                .stream().map(s -> ScheduleDto.fromSchedule(s, uriInfo)).collect(Collectors.toList());
        return CacheHelper.handleResponse(schedules, scheduleCaching,
                new GenericEntity<List<ScheduleDto>>(schedules) {},"schedules", request).build();
    }

    /**
     * Returns doctor schedule for all clinics, used by doctor to see other clinic's schedule
     * while adding a working hour to specific clinic
     * @param license
     * @return
     */
    @GET
    @Path("/{license}/schedules")
    @Produces(value = { MediaType.APPLICATION_JSON })
    public Response getDoctorSchedules(@PathParam("license") final String license,
                                       @Context Request request) throws EntityNotFoundException {
        Doctor doctor = doctorService.getDoctorByLicense(license);
        if(doctor == null) throw new EntityNotFoundException("doctor");

        List<ScheduleDto> schedules = scheduleService.getDoctorSchedule(doctor).stream()
                .map(s -> ScheduleDto.fromSchedule(s, uriInfo)).collect(Collectors.toList());
        return CacheHelper.handleResponse(schedules, scheduleCaching,
                new GenericEntity<List<ScheduleDto>>(schedules) {},"schedules", request).build();
    }
    /**
     * Lets DOCTOR delete a scheduled working hour for a specific clinic
     * @param license
     * @param clinic
     * @param day
     * @param hour
     * @return
     */
    @DELETE
    @Path("/{license}/clinics/{clinic}/schedules")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#license, 'doctor')")
    public Response deleteDoctorClinicSchedule(@PathParam("license") final String license,
                                               @PathParam("clinic") final Integer clinic,
                                               @QueryParam("day") final Integer day,
                                               @QueryParam("hour") final Integer hour)
            throws EntityNotFoundException, OutOfRangeException {
        DoctorClinic dc = doctorClinicService.getDoctorClinic(license,clinic);
        if(dc == null) throw new EntityNotFoundException("doctor-clinic");

        scheduleService.deleteSchedule(hour, day, license, clinic);
        return Response.noContent().build();
    }

    /**
     * Lets DOCTOR schedule a working hour for a specific clinic
     * @param license
     * @param clinic
     * @param form
     * @return
     */
    @POST
    @Path("/{license}/clinics/{clinic}/schedules")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("hasPermission(#license, 'doctor')")
    public Response createSchedule(@PathParam("license") final String license,
                                   @PathParam("clinic") final Integer clinic,
                                   ScheduleForm form) throws EntityNotFoundException, ConflictException {
        DoctorClinic dc = doctorClinicService.getDoctorClinic(license, clinic);
        if(dc == null) throw new EntityNotFoundException("doctor-clinic");

        scheduleService.createSchedule(form.getHour(), form.getDay(), dc.getDoctor().getLicense(),
                dc.getClinic().getId());
        return Response.created(uriInfo.getAbsolutePath()).build();

    }


    /**
     * Returns a list of all available Appointments from today to 9 weeks in the future, to check doctor's
     * availability
     * @param license
     * @return List of Appointments
     * @throws EntityNotFoundException
     */
    @GET
    @Path("/{license}/appointments")
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
}
