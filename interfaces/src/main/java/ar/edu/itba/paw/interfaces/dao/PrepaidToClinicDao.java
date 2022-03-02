package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.PrepaidToClinic;

import java.util.List;

public interface PrepaidToClinicDao extends PaginationDao<PrepaidToClinic> {
    List<PrepaidToClinic> getPrepaidToClinics();

    PrepaidToClinic addPrepaidToClinic(Prepaid prepaid, Clinic clinic);

    boolean clinicHasPrepaid(String prepaid,int clinic);

    List<Prepaid> getPrepaidsForClinic(int clinic, int page);

    List<Prepaid> getPrepaidsForClinic(int clinic);

    long deletePrepaidFromClinic(String prepaid, int clinic);

    int maxAvailablePagePerClinic(int id);
}
