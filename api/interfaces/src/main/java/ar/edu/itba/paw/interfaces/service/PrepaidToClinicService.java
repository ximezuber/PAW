package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.PrepaidToClinic;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;

import java.util.List;

public interface PrepaidToClinicService extends PaginationService<PrepaidToClinic> {
    List<PrepaidToClinic> getPrepaidToClinics();

    PrepaidToClinic addPrepaidToClinic(String prepaid, int clinic) throws EntityNotFoundException;

    boolean clinicHasPrepaid(String prepaid,int clinic);

    List<Prepaid> getPrepaidsForClinic(int clinic, int page);

    List<Prepaid> getPrepaidsForClinic(int clinic);

    long deletePrepaidFromClinic(String prepaid, int clinic) throws EntityNotFoundException;

    int maxAvailablePagePerClinic(int id);
}
