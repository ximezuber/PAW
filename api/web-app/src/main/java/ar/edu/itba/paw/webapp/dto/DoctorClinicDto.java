package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.DoctorClinic;

import javax.ws.rs.core.UriInfo;


public class DoctorClinicDto {

    private DoctorDto doctor;
    private ClinicDto clinic;
    private int consultPrice;


    public static DoctorClinicDto fromDoctorClinic(DoctorClinic doctorClinic, UriInfo uriInfo) {
        DoctorClinicDto doctorClinicDto = new DoctorClinicDto();
        doctorClinicDto.doctor = DoctorDto.fromDoctor(doctorClinic.getDoctor(), uriInfo);
        doctorClinicDto.clinic = ClinicDto.fromClinic(doctorClinic.getClinic(), uriInfo);
        doctorClinicDto.consultPrice = doctorClinic.getConsultPrice();
        return doctorClinicDto;
    }

    public DoctorDto getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDto doctorDto) {
        this.doctor = doctorDto;
    }

    public ClinicDto getClinic() {
        return clinic;
    }

    public void setClinic(ClinicDto clinic) {
        this.clinic = clinic;
    }

    public int getConsultPrice() {
        return consultPrice;
    }

    public void setConsultPrice(int consultPrice) {
        this.consultPrice = consultPrice;
    }

}
