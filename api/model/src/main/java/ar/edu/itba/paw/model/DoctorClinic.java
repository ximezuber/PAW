package ar.edu.itba.paw.model;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "doctorClinics")
public class DoctorClinic {

    @ManyToOne
    @JoinColumn(name = "doctorLicense",referencedColumnName = "license", insertable = false, updatable = false)
    private Doctor doctor;

    @EmbeddedId
    private DoctorClinicKey doctorClinicKey;

    @ManyToOne
    @JoinColumn(name = "clinicid", insertable = false, updatable = false)
    private Clinic clinic;

    @Column(name = "consultPrice")
    private int consultPrice;

    @OneToMany(mappedBy = "doctorClinic", fetch=FetchType.EAGER)
    private List<Schedule> schedule;

    @OneToMany(mappedBy = "doctorClinic", fetch=FetchType.EAGER)
    private List<Appointment> appointments;

    public DoctorClinic(Doctor doctor, Clinic clinic, int consultPrice){
        this.doctor = doctor;
        this.clinic = clinic;
        this.doctorClinicKey = new DoctorClinicKey(doctor.getLicense(),clinic.getId());
        this.consultPrice = consultPrice;
    }

    public DoctorClinic(){

    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public int getConsultPrice() {
        return consultPrice;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void setConsultPrice(int consultPrice) {
        this.consultPrice = consultPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DoctorClinic){
            return ((DoctorClinic) obj).getDoctor().equals(this.getDoctor()) && ((DoctorClinic) obj).getClinic().equals(this.getClinic());
        }
        return false;
    }
}
