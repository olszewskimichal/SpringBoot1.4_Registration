package com.register.example.controllers;

import com.register.example.forms.*;
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
public class ProfileController {
    private ProfileSession userProfileSession;

    @Autowired
    public ProfileController(ProfileSession userProfileSession) {
        this.userProfileSession = userProfileSession;
    }

    @ModelAttribute
    public ProfileForm getProfileForm() {
        return userProfileSession.toForm();
    }

    @RequestMapping("/profile")
    public String displayProfile(Model model, ProfileForm profileForm) {
        model.addAttribute("typeChecker", new TypeChecker());
        return "multiForm";
    }

    @RequestMapping(value = "/profile", params = {"save"}, method = RequestMethod.POST)
    public String saveProfile(@Valid ProfileForm profileForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "multiForm";
        }
        userProfileSession.saveForm(profileForm);
        log.info(userProfileSession.toString());
        return "redirect:/";
    }

    @RequestMapping(value = "/profile", params = {"add"})
    public String addRow(ProfileForm profileForm) {
        log.info("add");
        profileForm.getForms().add(new CokolwiekForm());
        System.out.println(profileForm);
        return "multiForm";
    }

    @RequestMapping(value = "/profile", params = {"add2"})
    public String addRow2(ProfileForm profileForm) {
        log.info("add2");
        profileForm.getForms().add(new JakiKolwiekForm());
        System.out.println(profileForm);
        return "multiForm";
    }

    @RequestMapping(value = "/profile", params = {"remove"})
    public String removeRow(ProfileForm profileForm, HttpServletRequest req) {
        Integer rowId = Integer.valueOf(req.getParameter("remove"));
        profileForm.getForms().remove(rowId.intValue());
        return "multiForm";
    }
}
