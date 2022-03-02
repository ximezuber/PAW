package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Patient;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class PatientDto {
    private UserDto userData;
    private String prepaid;
    private String prepaidNumber;
    private String id;
    private URI appointments;
    private URI favorites;

    public static PatientDto fromPatient(Patient patient, UriInfo uriInfo) {
        PatientDto patientDto = new PatientDto();
        patientDto.userData = UserDto.fromUser(patient.getUser());
        patientDto.id = patient.getId();
        patientDto.prepaid = patient.getPrepaid();
        patientDto.prepaidNumber = patient.getPrepaidNumber();
        patientDto.appointments = uriInfo.getBaseUriBuilder().path("appointments")
                .path(patient.getEmail()).build();
        patientDto.favorites = uriInfo.getBaseUriBuilder().path("patients")
                .path(patient.getEmail()).path("favorites").build();
        return patientDto;
    }

    public UserDto getUserData() {
        return userData;
    }

    public void setUserData(UserDto userData) {
        this.userData = userData;
    }

    public String getPrepaid() {
        return prepaid;
    }

    public void setPrepaid(String prepaid) {
        this.prepaid = prepaid;
    }

    public String getPrepaidNumber() {
        return prepaidNumber;
    }

    public void setPrepaidNumber(String prepaidNumber) {
        this.prepaidNumber = prepaidNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public URI getAppointments() {
        return appointments;
    }

    public void setAppointments(URI appointments) {
        this.appointments = appointments;
    }

    public URI getFavorites() {
        return favorites;
    }

    public void setFavorites(URI favorites) {
        this.favorites = favorites;
    }
}
