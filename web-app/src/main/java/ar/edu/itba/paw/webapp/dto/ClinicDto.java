package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Clinic;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class ClinicDto {

    private int id;
    private String address;
    private String location;
    private String name;
    private URI prepaid;

    public static ClinicDto fromClinic(Clinic clinic, UriInfo uriInfo) {
        ClinicDto clinicDto = new ClinicDto();
        clinicDto.address = clinic.getAddress();
        clinicDto.id = clinic.getId();
        clinicDto.location = clinic.getLocation().getLocationName();
        clinicDto.name = clinic.getName();
        clinicDto.prepaid = uriInfo.getBaseUriBuilder().path("clinics")
                .path(String.valueOf(clinic.getId())).path("prepaid").path("all").build();
        return clinicDto;
    }

    public URI getPrepaid() {
        return prepaid;
    }

    public void setPrepaid(URI prepaid) {
        this.prepaid = prepaid;
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
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
