package com.register.example.controllers;

import com.register.example.entity.User;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

}
