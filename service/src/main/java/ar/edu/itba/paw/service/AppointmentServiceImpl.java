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
    private UserService userService;

    // TODO: Check if this is right (necessary) and if it should just be a private method
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
        DoctorClinic doctorClinic = doctorClinicService.getDoctorClinicWithSchedule(license, clinicId);

        User user = userService.findUserByEmail(userEmail);

        if(!today.isBefore(date)) {
            throw new DateInPastException();
        }
        if(isAppointment(license, userEmail, date)) {
            throw new AppointmentAlreadyScheduledException();
        }

        if (hasAppointment(doctor, date)) throw new HasAppointmentException("doctor");

        if (hasAppointment(user, date)) throw new HasAppointmentException("patient");


        for (Schedule schedule : doctorClinic.getSchedule()) {
            if (date.getDayOfWeek().getValue() == schedule.getDay() && date.getHour() == schedule.getHour()) {
                Locale locale = LocaleContextHolder.getLocale();
                final String language = locale.getLanguage();
                locale = Arrays.stream(Locale.getAvailableLocales())
                        .filter(loc -> language.equals(loc.getLanguage())).findFirst().orElse(Locale.ENGLISH);

                emailService.sendSimpleMail(
                        userEmail,
                        messageSource.getMessage("appointment.created.subject", null, locale),
                        messageSource.getMessage("appointment.created.text", null, locale)
                                + " " + dateString(date));
                return appointmentDao.createAppointment(doctorClinic, user, date);
            }
        }
        throw new OutOfScheduleException();
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
                        date.getMonthValue(), date.getDayOfMonth(), s.getHour(), 0),
                        s.getDoctorClinic(), null);
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
    public Optional<Appointment> getAppointment(String license, int year, int month, int day, int time)
            throws EntityNotFoundException {
        Doctor doctor = doctorService.getDoctorByLicense(license);
        if(doctor == null) throw new EntityNotFoundException("doctor");
        LocalDateTime date = createAppointmentCalendar(year, month, day, time);

        return appointmentDao.getAppointment(doctor, date);
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

    @Override
    public boolean isAppointment(String doctorLicense, String patientEmail, LocalDateTime date) {
        return appointmentDao.hasAppointment(doctorLicense, patientEmail, date);
    }

    @Transactional
    @Override
    public void cancelUserAppointment(String userEmail, String license, int year, int month, int day, int time)
            throws EntityNotFoundException {
        boolean cancelledByDoctor = userService.isDoctor(userEmail);
        Doctor doctor = doctorService.getDoctorByLicense(license);
        if(doctor == null) throw new EntityNotFoundException("doctor");

        cancelAppointment(doctor, year, month, day, time, cancelledByDoctor);
    }

    @Transactional
    @Override
    public int cancelAllAppointmentsOnSchedule(DoctorClinic doctorClinic, int day, int hour) {
        return appointmentDao.cancelAllAppointmentsOnSchedule(doctorClinic, day, hour);
    }


    private boolean hasAppointment(Doctor doctor, LocalDateTime date) {
        return appointmentDao.hasAppointment(doctor, date);
    }

    private boolean hasAppointment(User patient, LocalDateTime date) {
        return appointmentDao.hasAppointment(patient, date);
    }

    private List<Appointment> getAllDoctorsAppointments(Doctor doctor) {
        return appointmentDao.getAllDoctorsAppointments(doctor);
    }

    private void cancelAppointment(Doctor doctor, int year, int month, int day, int time, boolean cancelledByDoctor)
            throws EntityNotFoundException {

        LocalDateTime date = createAppointmentCalendar(year, month, day, time);

        Locale locale = LocaleContextHolder.getLocale();

        final String language = locale.getLanguage();
        locale = Arrays.stream(Locale.getAvailableLocales())
                .filter(loc -> language.equals(loc.getLanguage())).findFirst().orElse(Locale.ENGLISH);

        Optional<Appointment> appointment = appointmentDao.getAppointment(doctor, date);

        if(appointment.isPresent()) {
            User user = userService.findUserByEmail(appointment.get().getPatientUser().getEmail());
            if (cancelledByDoctor) {
                emailService.sendSimpleMail(
                        user.getEmail(),
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
            appointmentDao.cancelAppointment(appointment.get());
        } else {
            throw new EntityNotFoundException("appointment");
        }
    }

    private String dateString(LocalDateTime calendar) {
        return calendar.format(DateTimeFormatter.ofPattern("EEEE yyyy-MM-dd hh:mm:ss a"));
    }
}
