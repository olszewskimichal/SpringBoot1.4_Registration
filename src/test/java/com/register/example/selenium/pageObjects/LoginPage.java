package com.register.example.selenium.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    By username = By.id("username");
    By password = By.id("password");
    By loginButton = By.name("loginSubmit");
    By registerLink = By.id("register");
    private WebDriver webDriver;

    public LoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void logInToApp(String login, String password) {
        typeUserName(login);
        typePassword(password);
        clickOnLoginButton();
    }

    public void typeUserName(String login) {
        webDriver.findElement(username).sendKeys(login);

    }

    public void typePassword(String pass) {
        webDriver.findElement(password).sendKeys(pass);

    }

    public void clickOnLoginButton() {
        webDriver.findElement(loginButton).click();
    }

    public void clickOnRegisterLink() {
        webDriver.findElement(registerLink).click();
    }
}
