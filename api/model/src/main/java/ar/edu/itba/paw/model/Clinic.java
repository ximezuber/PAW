package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "clinics")
public class Clinic {

    @Column
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "clinics_id_seq")
    @SequenceGenerator(sequenceName = "clinics_id_seq", name = "clinics_id_seq", allocationSize = 1)
    private int id;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @OneToMany(mappedBy = "clinic")
    private List<PrepaidToClinic> prepaids;


    public Clinic(int id, String name, String address, Location location){
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
        this.prepaids = new LinkedList<>();
    }

    public Clinic(){

    }

    public List<PrepaidToClinic> getPrepaids() {
        return prepaids;
    }

    public void setPrepaids(List<PrepaidToClinic> prepaids) {
        this.prepaids = prepaids;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Clinic){
            return ((Clinic) obj).getId() == this.getId();
        }
        return false;
    }
}
