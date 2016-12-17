package com.register.example.selenium.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class BrowserConfiguration {

    private static FirefoxBinary getFirefoxBinaryForTravisCi() throws IOException {
        String firefoxPath = getFirefoxPath();
        Logger staticLog = LoggerFactory.getLogger(BrowserConfiguration.class);
        staticLog.info("Firefox path: " + firefoxPath);

        return new FirefoxBinary(new File(firefoxPath));
    }

    private static String getFirefoxPath() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("which", "firefox");
        pb.redirectErrorStream(true);
        Process process = pb.start();
        try (InputStreamReader isr = new InputStreamReader(process.getInputStream(), "UTF-8");
             BufferedReader br = new BufferedReader(isr)) {
            return br.readLine();
        }
    }


    public WebDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\Admin\\Downloads\\chromedriver_win32\\chromedriver.exe");
        return new ChromeDriver();
    }

    public WebDriver firefox() {
        return new FirefoxDriver();
    }

    public WebDriver firefox2() throws IOException {
        String travisCiFlag = System.getenv().get("TRAVIS");
        FirefoxBinary firefoxBinary = "true".equals(travisCiFlag)
                ? getFirefoxBinaryForTravisCi()
                : new FirefoxBinary();

        return new FirefoxDriver(firefoxBinary, new FirefoxProfile());
    }

}
