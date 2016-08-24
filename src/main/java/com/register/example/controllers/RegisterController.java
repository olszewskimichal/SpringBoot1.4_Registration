package com.register.example.controllers;

import com.register.example.entity.User;
import com.register.example.entity.tokens.VerificationToken;
import com.register.example.forms.UserCreateForm;
import com.register.example.service.UserService;
import com.register.example.validators.UserCreateValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@Slf4j
public class RegisterController {

    private final UserCreateValidator userCreateValidator;

    private final UserService userService;

    @Autowired
    public RegisterController(UserCreateValidator userCreateValidator, UserService userService) {
        this.userCreateValidator = userCreateValidator;
        this.userService = userService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(Model model) {
        log.info("wywołanie metody registerPage");
        try {
            model.addAttribute("userCreateForm", new UserCreateForm());
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            log.info(model.toString());
        }
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerSubmit(@ModelAttribute @Valid UserCreateForm userCreateForm, BindingResult result, Model model, HttpServletRequest servletRequest) {
        log.info("wywołanie metody registerSubmit");
        userCreateValidator.validate(userCreateForm, result);
        if (result.hasErrors()) {
            log.info(userCreateForm.toString());
            return "register";
        } else {
            User user = userService.create(userCreateForm);

            log.info("stworzono uzytkownika \n" + user.getLogin());
            model.addAttribute("userCreateForm", new UserCreateForm());
            model.addAttribute("confirmRegistration", true);
            return "register";
        }
    }

    @RequestMapping(value = "/register/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(Model model, @RequestParam("token") String token) {
        log.info("Potwierdzenie rejestacji");
        Optional<VerificationToken> verificationToken = userService.getVerificationToken(token);
        if (!verificationToken.isPresent()) {
            model.addAttribute("blednyToken", true);
            return "login";
        }
            User user = verificationToken.get().getUser();
            LocalDateTime localDateTime = LocalDateTime.now();
            long diff = Duration.between(localDateTime, verificationToken.get().getExpiryDate()).toMinutes();

            if (diff < 0L) {
                log.info(String.format("Token juz jest nieaktulany \n dataDO= %s \n", verificationToken.get().getExpiryDate()));
                model.addAttribute("nieaktualny", true);
            } else {
                log.info("Token jest aktualny - aktywacja konta");
                if (verificationToken.get().getIsUsed()){
                    log.info(String.format("Token juz jest wykorzystany"));
                    model.addAttribute("wykorzystany", true);
                }else {
                    userService.activateUser(user, verificationToken.get());
                    model.addAttribute("aktualny", true);
                }
            }
        return "login";
    }


}
