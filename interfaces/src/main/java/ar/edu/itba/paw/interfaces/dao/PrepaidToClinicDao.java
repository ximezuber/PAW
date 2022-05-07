package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.PrepaidToClinic;

import java.util.List;
import java.util.Optional;

public interface PrepaidToClinicDao extends PaginationDao<PrepaidToClinic> {
    List<PrepaidToClinic> getPrepaidToClinics();

    PrepaidToClinic addPrepaidToClinic(Prepaid prepaid, Clinic clinic);

    Optional<PrepaidToClinic> getPrepaidToClinic(Prepaid prepaid, Clinic clinic);

    List<Prepaid> getPrepaidForClinic(Clinic clinic, int page);

    List<Prepaid> getPrepaidForClinic(Clinic clinic);

    void deletePrepaidFromClinic(PrepaidToClinic prepaidToClinic);

    int maxAvailablePagePerClinic(Clinic clinic);
}
