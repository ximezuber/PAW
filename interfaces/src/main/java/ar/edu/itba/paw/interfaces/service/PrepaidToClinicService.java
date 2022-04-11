package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.model.Clinic;
import ar.edu.itba.paw.model.Prepaid;
import ar.edu.itba.paw.model.PrepaidToClinic;
import ar.edu.itba.paw.model.exceptions.EntityNotFoundException;

import java.util.List;

public interface PrepaidToClinicService extends PaginationService<PrepaidToClinic> {

    PrepaidToClinic addPrepaidToClinic(Prepaid prepaid, Clinic clinic) throws EntityNotFoundException;

    boolean clinicHasPrepaid(String prepaid, int clinic);

    List<Prepaid> getPrepaidForClinic(Clinic clinic, int page);

    List<Prepaid> getPrepaidForClinic(Clinic clinic);

    void deletePrepaidFromClinic(Prepaid prepaid, Clinic clinic) throws EntityNotFoundException;

    int maxAvailablePagePerClinic(Clinic id);
}
