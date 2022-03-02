package ar.edu.itba.paw.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "specialties")
public class Specialty {

    @Id
    private String name;

    public Specialty(String name){
        this.name = name;
    }

    public Specialty(){}

    public String getSpecialtyName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
