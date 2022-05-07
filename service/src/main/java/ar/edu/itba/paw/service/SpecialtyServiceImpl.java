package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.SpecialtyDao;
import ar.edu.itba.paw.interfaces.service.DoctorService;
import ar.edu.itba.paw.interfaces.service.SpecialtyService;
import ar.edu.itba.paw.model.Doctor;
import ar.edu.itba.paw.model.Specialty;
import ar.edu.itba.paw.model.exceptions.DuplicateEntityException;
import ar.edu.itba.paw.model.exceptions.EntityDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class SpecialtyServiceImpl implements SpecialtyService {

    @Autowired
    private SpecialtyDao specialtyDao;

    @Autowired
    private DoctorService doctorService;

    @Transactional
    @Override
    public Specialty createSpecialty(String name) throws DuplicateEntityException {
        Optional<Specialty> exists = getSpecialtyByName(name);
        if (exists.isPresent()) throw new DuplicateEntityException("specialty-exists");
        return specialtyDao.createSpecialty(name);
    }

    @Override
    public List<Specialty> getSpecialties(){
        return specialtyDao.getSpecialties();
    }

    @Override
    public List<Specialty> getPaginatedObjects(int page) {
        if(page < 0) {
            return Collections.emptyList();
        }
        return specialtyDao.getPaginatedObjects(page);
    }

    @Override
    public int maxAvailablePage() {
        return specialtyDao.maxAvailablePage();
    }

    @Override
    public Optional<Specialty> getSpecialtyByName(String specialtyName) {
        return specialtyDao.getSpecialtyByName(specialtyName);
    }

    @Transactional
    @Override
    public void deleteSpecialty(Specialty specialty) throws EntityDependencyException {
        List<Doctor> doctorsWithSpecialty = doctorService.getDoctorBySpecialty(specialty);
        if (!doctorsWithSpecialty.isEmpty()) throw new EntityDependencyException("doctors");
        specialtyDao.deleteSpecialty(specialty);
    }
}
