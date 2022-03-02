package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PrepaidToClinicKey implements Serializable {

    @Column
    private String prepaid;

    @Column
    private int clinicid;

    public String getPrepaid() {
        return prepaid;
    }

    public void setPrepaid(String prepaid) {
        this.prepaid = prepaid;
    }

    public int getClinic() {
        return clinicid;
    }

    public void setClinic(int clinic) {
        this.clinicid = clinic;
    }

    public PrepaidToClinicKey(String prepaid, int clinic) {
        this.prepaid = prepaid;
        this.clinicid = clinic;
    }

    public PrepaidToClinicKey() {
    }

        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrepaidToClinicKey that = (PrepaidToClinicKey) o;
        return getClinic() == that.getClinic() &&
                getPrepaid().equals(that.getPrepaid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrepaid(), getClinic());
    }
}
