package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.PatientDao;
import ar.edu.itba.paw.model.Patient;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@Repository
public class PatientDaoImpl implements PatientDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Patient create(String id, String prepaid, String prepaidNumber, User user) {

        Patient patient = new Patient(id, prepaid, prepaidNumber, user);
        entityManager.persist(patient);
        return patient;
    }

    @Override
    public Patient getPatientByEmail(String email) {
        return entityManager.find(Patient.class, email);
    }

    @Override
    public void updatePatient(String email, Map<String, String> args) {
        Query query;
        if (args.containsKey("prepaid")) {
            query = entityManager.createQuery("update Patient as patient set patient.prepaid = :prepaid where patient.user.email = :email");
            query.setParameter("prepaid", args.get("prepaid"));
            query.setParameter("email",email);
            query.executeUpdate();
        }
        if (args.containsKey("prepaidNumber")) {
            query = entityManager.createQuery("update Patient as patient set prepaidNumber = :prepaidNumber where patient.user.email = :email");
            query.setParameter("prepaidNumber", args.get("prepaidNumber"));
            query.setParameter("email",email);
            query.executeUpdate();
        }
    }

    @Override
    public List<Patient> getPatientsByPrepaid(String prepaid) {
        final TypedQuery<Patient> query = entityManager.createQuery("from Patient as pat where pat.prepaid= :prepaid",Patient.class);

        query.setParameter("prepaid",prepaid);
        final List<Patient> list = query.getResultList();
        return list;
    }

    @Override
    public List<Patient> getPatientsById(String id) {
        final TypedQuery<Patient> query = entityManager.createQuery("from Patient as pat where pat.id= :id",Patient.class);

        query.setParameter("id",id);
        final List<Patient> list = query.getResultList();
        return list;
    }

}

