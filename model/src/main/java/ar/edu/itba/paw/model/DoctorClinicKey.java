package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DoctorClinicKey implements Serializable {

    @Column
    private String doctorLicense;

    @Column
    private int clinicId;

    public DoctorClinicKey(String doctorLicense, int clinicId) {
        this.doctorLicense = doctorLicense;
        this.clinicId = clinicId;
    }

    public DoctorClinicKey(){
    }

    public String getDoctorLicense() {
        return doctorLicense;
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setDoctorLicense(String doctorLicense) {
        this.doctorLicense = doctorLicense;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorClinicKey that = (DoctorClinicKey) o;
        return clinicId == that.getClinicId() &&
                Objects.equals(doctorLicense, that.getDoctorLicense());
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorLicense, clinicId);
    }
}
