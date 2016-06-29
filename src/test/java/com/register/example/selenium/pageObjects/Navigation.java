package com.register.example.selenium.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Navigation {
    public WebDriver webDriver;
    By home = By.id("home");
    By pl = By.id("pl");

    public Navigation(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void clickHome() {
        webDriver.findElement(home).click();
    }

    public void clickToPolishLang() {
        webDriver.findElement(pl).click();
    }

}
