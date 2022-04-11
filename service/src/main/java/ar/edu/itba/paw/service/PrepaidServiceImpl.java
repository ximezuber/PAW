package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.PrepaidDao;
import ar.edu.itba.paw.interfaces.service.PatientService;
import ar.edu.itba.paw.interfaces.service.PrepaidService;
import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class PrepaidServiceImpl implements PrepaidService {

    @Autowired
    private PrepaidDao prepaidDao;

    @Autowired
    private PatientService patientService;

    public List<Prepaid> getPrepaid() {
        return prepaidDao.getPrepaid();
    }

    @Override
    public Optional<Prepaid> getPrepaidByName(String prepaidName) {
        return prepaidDao.getPrepaidByName(prepaidName);
    }

    @Transactional
    @Override
    public Prepaid createPrepaid(String name) throws DuplicateEntityException {
        Optional<Prepaid> exists = getPrepaidByName(name);
        if (exists.isPresent()) throw new DuplicateEntityException("prepaid-exists");
        return prepaidDao.createPrepaid(name);
    }

    @Transactional
    @Override
    public void deletePrepaid(Prepaid prepaid) {
        List<Patient> patients = patientService.getPatientsByPrepaid(prepaid.getName());
        prepaidDao.deletePrepaid(prepaid);
        for(Patient patient : patients) {
            patientService.updatePatient(patient.getEmail(), null ,null);
        }
    }

    @Override
    public List<Prepaid> getPaginatedObjects(int page) {
        if(page < 0) {
            return Collections.emptyList();
        }
        return prepaidDao.getPaginatedObjects(page);
    }

    @Override
    public int maxAvailablePage() {
        return prepaidDao.maxAvailablePage();
    }
}
