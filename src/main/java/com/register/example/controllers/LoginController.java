package com.register.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Slf4j
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        log.info("wywołanie metody getLoginPage");
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        log.info("wywołanie metody  loginError");
        model.addAttribute("loginError", true);
        model.addAttribute("errorMessage", "Nieprawidłwy użytkownik lub hasło");
        return "login";
    }

}
