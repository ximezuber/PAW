package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.AppointmentDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private DoctorClinicService doctorClinicService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService userService;

    @Override
    public LocalDateTime createAppointmentCalendar(int year, int month, int day, int time) {
        return LocalDateTime.of(year, Month.of(month), day, time, 0);

    }

    @Transactional
    @Override
    public Appointment createAppointment(String license, int clinicId, String userEmail, int year, int month, int day, int time)
            throws DateInPastException, AppointmentAlreadyScheduledException, OutOfScheduleException, HasAppointmentException {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime date = createAppointmentCalendar(year, month, day, time);

        Doctor doctor = doctorService.getDoctorByLicense(license);
        Clinic clinic = clinicService.getClinicById(clinicId);
        DoctorClinic doctorClinic = doctorClinicService.getDoctorClinicFromDoctorAndClinic(doctor, clinic);

        User user = userService.findUserByEmail(userEmail);

        if(!today.isBefore(date)) {
            throw new DateInPastException();
        }
        if(hasAppointment(license, userEmail, date)) {
            throw new AppointmentAlreadyScheduledException();
        }

        if (hasAppointment(doctor, date)) throw new HasAppointmentException("doctor");

        if (hasAppointment(user, date)) throw new HasAppointmentException("patient");


        for (Schedule schedule : doctorClinic.getSchedule()) {
            if (date.getDayOfWeek().getValue() == schedule.getDay() && date.getHour() == schedule.getHour()) {
                Locale locale = LocaleContextHolder.getLocale();
                final String language = locale.getLanguage();
                locale = Arrays.stream(Locale.getAvailableLocales()).filter(loc -> language.equals(loc.getLanguage())).findFirst().orElse(Locale.ENGLISH);

                emailService.sendSimpleMail(
                        userEmail,
                        messageSource.getMessage("appointment.created.subject", null, locale),
                        messageSource.getMessage("appointment.created.text", null, locale) + " " + dateString(date));
                return appointmentDao.createAppointment(doctorClinic, user, date);
            }
        }

        throw new OutOfScheduleException();
    }

    @Override
    public List<Appointment> getDoctorsAppointments(DoctorClinic doctorClinic) {
        return appointmentDao.getDoctorsAppointments(doctorClinic);
    }

    @Override
    public List<Appointment> getPatientsAppointments(User patient) {
        return appointmentDao.getPatientsAppointments(patient);
    }

    @Override
    public List<Appointment> getPatientsAppointments(User patient, int clinicId) {
        return appointmentDao.getPatientsAppointments(patient, clinicId);
    }

    @Transactional
    @Override
    public void cancelAppointment(String license, int clinicId, int year, int month, int day, int time, boolean cancelledByDoctor)
            throws EntityNotFoundException, RequestEntityNotFoundException {
        DoctorClinic dc = doctorClinicService.getDoctorInClinic(license, clinicId);
        if(dc == null) throw new RequestEntityNotFoundException("doctor-clinic");

        LocalDateTime appointmentDate = createAppointmentCalendar(year, month, day, time);
        Appointment appointment = hasAppointment(dc, appointmentDate);

        if(appointment == null) throw new EntityNotFoundException("appointment");

        String patientEmail = appointment.getPatientUser().getEmail();
        cancelAppointment(license, clinicId, patientEmail, year, month, day, time, cancelledByDoctor);

    }

    @Transactional
    @Override
    public void cancelUserAppointment(String userEmail, String license, int clinicId, int year, int month, int day, int time)
            throws EntityNotFoundException, RequestEntityNotFoundException {
        boolean canceledByDoctor = userService.isDoctor(userEmail);
        cancelAppointment(license, clinicId, year, month, day, time, canceledByDoctor);
    }

    @Transactional
    @Override
    public void cancelAppointment(String license, int clinicId, String userEmail, int year, int month, int day, int time, boolean cancelledByDoctor) {

        LocalDateTime date = createAppointmentCalendar(year, month, day, time);

        Doctor doctor = doctorService.getDoctorByLicense(license);
        Clinic clinic = clinicService.getClinicById(clinicId);
        DoctorClinic doctorClinic = doctorClinicService.getDoctorClinicFromDoctorAndClinic(doctor, clinic);

        User user = userService.findUserByEmail(userEmail);

        Locale locale = LocaleContextHolder.getLocale();

        final String language = locale.getLanguage();
        locale = Arrays.stream(Locale.getAvailableLocales()).filter(loc -> language.equals(loc.getLanguage())).findFirst().orElse(Locale.ENGLISH);

        if(hasAppointment(license , userEmail ,date)) {
            if (cancelledByDoctor) {
                emailService.sendSimpleMail(
                        userEmail,
                        messageSource.getMessage("appointment.cancelled.subject", null, locale),
                        messageSource.getMessage(
                                "appointment.cancelled.by.doctor.text", null, locale) +
                                " " + dateString(date));
            } else {
                emailService.sendSimpleMail(
                        doctor.getEmail(),
                        messageSource.getMessage("appointment.cancelled.subject", null, locale),
                        messageSource.getMessage(
                                "appointment.cancelled.by.patient.text", null, locale) +
                                " " + user.getFirstName() + " " + user.getLastName() +
                                " " + dateString(date));
            }
            appointmentDao.cancelAppointment(doctorClinic, user, date);
        }
    }

    @Override
    public List<Appointment> getUserAppointments(User user) {
        if(userService.isDoctor(user.getEmail())) {
            Doctor doctor = doctorService.getDoctorByEmail(user.getEmail());
            return getAllDoctorsAppointments(doctor);
        } else {
            return getPatientsAppointments(user);
        }
    }

    @Override
    public List<Appointment> getUserAppointmentsForClinic(User user, Clinic clinic) {
        if(userService.isDoctor(user.getEmail())) {
            Doctor doctor = doctorService.getDoctorByEmail(user.getEmail());
            DoctorClinic doctorClinic = doctorClinicService
                    .getDoctorInClinic(doctor.getLicense(), clinic.getId());
            return getDoctorsAppointments(doctorClinic);
        } else {
            return getPatientsAppointments(user, clinic.getId());
        }
    }

    @Override
    public Appointment hasAppointment(DoctorClinic doctorClinic, LocalDateTime date) {
        return appointmentDao.hasAppointment(doctorClinic, date);
    }

    @Override
    public boolean hasAppointment(String doctorLicense, String patientEmail, LocalDateTime date) {
        return appointmentDao.hasAppointment(doctorLicense,patientEmail,date);
    }

    @Override
    public boolean hasAppointment(Doctor doctor, LocalDateTime date) {
        return appointmentDao.hasAppointment(doctor, date);
    }

    @Override
    public boolean hasAppointment(User patient, LocalDateTime date) {
        return appointmentDao.hasAppointment(patient, date);
    }

    @Override
    public List<Appointment> getAllDoctorsAppointments(Doctor doctor) {
        return appointmentDao.getAllDoctorsAppointments(doctor);
    }

    @Override
    public List<Appointment> getDoctorAppointmentsWithinWeek(Doctor doctor, LocalDate beginning, LocalDate end){
        return appointmentDao.getDoctorAppointmentsWithinWeek(doctor, beginning, end);
    }

    @Transactional
    @Override
    public void cancelAllAppointmentsOnSchedule(DoctorClinic doctorClinic, int day, int hour) {
        appointmentDao.cancelAllAppointmentsOnSchedule(doctorClinic, day, hour);
    }

    @Override
    public List<Appointment> getDoctorsAvailableAppointments(Doctor doctor) {
        List<Appointment> appointments = getAllDoctorsAppointments(doctor);
        List<Schedule> schedule = scheduleService.getDoctorSchedule(doctor);

        Set<Appointment> available = new HashSet<>();
        LocalDateTime today = LocalDateTime.now();

        for (Schedule s: schedule) {
            int day = today.getDayOfWeek().getValue();
            if (day < s.getDay()) {
                LocalDateTime date = today.plusDays(s.getDay()-day);
                Appointment appointment = new Appointment(LocalDateTime.of(date.getYear(),
                        date.getMonthValue(), date.getDayOfMonth(), s.getHour(), 0), s.getDoctorClinic(), null);
                available.add(appointment);
            }
        }

        for (int week = 1; week < 10; week++) {
            for (Schedule s: schedule) {
                int day = today.getDayOfWeek().getValue();
                LocalDateTime date = today.plusDays(s.getDay() - day + week*7);
                Appointment appointment = new Appointment(LocalDateTime.of(date.getYear(),
                        date.getMonthValue(), date.getDayOfMonth(), s.getHour(), 0), s.getDoctorClinic(), null);
                available.add(appointment);
            }
        }

        Set<Appointment> occupied = new HashSet<>(appointments);
        available.removeAll(occupied);

        return available.stream().sorted(Comparator.comparing(o -> o.getAppointmentKey().getDate())
        ).collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getPaginatedAppointments(User user, int page) {
        if(userService.isDoctor(user.getEmail())) {
            Doctor doctor = doctorService.getDoctorByEmail(user.getEmail());
            return appointmentDao.getPaginatedAppointments(page, doctor);
        } else {
            Patient patient = patientService.getPatientByEmail(user.getEmail());
            return appointmentDao.getPaginatedAppointments(page, patient);
        }
    }

    @Override
    public int getMaxAvailablePage(User user) {
        if(userService.isDoctor(user.getEmail())) {
            Doctor doctor = doctorService.getDoctorByEmail(user.getEmail());
            return appointmentDao.getMaxAvailablePage(doctor);
        } else {
            Patient patient = patientService.getPatientByEmail(user.getEmail());
            return appointmentDao.getMaxAvailablePage(patient);
        }
    }

    private String dateString(LocalDateTime calendar) {
        return calendar.format(DateTimeFormatter.ofPattern("EEEE yyyy-MM-dd hh:mm:ss a"));
    }
}
