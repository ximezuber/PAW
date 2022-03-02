package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PrepaidForm {

    @Size(min=1, max=20, message = "signup.size.between.constraint")
    @Pattern(regexp = "[a-zA-Z0-9 ]*", message = "alphanumeric.constraint")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
