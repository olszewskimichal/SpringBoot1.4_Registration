package com.register.example.controllers;

import com.register.example.entity.User;
import com.register.example.entity.tokens.PasswordResetToken;
import com.register.example.forms.ResetPasswordForm;
import com.register.example.service.UserService;
import com.register.example.validators.ResetPasswordValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@Slf4j
public class ResetPasswordController {
    private final ResetPasswordValidator resetPasswordValidator;

    private final UserService userService;

    @Autowired
    public ResetPasswordController(ResetPasswordValidator resetPasswordValidator, UserService userService) {
        this.resetPasswordValidator = resetPasswordValidator;
        this.userService = userService;
    }

    @GetMapping(value = "/resetPassword")
    public String getPasswordResetPage(Model model, @RequestParam("token") String token) {
        log.info("Resetowanie hasła");
        Optional<PasswordResetToken> resetToken = userService.getPasswordResetToken(token);
        if (!resetToken.isPresent()) {
            model.addAttribute("blednyPasswordToken", true);
            return "login";
        }
        if (resetToken.get().getIsUsed()) {
            log.info("Token juz jest wykorzystany");
            model.addAttribute("wykorzystany", true);
            return "login";
        }
        log.info("Token jest aktualny - wywołanie strony do resetowania hasła");
        model.addAttribute("resetPasswordForm", new ResetPasswordForm(token));
        return "resetPassword";
    }


    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public String resetPassword(@ModelAttribute @Valid ResetPasswordForm resetPasswordForm, BindingResult result, Model model) {
        log.info("Resetowanie hasła");
        resetPasswordValidator.validate(resetPasswordForm, result);
        if (result.hasErrors()) {
            log.info(resetPasswordForm.toString());
            return "resetPassword";
        } else {
            User user = userService.resetPassword(resetPasswordForm);
            log.info("Zmieniono hasło uzytkownika " + user.getLogin());
            model.addAttribute("confirmPasswordReset", true);
            return "login";
        }
    }
}
