package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Clinic;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ClinicDto {

    private int id;
    private String address;
    private String Location;
    private String name;
    private URI clinicPrepaids;

    public static ClinicDto fromClinic(Clinic clinic, UriInfo uriInfo) {
        ClinicDto clinicDto = new ClinicDto();
        clinicDto.address = clinic.getAddress();
        clinicDto.id = clinic.getId();
        clinicDto.Location = clinic.getLocation().getLocationName();
        clinicDto.name = clinic.getName();
        clinicDto.clinicPrepaids = uriInfo.getBaseUriBuilder().path("clinics")
                .path(String.valueOf(clinic.getId())).path("clinicPrepaids").build();
        return clinicDto;
    }

    public URI getClinicPrepaids() {
        return clinicPrepaids;
    }

    public void setClinicPrepaids(URI clinicPrepaids) {
        this.clinicPrepaids = clinicPrepaids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
