package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FavoriteKey implements Serializable {

    @Column
    private String doctor;

    @Column
    private String patient;

    public FavoriteKey(){}

    public FavoriteKey(String doctor, String patient){
        this.doctor = doctor;
        this.patient = patient;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteKey)) return false;
        FavoriteKey k = (FavoriteKey) o;
        return Objects.equals(getDoctor(), k.getDoctor()) &&
                Objects.equals(getPatient(), k.getPatient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDoctor(), getPatient());
    }
}
