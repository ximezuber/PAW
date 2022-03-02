package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.helpers.validation.annotations.ClinicExists;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.UniqueDoctorClinic;

import javax.validation.constraints.Min;

@UniqueDoctorClinic(clinic = "clinic", message = "value.registered")
public class DoctorClinicForm {

    @ClinicExists(message = "value.not.exists")
    private int clinic;

    @Min(value = 0, message = "doctor.clinic.size.min.constraint")
    private int consultPrice;

    public int getClinic() {
        return clinic;
    }

    public void setClinic(int clinic) {
        this.clinic = clinic;
    }

    public int getConsultPrice() {
        return consultPrice;
    }

    public void setConsultPrice(int consultPrice) {
        this.consultPrice = consultPrice;
    }
}
