package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.PrepaidToClinicDao;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.PrepaidToClinic;
import ar.edu.itba.paw.model.PrepaidToClinicKey;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrepaidToClinicDaoImpl implements PrepaidToClinicDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final static int MAX_PREPAID_TO_CLINICS_PER_PAGE = 12;


    @Override
    public List<PrepaidToClinic> getPrepaidToClinics(){
        TypedQuery<PrepaidToClinic> query = entityManager.createQuery("from PrepaidToClinic as p ORDER BY " +
                        "p.prepaid.name, p.clinic.name, clinic.location.name",
                PrepaidToClinic.class);
        return query.getResultList();
    }

    @Override
    public List<Prepaid> getPrepaidsForClinic(int clinic, int page) {
        TypedQuery<PrepaidToClinic> query = entityManager
                .createQuery("from PrepaidToClinic as cp where cp.clinic.id = :id", PrepaidToClinic.class);
        query.setParameter("id", clinic);
        return query.setFirstResult(page * MAX_PREPAID_TO_CLINICS_PER_PAGE)
                .setMaxResults(MAX_PREPAID_TO_CLINICS_PER_PAGE)
                .getResultList()
                .stream().map(PrepaidToClinic::getPrepaid).collect(Collectors.toList());
    }

    @Override
    public List<Prepaid> getPrepaidsForClinic(int clinic) {
        TypedQuery<PrepaidToClinic> query = entityManager
                .createQuery("from PrepaidToClinic as cp where cp.clinic.id = ?1", PrepaidToClinic.class);
        query.setParameter(1, clinic);
        return query.getResultList()
                .stream().map(PrepaidToClinic::getPrepaid).collect(Collectors.toList());
    }

    @Override
    public List<PrepaidToClinic> getPaginatedObjects(int page){
        TypedQuery<PrepaidToClinic> query = entityManager.createQuery("from PrepaidToClinic as p " +
                        "ORDER BY p.prepaid.name, p.clinic.name, clinic.location.name",
                PrepaidToClinic.class);
        return query
                .setFirstResult(page * MAX_PREPAID_TO_CLINICS_PER_PAGE)
                .setMaxResults(MAX_PREPAID_TO_CLINICS_PER_PAGE)
                .getResultList();
    }

    @Override
    public int maxAvailablePage() {
        return (int) (Math.ceil(( ((double)getPrepaidToClinics().size()) / (double)MAX_PREPAID_TO_CLINICS_PER_PAGE)));
    }

    @Override
    public int maxAvailablePagePerClinic(int id) {
        return (int) (Math.ceil(( ((double)getPrepaidsForClinic(id).size()) / (double)MAX_PREPAID_TO_CLINICS_PER_PAGE)));
    }

    @Override
    public PrepaidToClinic addPrepaidToClinic(Prepaid prepaid, Clinic clinic){
        PrepaidToClinic prepaidToClinic = new PrepaidToClinic(clinic, prepaid);
        entityManager.persist(prepaidToClinic);
        return prepaidToClinic;
    }

    @Override
    public boolean clinicHasPrepaid(String prepaid, int clinic){
        TypedQuery<PrepaidToClinic> query = entityManager
                .createQuery("from PrepaidToClinic as p where p.clinic.id = :clinic and p.prepaid.name = :prepaid",
                        PrepaidToClinic.class);
        query.setParameter("clinic",clinic);
        query.setParameter("prepaid",prepaid);
        List<PrepaidToClinic> list = query.getResultList();
        return !list.isEmpty();
    }

    @Override
    public long deletePrepaidFromClinic(String prepaid, int clinic) {
        Query query = entityManager.createQuery("delete from PrepaidToClinic as p " +
                "where p.prepaid.name = :prepaid and p.clinic.id = :clinic");
        query.setParameter("clinic",clinic);
        query.setParameter("prepaid",prepaid);
        return query.executeUpdate();
    }
}
