package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.ScheduleDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.DoctorClinic;
import ar.edu.itba.paw.model.Schedule;
import ar.edu.itba.paw.model.exceptions.ConflictException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import ar.edu.itba.paw.model.exceptions.OutOfRangeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorClinicService doctorClinicService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ClinicService clinicService;

    @Transactional
    @Override
    public Schedule createSchedule(int hour, int day, String license, int clinicId) throws ConflictException {

        DoctorClinic doctorClinic = doctorClinicService.getDoctorClinic(license, clinicId);

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
    public boolean doctorHasScheduleInClinic(DoctorClinic doctorClinic, int day, int hour) {
        List<Schedule> schedules = this.getDoctorClinicSchedule(doctorClinic);
        if(schedules != null) {
            return schedules.contains(new Schedule(day, hour, doctorClinic));
        }else{
            return false;
        }
    }

    @Transactional
    @Override
    public void deleteSchedule(int hour, int day, String license, int clinicId)
            throws OutOfRangeException, EntityNotFoundException {
        if(hour > 0 && hour < 24 && day > 0 && day < 8) {
            DoctorClinic doctorClinic = doctorClinicService.getDoctorClinicWithSchedule(
                    license,
                    clinicId);

            if(doctorHasSchedule(doctorClinic.getDoctor(), day, hour)) {
                if(doctorHasScheduleInClinic(doctorClinic, day, hour)) {
                    scheduleDao.deleteSchedule(hour, day, doctorClinic);
                    appointmentService.cancelAllAppointmentsOnSchedule(doctorClinic, day, hour);
                } else {
                    throw new EntityNotFoundException("schedule-clinic");
                }

            } else {
                throw new EntityNotFoundException("schedule");
            }
        } else {
           throw new OutOfRangeException("time");
        }

    }

    private boolean doctorHasSchedule(Doctor doctor, int day, int hour) {
        return scheduleDao.doctorHasSchedule(doctor,day,hour);
    }
}
