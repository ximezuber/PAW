package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @OneToOne
    @JoinColumn(name = "email")
    private User user;

    @OneToOne
    @JoinColumn(name = "specialty")
    private Specialty specialty;

    @Id
    private String license;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    public Doctor(Specialty specialty, String license, String phoneNumber, User user){
        this.user = user;
        this.specialty = specialty;
        this.license = license;
        this.phoneNumber = phoneNumber;
    }

    public Doctor(){

    }

    public User getUser() { return user; }

    public Specialty getSpecialty() {
        return specialty;
    }

    public String getLicense() {
        return license;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {return user.getEmail();}

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getPassword(){return user.getPassword();}

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(this.getLicense());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Doctor){
            return ((Doctor) obj).getLicense().equals(this.getLicense());
        }
        return false;
    }
}
