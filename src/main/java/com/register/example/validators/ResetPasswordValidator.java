package com.register.example.validators;


import com.register.example.forms.ResetPasswordForm;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@ToString
@Slf4j
@Component
public class ResetPasswordValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(ResetPasswordForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ResetPasswordForm form = (ResetPasswordForm) target;
        log.info("Walidacja formularza resetowania hasła uzytkownika {}", target);
        if (form.getPassForm() == null || form.getConfirmPassword() == null)
            errors.rejectValue("passForm", "password_error");
        else if (!form.getPassForm().equals(form.getConfirmPassword())) {
            errors.rejectValue("passForm", "password_error");
        }
    }
}
