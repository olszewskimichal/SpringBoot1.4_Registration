package com.register.example.selenium.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage {
    By email = By.id("email");
    By login = By.id("login");
    By name = By.id("name");
    By lastName = By.id("lastName");
    By password = By.id("password");
    By confirmPassword = By.id("confirmPassword");
    By registerButton = By.id("submit");
    private WebDriver webDriver;

    public RegisterPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void typeEmail(String email) {
        webDriver.findElement(this.email).sendKeys(email);
    }

    public void typeLogin(String login) {
        webDriver.findElement(this.login).sendKeys(login);
    }

    public void typeName(String name) {
        webDriver.findElement(this.name).sendKeys(name);
    }

    public void typeLastName(String lastName) {
        webDriver.findElement(this.lastName).sendKeys(lastName);
    }

    public void typePassword(String pass) {
        webDriver.findElement(password).sendKeys(pass);

    }

    public void typeConfirmPassword(String confirmPassword) {
        webDriver.findElement(this.confirmPassword).sendKeys(confirmPassword);

    }

    public void clickOnRegisterButton() {
        webDriver.findElement(registerButton).click();
    }
}
