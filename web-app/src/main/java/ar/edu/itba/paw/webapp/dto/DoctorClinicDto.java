package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.DoctorClinic;

import javax.ws.rs.core.UriInfo;
import java.net.URI;


public class DoctorClinicDto {

    private URI doctor;
    private URI clinic;
    private int consultPrice;


    public static DoctorClinicDto fromDoctorClinic(DoctorClinic doctorClinic, UriInfo uriInfo) {
        DoctorClinicDto doctorClinicDto = new DoctorClinicDto();
        doctorClinicDto.doctor = uriInfo.getBaseUriBuilder().path("doctors")
                .path(doctorClinic.getDoctor().getLicense()).build();
        doctorClinicDto.clinic = uriInfo.getBaseUriBuilder().path("clinics")
                .path(String.valueOf(doctorClinic.getClinic().getId())).build();
        doctorClinicDto.consultPrice = doctorClinic.getConsultPrice();
        return doctorClinicDto;
    }

    public URI getDoctor() {
        return doctor;
    }

    public void setDoctor(URI doctor) {
        this.doctor = doctor;
    }

    public URI getClinic() {
        return clinic;
    }

    public void setClinic(URI clinic) {
        this.clinic = clinic;
    }

    public int getConsultPrice() {
        return consultPrice;
    }

    public void setConsultPrice(int consultPrice) {
        this.consultPrice = consultPrice;
    }

}
