package com.register.example.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
public class HomeController {

    @RequestMapping(value = "/")
    public String homePage(Model model) {
        log.info("wywołanie metody homePage");
        return "index";
    }

}
