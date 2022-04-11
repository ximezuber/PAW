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
    public PrepaidToClinic addPrepaidToClinic(String prepaidName, int clinicId) throws EntityNotFoundException {
        Prepaid prepaid = prepaidService.getPrepaidByName(prepaidName)
                .orElseThrow(() -> new EntityNotFoundException("prepaid"));
        Clinic clinic = clinicService.getClinicById(clinicId).orElseThrow(() -> new EntityNotFoundException("clinic"));
        return prepaidToClinicDao.addPrepaidToClinic(prepaid, clinic);
    }

    @Override
    public boolean clinicHasPrepaid(String prepaid, int clinic) {
        return prepaidToClinicDao.clinicHasPrepaid(prepaid,clinic);
    }

    @Transactional
    @Override
    public long deletePrepaidFromClinic(String prepaidName, int clinicId) throws EntityNotFoundException {
        Prepaid prepaid = prepaidService.getPrepaidByName(prepaidName)
                .orElseThrow(() -> new EntityNotFoundException("prepaid"));
        Clinic clinic = clinicService.getClinicById(clinicId).orElseThrow(() -> new EntityNotFoundException("clinic"));
        if (!clinicHasPrepaid(prepaidName, clinicId)) throw new EntityNotFoundException("clinic-prepaid");
        return prepaidToClinicDao.deletePrepaidFromClinic(prepaidName, clinicId);
    }

    @Override
    public int maxAvailablePagePerClinic(int id) {
        return prepaidToClinicDao.maxAvailablePagePerClinic(id);
    }

    @Override
    public List<Prepaid> getPrepaidForClinic(int clinic, int page) {
        return prepaidToClinicDao.getPrepaidsForClinic(clinic, page);
    }

    @Override
    public List<Prepaid> getPrepaidForClinic(int clinic) {
        return prepaidToClinicDao.getPrepaidsForClinic(clinic);
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
