package com.register.example.selenium.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;

public class BrowserConfiguration {

    public WebDriver getFirefoxDriver() {
        File pathBinary = new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
        FirefoxBinary Binary = new FirefoxBinary(pathBinary);
        FirefoxProfile firefoxPro = new FirefoxProfile();
        return new FirefoxDriver(Binary, firefoxPro);
    }

    public WebDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\Admin\\Downloads\\chromedriver_win32\\chromedriver.exe");
        return new ChromeDriver();
    }

    public WebDriver firefox() {
        return new FirefoxDriver();
    }

}
