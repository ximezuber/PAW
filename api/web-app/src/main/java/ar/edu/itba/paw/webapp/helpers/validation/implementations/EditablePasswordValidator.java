package ar.edu.itba.paw.webapp.helpers.validation.implementations;

import ar.edu.itba.paw.webapp.helpers.validation.annotations.EditablePassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EditablePasswordValidator implements ConstraintValidator<EditablePassword,String> {

    private static final int MIN_SIZE = 8;

    @Override
    public void initialize(EditablePassword editablePassword) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext cxt) {
        if (password == null)
            return true;
        return password.equals("") || password.length() >= MIN_SIZE;
    }
}
