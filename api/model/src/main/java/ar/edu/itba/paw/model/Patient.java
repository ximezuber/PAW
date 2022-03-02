package ar.edu.itba.paw.model;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @OneToOne
    @JoinColumn(name = "email")
    @MapsId
    private User user;

    @Id
    private String email;

    @Column(name = "prepaid")
    private String prepaid;

    @Column(name = "prepaidNumber")
    private String prepaidNumber;

    @Column(name = "id")
    private String id;

    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "favorites",joinColumns = {@JoinColumn(name = "patient")},inverseJoinColumns = {@JoinColumn(name = "doctor")})
    private List<Doctor> favorites;

    
    public Patient(String id, String prepaid, String prepaidNumber,User user) {
        this.user = user;
        this.id = id;
        this.prepaid = prepaid;
        this.prepaidNumber = prepaidNumber;
        this.email = user.getEmail();
    }

    public Patient(){
    }

    public User getUser() {
        return user;
    }

    public List<Doctor> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Doctor> favorites) {
        this.favorites = favorites;
    }

    public String getId() {
        return id;
    }

    public String getPrepaid() {
        return prepaid;
    }

    public String getPrepaidNumber() {
        return prepaidNumber;
    }


    public String getEmail() {
        return user.getEmail();
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public void setFirstName(String firstName) {
        this.user.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        this.user.setLastName(lastName);
    }

    public void setPrepaid(String prepaid) {
        this.prepaid = prepaid;
    }

    public void setPrepaidNumber(String prepaidNumber) {
        this.prepaidNumber = prepaidNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Patient){
            return ((Patient) obj).getEmail().equals(this.getEmail());
        }
        return false;
    }
}
