package com.register.example.validators;

import com.register.example.IntegrationTestBase;
import com.register.example.forms.ResetPasswordForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Java6Assertions.assertThat;


public class ResetPasswordValidatorTest extends IntegrationTestBase {

    @Autowired
    ResetPasswordValidator resetPasswordValidator;

    Errors errors;

    @Test
    public void shouldReturnErrorWithEmptyConfirmPassword() throws Exception {
        ResetPasswordForm resetPasswordForm = new ResetPasswordForm("token");
        resetPasswordForm.setPassword("dupa");
        errors = new BindException(resetPasswordForm, "resetPasswordForm");
        resetPasswordValidator.validate(resetPasswordForm, errors);
        assertThat(errors.getErrorCount()).isEqualTo(1);
    }

    @Test
    public void shouldReturnErrorWithEmptyPasswordAndConfirmPassword() throws Exception {
        ResetPasswordForm resetPasswordForm = new ResetPasswordForm("token");
        errors = new BindException(resetPasswordForm, "resetPasswordForm");
        resetPasswordValidator.validate(resetPasswordForm, errors);
        assertThat(errors.getErrorCount()).isEqualTo(1);
    }

    @Test
    public void shouldReturn0ErrorsWithCorrectPasswords() throws Exception {
        ResetPasswordForm resetPasswordForm = new ResetPasswordForm("token");
        resetPasswordForm.setPassword("dupa");
        resetPasswordForm.setConfirmPassword("dupa");
        errors = new BindException(resetPasswordForm, "resetPasswordForm");
        resetPasswordValidator.validate(resetPasswordForm, errors);
        assertThat(errors.getErrorCount()).isEqualTo(0);
    }


}