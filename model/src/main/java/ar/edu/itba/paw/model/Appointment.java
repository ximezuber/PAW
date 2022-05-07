package ar.edu.itba.paw.model;
import javax.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "appointments")
public class Appointment {

    @ManyToOne
    @JoinColumns({
            @JoinColumn(
                    name = "doctor",insertable = false, updatable = false,
                    referencedColumnName = "doctorLicense"),
            @JoinColumn(
                    name = "clinic",insertable = false, updatable = false,
                    referencedColumnName = "clinicid")
    })
    private DoctorClinic doctorClinic;

    @Column
    private int clinic;

    @EmbeddedId
    private AppointmentKey appointmentKey;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient", insertable = false, updatable = false)
    private User patientUser;

    @Column
    private String patient;


    public Appointment(LocalDateTime date, DoctorClinic doctorClinic, User patient) {
        this.clinic = doctorClinic.getClinic().getId();
        this.doctorClinic = doctorClinic;
        this.patientUser = patient;
        this.patient = patient == null ? null : patient.getEmail();
        this.appointmentKey = new AppointmentKey(doctorClinic.getDoctor().getLicense(), date);
    }

    public Appointment() {
    }

    public DoctorClinic getDoctorClinic() {
        return doctorClinic;
    }

    public User getPatientUser() {
        return patientUser;
    }


    public void setDoctorClinic(DoctorClinic doctorClinic) {
        this.doctorClinic = doctorClinic;
    }

    public void setPatientUser(User patientUser) {
        this.patientUser = patientUser;
    }

    public AppointmentKey getAppointmentKey() {
        return appointmentKey;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Appointment)) return false;
        Appointment other = (Appointment) obj;
        return this.appointmentKey.equals(other.appointmentKey);
    }

    @Override
    public int hashCode() {
        return this.appointmentKey.hashCode();
    }
}
