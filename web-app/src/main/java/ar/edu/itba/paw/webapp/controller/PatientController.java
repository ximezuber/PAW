package ar.edu.itba.paw.webapp.controller;

/*
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.form.PersonalInformationForm;
import ar.edu.itba.paw.webapp.helpers.SecurityHelper;
import ar.edu.itba.paw.webapp.helpers.UserContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PrepaidService prepaidService;

    @ModelAttribute
    public void prepaids(Model model) {
        model.addAttribute("prepaids", prepaidService.getPrepaids());
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.GET })
    public ModelAndView profile() {
        final ModelAndView mav = new ModelAndView("patient/profile");
        User user = UserContextHelper.getLoggedUser(SecurityContextHolder.getContext(), userService);
        Patient patient = patientService.getPatientByEmail(user.getEmail());

        mav.addObject("user", user);
        mav.addObject("patient", patient);

        return mav;
    }

    @RequestMapping(value = "/editProfile", method = { RequestMethod.GET })
    public ModelAndView editProfile(@ModelAttribute("personalInformationForm") final PersonalInformationForm form) {
        final ModelAndView mav = new ModelAndView("patient/editProfile");
        User user = UserContextHelper.getLoggedUser(SecurityContextHolder.getContext(), userService);
        Patient patient = patientService.getPatientByEmail(user.getEmail());
        setFormInformation(form, user, patient);

        mav.addObject("user", user);
        mav.addObject("patient", patient);

        return mav;
    }

    @RequestMapping(value = "/editProfile", method = { RequestMethod.POST })
    public ModelAndView editedProfile(@Valid @ModelAttribute("personalInformationForm") final PersonalInformationForm form,
                                      final BindingResult errors) {
        
        if(errors.hasErrors()){
            return editProfile(form);
        }

        User user = UserContextHelper.getLoggedUser(SecurityContextHolder.getContext(), userService);
        userService.updateUser(user.getEmail(),
                SecurityHelper.processNewPassword(form.getNewPassword(), passwordEncoder),
                form.getFirstName(),form.getLastName());
        patientService.updatePatient(user.getEmail(),form.getPrepaid(),form.getPrepaidNumber());

        return profile();
    }

    @RequestMapping(value = "/appointments", method = { RequestMethod.GET })
    public ModelAndView appointments() {

        final ModelAndView mav = new ModelAndView("patient/appointments");

        User user = UserContextHelper.getLoggedUser(SecurityContextHolder.getContext(), userService);
        Patient patient = patientService.getPatientByEmail(user.getEmail());
        patientService.setAppointments(patient);

        mav.addObject("user", user);
        mav.addObject("patient", patient);

        return mav;
    }

    @RequestMapping(value = "/favorites",  method = { RequestMethod.GET })
    public ModelAndView favorites(){
        final ModelAndView mav = new ModelAndView(("patient/favorites"));
        User user = UserContextHelper.getLoggedUser(SecurityContextHolder.getContext(), userService);
        Patient patient = patientService.getPatientByEmail(user.getEmail());
        List<Doctor> favorites = patientService.getPatientFavoriteDoctors(patient);

        mav.addObject("user", user);
        mav.addObject("patient", patient);
        mav.addObject("favorites",favorites);

        return mav;
    }

    @RequestMapping(value = "/addFavorite/{doctorId}", method = { RequestMethod.GET })
    public ModelAndView addFavorite(@PathVariable("doctorId") String license){

        User user = UserContextHelper.getLoggedUser(SecurityContextHolder.getContext(), userService);
        patientService.addFavorite(user.getEmail(), license);

        final ModelAndView mav = new ModelAndView("redirect:/results/" + license);

        return mav;
    }

    @RequestMapping(value = "/deleteFavorite/{doctorId}", method = { RequestMethod.GET })
    public ModelAndView deleteFavorite(@PathVariable("doctorId") String license, HttpServletRequest request){

        User user = UserContextHelper.getLoggedUser(SecurityContextHolder.getContext(), userService);
        patientService.deleteFavorite(user.getEmail(), license);

        String referer = request.getHeader("Referer");
        final ModelAndView mav = new ModelAndView("redirect:" + referer);
        return mav;
    }

    // Private methods for PatientController //

    private void setFormInformation(PersonalInformationForm form, User user, Patient patient) {
        form.setFirstName(user.getFirstName());
        form.setLastName(user.getLastName());
        form.setPrepaid(patient.getPrepaid());
        form.setPrepaidNumber(patient.getPrepaidNumber());
    }
} */

