package com.register.example.selenium.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NonAuthenticatedNavigation extends Navigation {

    By login = By.id("loginPage");

    public NonAuthenticatedNavigation(WebDriver webDriver) {
        super(webDriver);
    }

    public void clickOnLoginPage() {
        webDriver.findElement(login).click();
    }
}
