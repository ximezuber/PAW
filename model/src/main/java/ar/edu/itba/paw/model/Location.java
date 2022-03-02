package ar.edu.itba.paw.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    private String name;

    @OneToMany(mappedBy = "location")
    private List<Clinic> clinicsInLocation;

    public Location(String name){
        this.name = name;
        this.clinicsInLocation = new LinkedList<>();
    }

    public Location(){

    }

    public String getLocationName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