import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.interfaces.service.FavoriteService;
import ar.edu.itba.paw.interfaces.service.PatientService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import ar.edu.itba.paw.model.exceptions.FavouriteExistsException;
import ar.edu.itba.paw.webapp.caching.DoctorCaching;
import ar.edu.itba.paw.webapp.caching.PatientCaching;
import ar.edu.itba.paw.webapp.dto.DoctorDto;
import ar.edu.itba.paw.webapp.dto.PatientDto;
import ar.edu.itba.paw.webapp.form.FavoriteForm;
import ar.edu.itba.paw.webapp.form.PersonalInformationForm;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import ar.edu.itba.paw.webapp.helpers.CacheHelper;
import ar.edu.itba.paw.webapp.helpers.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
                               @Context Request request) {
        Patient patient = patientService.getPatientByEmail(patientEmail);
        if(patient != null) {
            PatientDto dto = PatientDto.fromPatient(patient, uriInfo);
            return CacheHelper.handleResponse(dto, patientCaching, "patient", request).build();
            // return Response.ok(dto).build();
        }
        return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response deletePatient(@PathParam("id") final String patientEmail) {
        patientService.deletePatient(patientEmail);
        return Response.noContent().build();
    }

    @PUT
    @Path("{id}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response updatePatient(@PathParam("id") final String patientEmail,
                                  PersonalInformationForm form) {
        Patient patient = patientService.getPatientByEmail(patientEmail);
        if(patient != null) {
            patientService.updatePatientProfile(patientEmail, SecurityHelper.processNewPassword(form.getNewPassword(),
                    passwordEncoder, userService, patientEmail), form.getFirstName(),
                    form.getLastName(), form.getPrepaid(), form.getPrepaidNumber());
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPatient(final SignUpForm form) throws DuplicateEntityException {
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        patientService.create(form.getId(), form.getPrepaid(),
                form.getPrepaidNumber(), form.getFirstName(),
                form.getLastName(), encodedPassword, form.getEmail());
        return Response.created(uriInfo.getAbsolutePathBuilder().path(form.getEmail()).build()).build();
    }

    @GET
    @Path("{id}/favorites")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response getPatientFavorites(@PathParam("id") String patientEmail,
                                        @QueryParam("page") @DefaultValue("0") Integer page,
                                        @Context Request request) throws EntityNotFoundException {
        page = (page < 0) ? 0 : page;

        Patient patient = patientService.getPatientByEmail(patientEmail);
        if(patient == null) throw new EntityNotFoundException("patient");
        List<DoctorDto> favorites = favoriteService.getPaginatedObjects(page, patient)
                .stream().map(f -> DoctorDto.fromDoctor(f.getDoctor(), uriInfo)).collect(Collectors.toList());
        int maxPage = favoriteService.maxAvailablePage(patient);
        return CacheHelper.handleResponse(favorites, doctorCaching,
                new GenericEntity<List<DoctorDto>>(favorites) {},
                "favorites", request)
                .header("Access-Control-Expose-Headers", "X-max-page")
                .header("X-max-page", maxPage).build();
        // return Response.ok(new GenericEntity<List<DoctorDto>>(favorites) {}).build();

    }

    @DELETE
    @Path("{id}/favorites")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response removeFromFavorites(@PathParam("id") final String patientEmail,
                                        @QueryParam("license") String doctorLicense) throws EntityNotFoundException {
        patientService.deleteFavorite(patientEmail, doctorLicense);
        return Response.noContent().build();
    }

    @POST
    @Path("{id}/favorites")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response addFavorite(@PathParam("id") final String patientEmail,
                                @QueryParam("license") String doctorLicense) throws EntityNotFoundException, FavouriteExistsException {
        patientService.addFavorite(patientEmail, doctorLicense);
        return Response.created(uriInfo.getAbsolutePath()).build();
    }

    /**
     * Returns true if the doctor is already a favorite of a patient
     * @param patientEmail
     * @param license
     * @return boolean
     * @throws EntityNotFoundException
     */
    @GET
    @Path("{id}/favorites/{license}")
    @Produces(value = { MediaType.APPLICATION_JSON })
    @PreAuthorize("hasPermission(#patientEmail, 'user')")
    public Response isDoctorFavorite (@PathParam("id") String patientEmail,
                                      @PathParam("license") String license,
                                      @Context Request request) throws EntityNotFoundException {
        Doctor doc = doctorService.getDoctorByLicense(license);
        if (doc == null) throw new EntityNotFoundException("doctor");
        Patient patient = patientService.getPatientByEmail(patientEmail);
        if(patient == null) throw new EntityNotFoundException("patient");
        if (favoriteService.isFavorite(doc, patient)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("not-favorite").build();

    }

}
