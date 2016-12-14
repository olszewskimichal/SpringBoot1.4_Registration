package com.register.example.validators;

import com.register.example.IntegrationTestBase;
import com.register.example.entity.Role;
import com.register.example.entity.User;
import com.register.example.forms.UserCreateForm;
import com.register.example.repository.UserRepository;
import com.register.example.repository.VerificationTokenRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserCreateValidatorTest extends IntegrationTestBase {

    @Autowired
    UserCreateValidator userCreateValidator;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationTokenRepository tokenRepository;

    @Autowired
    PersistentTokenRepository persistentTokenRepository;

    Errors errors;

    @Test
    public void shouldReturnErrorWithEmptyConfirmPassword() throws Exception {
        UserCreateForm userCreateForm = new UserCreateForm();
        userCreateForm.setPassword("dupa");
        errors = new BindException(userCreateForm, "userCreateForm");
        userCreateValidator.validate(userCreateForm, errors);
        assertThat(errors.getErrorCount()).isEqualTo(1);
    }

    @Test
    public void shouldReturnErrorWithEmptyPasswordAndConfirmPassword() throws Exception {
        UserCreateForm userCreateForm = new UserCreateForm();
        errors = new BindException(userCreateForm, "userCreateForm");
        userCreateValidator.validate(userCreateForm, errors);
        assertThat(errors.getErrorCount()).isEqualTo(1);
    }

    @Test
    public void shouldReturn0ErrorsWithCorrectPasswords() throws Exception {
        UserCreateForm userCreateForm = new UserCreateForm();
        userCreateForm.setPassword("dupa");
        userCreateForm.setConfirmPassword("dupa");
        errors = new BindException(userCreateForm, "userCreateForm");
        userCreateValidator.validate(userCreateForm, errors);
        assertThat(errors.getErrorCount()).isEqualTo(0);
    }

    @Test
    public void shouldReturnErrorWithExistingUserName() throws Exception {
        userRepository.save(new User("user4", "user4", "user4@o2.pl", "user4", "user4", Role.USER, true));
        UserCreateForm userCreateForm = new UserCreateForm();
        userCreateForm.setPassword("dupa");
        userCreateForm.setConfirmPassword("dupa");
        userCreateForm.setLogin("user4");
        userCreateForm.setEmail("user4@o2.pl");
        errors = new BindException(userCreateForm, "userCreateForm");
        userCreateValidator.validate(userCreateForm, errors);
        assertThat(errors.getErrorCount()).isEqualTo(1);
        System.out.println(errors.getAllErrors());
    }
}