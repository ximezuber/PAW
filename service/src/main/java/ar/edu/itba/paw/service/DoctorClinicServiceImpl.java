package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.DoctorClinicDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
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

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ClinicService clinicService;

    @Transactional
    @Override
    public DoctorClinic createDoctorClinic(String email, int clinicId, int consultPrice)
            throws EntityNotFoundException, DuplicateEntityException {
        Doctor doctor = doctorService.getDoctorByEmail(email);
        Clinic clinic = clinicService.getClinicById(clinicId);
        if (doctor == null) throw new EntityNotFoundException("doctor");
        if (clinic == null) throw new EntityNotFoundException("clinic");
        DoctorClinic dc = getDoctorClinic(doctor.getLicense(), clinicId);
        if (dc != null) throw new DuplicateEntityException("doctor-clinic-exists");

        return doctorClinicDao.createDoctorClinic(doctor, clinic, consultPrice);
    }

    @Transactional
    @Override
    public long deleteDoctorClinic(String license, int clinicid) throws EntityNotFoundException {
        Doctor doc = doctorService.getDoctorByLicense(license);
        if (doc == null) throw new EntityNotFoundException("doctor");
        Clinic clinic = clinicService.getClinicById(clinicid);
        if (clinic == null) throw new EntityNotFoundException("clinic");
        return doctorClinicDao.deleteDoctorClinic(license, clinicid);
    }

    @Override
    public List<DoctorClinic> getDoctorsSubscribedClinics(Doctor doctor) {
        List<DoctorClinic> list = doctorClinicDao.getDoctorsSubscribedClinics(doctor);
        return list;
    }

    @Override
    public DoctorClinic getDoctorClinic(String license, int clinic) {
        return doctorClinicDao.getDoctorClinic(license, clinic);
    }

    @Override
    public DoctorClinic getDoctorClinicWithSchedule(String doctor, int clinic) {
        DoctorClinic doctorClinic = doctorClinicDao.getDoctorClinic(doctor, clinic);
        if(doctorClinic != null) {
            List<Schedule> schedules = scheduleService.getDoctorClinicSchedule(doctorClinic);
            doctorClinic.setSchedule(schedules);
        }

        return doctorClinic;
    }

    @Override
    public List<DoctorClinic> getFilteredDoctorClinics(Location location, Specialty specialty,
                                   String firstName, String lastName,
                                   Prepaid prepaid, int consultPrice) {

        List<DoctorClinic> doctorClinics = doctorClinicDao.getFilteredDoctors(location, specialty, firstName,
                lastName, prepaid, consultPrice);
        setSchedule(doctorClinics);
        return doctorClinics;
    }

    @Override
    public List<DoctorClinic> getPaginatedDoctorsClinics(Doctor doctor, int page) {
        return doctorClinicDao.getDoctorClinicPaginatedByList(doctor, page);
    }

    @Transactional
    @Override
    public void editPrice(String license, int clinicId, int price) throws EntityNotFoundException {
        DoctorClinic dc = getDoctorClinic(license, clinicId);
        if (dc == null) throw new EntityNotFoundException("doctor-clinic");
        doctorClinicDao.editPrice(dc, price);
    }

    @Override
    public int maxAvailablePage() {
        return doctorClinicDao.maxPageAvailable();
    }

    private void setSchedule(List<DoctorClinic> list) {
        if(list != null) {
            for (DoctorClinic doctorClinic: list) {
                List<Schedule> schedules = scheduleService.getDoctorClinicSchedule(doctorClinic);
                doctorClinic.setSchedule(schedules);
            }
        }
    }
}
