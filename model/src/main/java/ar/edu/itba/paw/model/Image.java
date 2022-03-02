package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "images_id_seq")
    @SequenceGenerator(sequenceName = "images_id_seq", name = "images_id_seq")
    private int id;

    @OneToOne(targetEntity = Doctor.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor")
    private Doctor doctor;

    @Column(name = "image")
    private byte[] image;

    public Image(Doctor doctor, byte[] image) {
        this.doctor = doctor;
        this.image = image;
    }

    public Image(){

    }

    public long getId() {
        return id;
    }

    public String getLicense() {
        return doctor.getLicense();
    }

    public byte[] getImage() { return this.image; }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLicense(String license) {
        this.doctor.setLicense(license);
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
