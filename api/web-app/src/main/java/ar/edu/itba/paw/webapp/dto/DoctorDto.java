package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Doctor;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class DoctorDto {

    private String specialty;
    private UserDto userData;
    private String license;
    private String phoneNumber;
    private URI profileImage;

    public static DoctorDto fromDoctor(Doctor doctor, UriInfo uriInfo) {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.license = doctor.getLicense();
        doctorDto.specialty = doctor.getSpecialty().getSpecialtyName();
        doctorDto.phoneNumber = doctor.getPhoneNumber();
        doctorDto.userData = UserDto.fromUser(doctor.getUser());
        doctorDto.profileImage = uriInfo.getBaseUriBuilder().path("doctors").path(doctor.getLicense()).path("ProfileImage").build();
        return doctorDto;
    }

    public UserDto getUserData() {
        return userData;
    }

    public void setUserData(UserDto userData) {
        this.userData = userData;
    }

    public URI getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(URI profileImage) {
        this.profileImage = profileImage;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public UserDto getUser() {
        return userData;
    }

    public void setUser(UserDto user) {
        this.userData = user;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
