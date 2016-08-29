package com.register.example.selenium.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class BrowserConfiguration {

    public WebDriver getFirefoxDriver() {
        File pathBinary = new File("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
        FirefoxBinary Binary = new FirefoxBinary(pathBinary);
        FirefoxProfile firefoxPro = new FirefoxProfile();
        return new FirefoxDriver(Binary, firefoxPro);
    }
    public WebDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\Downloads\\chromedriver_win32\\chromedriver.exe");
        return new ChromeDriver();
    }
    public WebDriver trvisDriver() throws IOException {
        String travisCiFlag = System.getenv().get("TRAVIS");
        FirefoxBinary firefoxBinary = "true".equals(travisCiFlag)
                ? getFirefoxBinaryForTravisCi()
                : new FirefoxBinary();

        return new FirefoxDriver(firefoxBinary, new FirefoxProfile());
    }

    FirefoxBinary getFirefoxBinaryForTravisCi() throws IOException {
        String firefoxPath = getFirefoxPath();
        return new FirefoxBinary(new File(firefoxPath));
    }

    String getFirefoxPath() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("which", "firefox");
        pb.redirectErrorStream(true);
        Process process = pb.start();
        try (InputStreamReader isr = new InputStreamReader(process.getInputStream(), "UTF-8");
             BufferedReader br = new BufferedReader(isr)) {
            return br.readLine();
        }
    }
}
