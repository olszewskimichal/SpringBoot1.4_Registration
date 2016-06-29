package com.register.example.selenium.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AuthenticatedNavigation extends Navigation {
    By loginName = By.id("loginName");
    By logout = By.id("logout");

    public AuthenticatedNavigation(WebDriver webDriver) {
        super(webDriver);
    }

    public void clickOnLogout() {
        webDriver.findElement(logout).click();
    }

    public String getLoginName() {
        return webDriver.findElement(loginName).getText();
    }

    public void clickOnloginName() {
        webDriver.findElement(loginName).click();
    }

}
