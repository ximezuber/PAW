package ar.edu.itba.paw.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "prepaids")
public class Prepaid {

    @Id
    private String name;

    public Prepaid(String name) {
        this.name = name;
    }

    public Prepaid(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
