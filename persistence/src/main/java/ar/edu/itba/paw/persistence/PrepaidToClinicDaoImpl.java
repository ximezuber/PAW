package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.PrepaidToClinicDao;
import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.PrepaidToClinic;
import ar.edu.itba.paw.model.PrepaidToClinicKey;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PrepaidToClinicDaoImpl implements PrepaidToClinicDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final static int MAX_PREPAID_TO_CLINICS_PER_PAGE = 12;


    @Override
    public List<PrepaidToClinic> getPrepaidToClinics(){
        TypedQuery<PrepaidToClinic> query = entityManager.createQuery("FROM PrepaidToClinic AS p ORDER BY " +
                        "p.prepaid.name, p.clinic.name, clinic.location.name",
                PrepaidToClinic.class);
        return query.getResultList();
    }

    @Override
    public List<Prepaid> getPrepaidForClinic(Clinic clinic, int page) {
        TypedQuery<PrepaidToClinic> query = entityManager
                .createQuery("FROM PrepaidToClinic AS cp WHERE cp.clinic.id = :id", PrepaidToClinic.class);
        query.setParameter("id", clinic.getId());
        return query.setFirstResult(page * MAX_PREPAID_TO_CLINICS_PER_PAGE)
                .setMaxResults(MAX_PREPAID_TO_CLINICS_PER_PAGE)
                .getResultList()
                .stream().map(PrepaidToClinic::getPrepaid).collect(Collectors.toList());
    }

    @Override
    public List<Prepaid> getPrepaidForClinic(Clinic clinic) {
        TypedQuery<PrepaidToClinic> query = entityManager
                .createQuery("FROM PrepaidToClinic AS cp WHERE cp.clinic.id = ?1", PrepaidToClinic.class);
        query.setParameter(1, clinic.getId());
        return query.getResultList()
                .stream().map(PrepaidToClinic::getPrepaid).collect(Collectors.toList());
    }

    @Override
    public List<PrepaidToClinic> getPaginatedObjects(int page) {
        TypedQuery<PrepaidToClinic> query = entityManager.createQuery("FROM PrepaidToClinic AS p " +
                        "ORDER BY p.prepaid.name, p.clinic.name, clinic.location.name",
                PrepaidToClinic.class);
        return query
                .setFirstResult(page * MAX_PREPAID_TO_CLINICS_PER_PAGE)
                .setMaxResults(MAX_PREPAID_TO_CLINICS_PER_PAGE)
                .getResultList();
    }

    @Override
    public int maxAvailablePage() {
        return (int) (Math.ceil(( ((double) getPrepaidToClinics().size()) / (double) MAX_PREPAID_TO_CLINICS_PER_PAGE)));
    }

    @Override
    public int maxAvailablePagePerClinic(Clinic clinic) {
        return (int) (Math.ceil(( ((double) getPrepaidForClinic(clinic).size()) / (double) MAX_PREPAID_TO_CLINICS_PER_PAGE)));
    }

    @Override
    public PrepaidToClinic addPrepaidToClinic(Prepaid prepaid, Clinic clinic){
        PrepaidToClinic prepaidToClinic = new PrepaidToClinic(clinic, prepaid);
        entityManager.persist(prepaidToClinic);
        return prepaidToClinic;
    }

    @Override
    public Optional<PrepaidToClinic> getPrepaidToClinic(Prepaid prepaid, Clinic clinic) {
        PrepaidToClinic prepaidToClinic = entityManager
                .find(PrepaidToClinic.class, new PrepaidToClinicKey(prepaid.getName(), clinic.getId()));
        return Optional.ofNullable(prepaidToClinic);
    }

    @Override
    public void deletePrepaidFromClinic(PrepaidToClinic prepaidToClinic) {
      PrepaidToClinic context = entityManager.merge(prepaidToClinic);
      entityManager.remove(context);
    }
}
