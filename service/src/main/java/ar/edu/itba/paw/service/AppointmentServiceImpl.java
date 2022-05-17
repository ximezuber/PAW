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
    public Appointment createAppointment(DoctorClinic doctorClinic, User patient,
                                         int year, int month, int day, int time)
            throws DateInPastException, AppointmentAlreadyScheduledException,
            OutOfScheduleException, HasAppointmentException {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime date = createAppointmentCalendar(year, month, day, time);

        if(!today.isBefore(date)) {
            throw new DateInPastException();
        }

        Optional<Appointment> existsDoctor = appointmentDao.getAppointment(doctorClinic.getDoctor(), date);
        Optional<Appointment> existsPatient = appointmentDao.getAppointment(patient, date);

        if(existsDoctor.isPresent() || existsPatient.isPresent()) {
            if (existsDoctor.isPresent() && existsPatient.isPresent()) {
                throw new AppointmentAlreadyScheduledException();
            } else if (existsDoctor.isPresent()){
                throw new HasAppointmentException("doctor");
            } else {
                throw new HasAppointmentException("patient");
            }
        }

        for (Schedule schedule : doctorClinic.getSchedule()) {
            if (date.getDayOfWeek().getValue() == schedule.getDay() && date.getHour() == schedule.getHour()) {
                Locale locale = LocaleContextHolder.getLocale();
                final String language = locale.getLanguage();
                locale = Arrays.stream(Locale.getAvailableLocales())
                        .filter(loc -> language.equals(loc.getLanguage())).findFirst().orElse(Locale.ENGLISH);

                emailService.sendSimpleMail(
                        patient.getEmail(),
                        messageSource.getMessage("appointment.created.subject", null, locale),
                        messageSource.getMessage("appointment.created.text", null, locale)
                                + " " + dateString(date));
                return appointmentDao.createAppointment(doctorClinic, patient, date);
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
                        date.getMonthValue(), date.getDayOfMonth(), s.getHour(), 0),
                        s.getDoctorClinic(), null);
                available.add(appointment);
            }
        }

        Set<Appointment> occupied = new HashSet<>(appointments);
        available.removeAll(occupied);

        return available.stream().sorted(Comparator.comparing(o -> o.getAppointmentKey().getDate())
        ).collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getPaginatedAppointments(User user, int page) throws EntityNotFoundException {
        Optional<Doctor> doctor = doctorService.getDoctorByEmail(user.getEmail());
        if(doctor.isPresent()) {
            return appointmentDao.getPaginatedAppointments(page, doctor.get());
        } else {
            Patient patient = patientService.getPatientByEmail(user.getEmail())
                    .orElseThrow(() -> new EntityNotFoundException("user"));
            return appointmentDao.getPaginatedAppointments(page, patient);
        }
    }

    @Override
    public Optional<Appointment> getAppointment(Doctor doctor, int year, int month, int day, int time) {
        LocalDateTime date = createAppointmentCalendar(year, month, day, time);
        return appointmentDao.getAppointment(doctor, date);
    }

    @Override
    public Optional<Appointment> getAppointment(User patient, int year, int month, int day, int time) {
        LocalDateTime date = createAppointmentCalendar(year, month, day, time);
        return appointmentDao.getAppointment(patient, date);
    }

    @Override
    public int getMaxAvailablePage(User user) throws EntityNotFoundException {
        Optional<Doctor> doctor = doctorService.getDoctorByEmail(user.getEmail());
        if(doctor.isPresent()) {
            return appointmentDao.getMaxAvailablePage(doctor.get());
        } else {
            Patient patient = patientService.getPatientByEmail(user.getEmail())
                    .orElseThrow(() -> new EntityNotFoundException("user"));
            return appointmentDao.getMaxAvailablePage(patient);
        }
    }

    @Override
    public Optional<Appointment> getAppointment(Doctor doctor, Patient patient, LocalDateTime date) {
        return appointmentDao.getAppointment(doctor, patient, date);
    }

    @Transactional
    @Override
    public void cancelUserAppointment(String userEmail, String license, int year, int month, int day, int time)
            throws EntityNotFoundException {
        boolean cancelledByDoctor = userService.isDoctor(userEmail);
        Doctor doctor = doctorService.getDoctorByLicense(license)
                .orElseThrow(() -> new EntityNotFoundException("doctor"));

        cancelAppointment(doctor, year, month, day, time, cancelledByDoctor);
    }

    @Transactional
    @Override
    public int cancelAllAppointmentsOnSchedule(DoctorClinic doctorClinic, int day, int hour) {
        return appointmentDao.cancelAllAppointmentsOnSchedule(doctorClinic, day, hour);
    }

    @Override
    public boolean isAppointment(String license, String patientEmail, LocalDateTime date) {
        Optional<Doctor> doctor = doctorService.getDoctorByLicense(license);
        Optional<Patient> patient = patientService.getPatientByEmail(patientEmail);

        if(doctor.isPresent() && patient.isPresent()) {
            return getAppointment(doctor.get(), patient.get(), date).isPresent();
        }

        return false;
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
            User user = appointment.get().getPatientUser();
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
