package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DoctorClinicDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class DoctorClinicServiceImpl implements DoctorClinicService {

    @Autowired
    private DoctorClinicDao doctorClinicDao;

    @Autowired
    private ScheduleService scheduleService;

    @Transactional
    @Override
    public DoctorClinic createDoctorClinic(Doctor doctor, Clinic clinic, int consultPrice)
            throws DuplicateEntityException {
        Optional<DoctorClinic> dc = doctorClinicDao.getDoctorClinic(doctor, clinic);
        if (dc.isPresent()) throw new DuplicateEntityException("doctor-clinic-exists");

        return doctorClinicDao.createDoctorClinic(doctor, clinic, consultPrice);
    }

    @Transactional
    @Override
    public void deleteDoctorClinic(DoctorClinic doctorClinic) {
        doctorClinicDao.deleteDoctorClinic(doctorClinic);
    }

    @Override
    public List<DoctorClinic> getDoctorsSubscribedClinics(Doctor doctor) {
        return doctorClinicDao.getDoctorsSubscribedClinics(doctor);
    }

    @Override
    public Optional<DoctorClinic> getDoctorClinic(Doctor doctor, Clinic clinic) {
        return doctorClinicDao.getDoctorClinic(doctor, clinic);
    }

    @Override
    public Optional<DoctorClinic> getDoctorClinicWithSchedule(Doctor doctor, Clinic clinic) {
        Optional<DoctorClinic> doctorClinic = doctorClinicDao.getDoctorClinic(doctor, clinic);
        if(doctorClinic.isPresent()) {
            DoctorClinic dc = doctorClinic.get();
            List<Schedule> schedules = scheduleService.getDoctorClinicSchedule(dc);
            dc.setSchedule(schedules);
            doctorClinic = Optional.of(dc);
        }

        return doctorClinic;
    }

    @Override
    public List<Doctor> getPaginatedFilteredDoctorClinics(Location location, Specialty specialty,
                                                                String firstName, String lastName,
                                                                Prepaid prepaid, int consultPrice, int page) {

        return doctorClinicDao.getFilteredDoctorInClinicsPaginated(location, specialty, firstName,
                lastName, prepaid, consultPrice, page);
    }

    @Override
    public List<DoctorClinic> getPaginatedDoctorsClinics(Doctor doctor, int page) {
        return doctorClinicDao.getDoctorClinicPaginatedByList(doctor, page);
    }

    @Transactional
    @Override
    public void editPrice(DoctorClinic dc, int price) {
        doctorClinicDao.editPrice(dc, price);
    }

    @Override
    public int maxAvailableFilteredDoctorClinicPage(Location location, Specialty specialty, String firstName,
                                                    String lastName, Prepaid prepaid, int consultPrice) {
        return doctorClinicDao.maxAvailableFilteredDoctorClinicPage(location, specialty, firstName, lastName,
                prepaid, consultPrice);
    }

    @Override
    public int maxAvailablePage(Doctor doctor) {
        return doctorClinicDao.maxPageAvailable(doctor);
    }
}
