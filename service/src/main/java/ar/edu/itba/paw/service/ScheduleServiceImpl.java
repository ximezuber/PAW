package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ScheduleDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.DoctorClinic;
import ar.edu.itba.paw.model.Schedule;
import ar.edu.itba.paw.model.exceptions.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private AppointmentService appointmentService;

    @Transactional
    @Override
    public Schedule createSchedule(int hour, int day, DoctorClinic doctorClinic) throws ConflictException {
        if(!doctorHasSchedule(doctorClinic.getDoctor(), day, hour)) {
            return scheduleDao.createSchedule(day, hour, doctorClinic);
        } else {
            throw new ConflictException("schedule-exists");
        }
    }

    @Override
    public List<Schedule> getDoctorClinicSchedule(DoctorClinic doctorClinic) {
        return scheduleDao.getDoctorClinicSchedule(doctorClinic);
    }

    @Override
    public List<Schedule> getDoctorSchedule(Doctor doctor) {
        return scheduleDao.getDoctorsSchedule(doctor);
    }

    @Override
    public Optional<Schedule> getDoctorsClinicSchedule(DoctorClinic doctorClinic, int day, int hour) {
        return scheduleDao.getDoctorScheduledHour(doctorClinic.getDoctor(), day, hour);
    }

    @Transactional
    @Override
    public void deleteSchedule(Schedule schedule) {
        scheduleDao.deleteSchedule(schedule);
        appointmentService.cancelAllAppointmentsOnSchedule(schedule.getDoctorClinic(),
                schedule.getDay(), schedule.getHour());

    }

    private boolean doctorHasSchedule(Doctor doctor, int day, int hour) {
        return scheduleDao.getDoctorScheduledHour(doctor,day,hour).isPresent();
    }
}
