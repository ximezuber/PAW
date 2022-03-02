package ar.edu.itba.paw.webapp.form;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.EditablePassword;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.FieldMatch;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FieldMatch(field = "newPassword",fieldMatch = "repeatPassword", message = "signup.password.not.matching")
public class PersonalInformationForm {

    @Size(max = 20, message = "signup.prepaid.number.size.max.constraint")
    @Pattern(regexp = "[a-zA-Z ]+", message = "message = alphabetic.constraint")
    private String firstName;

    @Size(max = 20, message = "signup.prepaid.number.size.max.constraint")
    @Pattern(regexp = "[a-zA-Z ]+", message = "message = alphabetic.constraint")
    private String lastName;

    @Size(max = 20, message = "signup.prepaid.number.size.max.constraint")
    @Pattern(regexp = "[a-zA-Z0-9 ]*", message = "alphanumeric.constraint")
    @EditablePassword(message = "user.password.too.short")
    private String newPassword;

    @Size(max = 20, message = "signup.prepaid.number.size.max.constraint")
    @Pattern(regexp = "[a-zA-Z0-9 ]*", message = "alphanumeric.constraint")
    private String repeatPassword;

    private String prepaid;

    @Size(max = 20, message = "signup.prepaid.number.size.max.constraint")
    @Pattern(regexp = "[0-9 ]*", message = "numeric.constraint")
    private String prepaidNumber;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String password) {
        this.newPassword = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getPrepaid() {
        return prepaid;
    }

    public void setPrepaid(String prepaid) {
        this.prepaid = prepaid;
    }

    public String getPrepaidNumber() {
        return prepaidNumber;
    }

    public void setPrepaidNumber(String prepaidNumber) {
        this.prepaidNumber = prepaidNumber;
    }
}
