package com.register.example.selenium.configuration;

import org.springframework.context.annotation.Profile;

@Profile("!test")
public class SeleniumTestBase {
    public static BrowserConfiguration browserConfiguration = new BrowserConfiguration();


}
