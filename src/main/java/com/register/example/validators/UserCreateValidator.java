package com.register.example.validators;


import com.register.example.forms.UserCreateForm;
import com.register.example.repository.UserRepository;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@ToString
@Slf4j
@Component
public class UserCreateValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserCreateValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateForm form = (UserCreateForm) target;
        log.info("Walidacja formularza tworzenia uzytkownika {}", target);
        if (form.getPassword() == null || form.getConfirmPassword() == null)
            errors.rejectValue("password", "password_error");
        else if (!form.getPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("password", "password_error");
        }

        if (userRepository.findUserDistinctByEmailOrLogin(form.getEmail(), form.getLogin()).isPresent()) {
            errors.rejectValue("email", "email_error");
        }
    }
}
