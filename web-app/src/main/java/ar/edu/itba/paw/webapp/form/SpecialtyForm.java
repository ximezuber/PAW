package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SpecialtyForm {

    @Size(min=1, max=30, message = "specialty.size.between.constraint")
    @Pattern(regexp = "[a-zA-Z ]+", message = "alphabetic.constraint")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
