package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.PatientDao;
import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientDaoImpl implements PatientDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Patient create(String id, Prepaid prepaid, String prepaidNumber, User user) {
        if (prepaid != null) {
            prepaid = entityManager.merge(prepaid);
        }
        Patient patient = new Patient(id, prepaid, prepaidNumber, user);
        entityManager.persist(patient);
        return patient;
    }

    @Override
    public Optional<Patient> getPatientByEmail(String email) {
        Patient patient = entityManager.find(Patient.class, email);
        return Optional.ofNullable(patient);
    }

    @Override
    public void updatePatient(Patient patient, String id, Prepaid prepaid, String prepaidNumber) {
        Patient contextPatient = entityManager.merge(patient);
        contextPatient.setId(id);
        if (prepaid != null) {
            prepaid = entityManager.merge(prepaid);
            contextPatient.setPrepaid(prepaid);
            contextPatient.setPrepaidNumber(prepaidNumber);
        }
        else {
            contextPatient.setPrepaid(null);
            contextPatient.setPrepaidNumber(null);
        }
        entityManager.persist(contextPatient);

    }

    @Override
    public List<Patient> getPatientsByPrepaid(Prepaid prepaid) {
        final TypedQuery<Patient> query = entityManager.createQuery("FROM Patient AS pat WHERE pat.prepaid= :prepaid",
                Patient.class);
        query.setParameter("prepaid", prepaid.getName());
        return query.getResultList();
    }

    @Override
    public List<Patient> getPatientsById(String id) {
        final TypedQuery<Patient> query = entityManager.createQuery("FROM Patient AS pat WHERE pat.id= :id",
                Patient.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

}

