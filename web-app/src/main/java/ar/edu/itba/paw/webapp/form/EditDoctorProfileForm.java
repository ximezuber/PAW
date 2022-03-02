package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.helpers.validation.annotations.EditablePassword;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.Exists;
import ar.edu.itba.paw.webapp.helpers.validation.annotations.FieldMatch;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FieldMatch(field = "newPassword",fieldMatch = "repeatPassword", message = "signup.password.not.matching")
public class EditDoctorProfileForm {

    // User information

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

    // Doctor information
    @Exists(field = "specialty", message = "value.not.exists")
    private String specialty;

    @Pattern(regexp = "[0-9]+", message = "numeric.constraint")
    private String phoneNumber;

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

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatNewPassword) {
        this.repeatPassword = repeatNewPassword;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
