package com.register.example.selenium.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ResetPasswordPage {
    @CacheLookup
    By password = By.id("password");
    @CacheLookup
    By confirmPassword = By.name("confirmPassword");
    @CacheLookup
    By send = By.cssSelector("button.btn.btn-lg.btn-primary.btn-block");

    private int timeout = 15;

    private WebDriver webDriver;

    public ResetPasswordPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void resetPassword(String password, String confirmPassword) {
        typePasswordConfirm(confirmPassword);
        typePassword(password);
        clickOnSendButton();
    }

    public void typePassword(String pass) {
        webDriver.findElement(password).sendKeys(pass);
    }

    public void typePasswordConfirm(String pass) {
        webDriver.findElement(confirmPassword).sendKeys(pass);
    }

    public void clickOnSendButton() {
        webDriver.findElement(send).click();
    }

    public ResetPasswordPage verifyPageLoaded(String pageLoadedText) {
        (new WebDriverWait(webDriver, timeout)).until((ExpectedCondition<Boolean>) d -> d.getPageSource().contains(pageLoadedText));
        return this;
    }

}
