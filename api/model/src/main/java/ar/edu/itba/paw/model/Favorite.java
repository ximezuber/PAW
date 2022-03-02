package ar.edu.itba.paw.model;
import javax.persistence.*;


@Entity
@Table(name = "favorites")
public class Favorite {

    @ManyToOne
    @JoinColumn(name = "doctor", insertable = false, updatable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient", insertable = false, updatable = false)
    private Patient patient;

    @EmbeddedId
    private FavoriteKey favoriteKey;

    public Favorite(){

    }
    public Favorite(Doctor doctor, Patient patient){
        this.patient = patient;
        this.doctor = doctor;
        this.favoriteKey = new FavoriteKey(doctor.getLicense(), patient.getEmail());


    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

}
