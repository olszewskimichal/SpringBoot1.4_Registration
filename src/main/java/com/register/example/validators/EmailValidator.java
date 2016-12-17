package com.register.example.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private String emailPattern = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    public void initialize(ValidEmail String) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        pattern = Pattern.compile(emailPattern);
        if (value == null) {
            return false;
        }
        matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
