package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PrepaidToClinicDao;
import ar.edu.itba.paw.interfaces.service.ClinicService;
import ar.edu.itba.paw.interfaces.service.PrepaidService;
import ar.edu.itba.paw.interfaces.service.PrepaidToClinicService;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.PrepaidToClinic;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PrepaidToClinicServiceImpl implements PrepaidToClinicService {

    @Autowired
    private PrepaidToClinicDao prepaidToClinicDao;

    @Autowired
    private PrepaidService prepaidService;

    @Autowired
    private ClinicService clinicService;

    @Transactional
    @Override
    public PrepaidToClinic addPrepaidToClinic(Prepaid prepaid, Clinic clinic) {
        return prepaidToClinicDao.addPrepaidToClinic(prepaid, clinic);
    }

    @Override
    public boolean clinicHasPrepaid(String prepaidName, int clinicId) {
        Optional<Prepaid> prepaid = prepaidService.getPrepaidByName(prepaidName);
        Optional<Clinic> clinic = clinicService.getClinicById(clinicId);
        if (!prepaid.isPresent() || !clinic.isPresent()) return false;
        return prepaidToClinicDao.getPrepaidToClinic(prepaid.get(), clinic.get()).isPresent();
    }

    @Transactional
    @Override
    public void deletePrepaidFromClinic(Prepaid prepaid, Clinic clinic) throws EntityNotFoundException {
        PrepaidToClinic prepaidToClinic = prepaidToClinicDao.getPrepaidToClinic(prepaid, clinic)
                .orElseThrow(() -> new EntityNotFoundException("clinic-prepaid"));
        prepaidToClinicDao.deletePrepaidFromClinic(prepaidToClinic);
    }

    @Override
    public int maxAvailablePagePerClinic(Clinic clinic) {
        return prepaidToClinicDao.maxAvailablePagePerClinic(clinic);
    }

    @Override
    public List<Prepaid> getPrepaidForClinic(Clinic clinic, int page) {
        return prepaidToClinicDao.getPrepaidForClinic(clinic, page);
    }

    @Override
    public List<Prepaid> getPrepaidForClinic(Clinic clinic) {
        return prepaidToClinicDao.getPrepaidForClinic(clinic);
    }

    @Override
    public List<PrepaidToClinic> getPaginatedObjects(int page) {
        if(page < 0) {
            return Collections.emptyList();
        }
        return prepaidToClinicDao.getPaginatedObjects(page);
    }

    @Override
    public int maxAvailablePage() {
        return prepaidToClinicDao.maxAvailablePage();
    }
}
