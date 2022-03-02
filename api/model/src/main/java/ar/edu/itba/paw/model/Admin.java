package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "admins")
public class Admin {

    @OneToOne
    @JoinColumn(name = "email")
    @MapsId
    private User user;

    @Id
    private String email;

    public Admin(User user) {
        this.email = user.getEmail();
        this.user = user;
    }

    public Admin(){}

    public String getEmail() {
        return email;
    }

    public User getUser() {
        return user;
    }
}
