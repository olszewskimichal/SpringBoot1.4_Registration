package com.register.example.selenium;

import com.register.example.selenium.configuration.SeleniumTestBase;
import com.register.example.selenium.pageObjects.NonAuthenticatedNavigation;
import com.register.example.selenium.pageObjects.RegisterPage;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Profile;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Profile("!test")
public class RegisterSeleniumTest extends SeleniumTestBase {

    public WebDriver driver;

    @Before
    public void setUp() {
        driver = browserConfiguration.getChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void shouldRegisterWithCorrectData() {
        driver.get("http://localhost:8080/register");
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.typeName("userTest");
        registerPage.typeLastName("userTest");
        registerPage.typeEmail("userTest@poczta.pl");
        registerPage.typeLogin("userTest");
        registerPage.typePassword("userTest");
        registerPage.typeConfirmPassword("userTest");
        registerPage.clickOnRegisterButton();
        System.out.println(driver.getPageSource() + "\n////////////////////////////////////");
        System.out.println(driver.getTitle() + "\n////////////////////////////////////");
        assertThat(driver.getPageSource()).contains("Twoje konto zostalo stworzone");
        assertThat(driver.getTitle()).isEqualTo("Zarejestruj się");
        driver.quit();
    }

    @Test
    public void shouldRegisterFailedWithExistingAccount() {
        driver.get("http://localhost:8080/register");
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.typeName("user");
        registerPage.typeLastName("user");
        registerPage.typeEmail("user@poczta.pl");
        registerPage.typeLogin("user");
        registerPage.typePassword("user");
        registerPage.typeConfirmPassword("user");
        registerPage.clickOnRegisterButton();
        System.out.println(driver.getPageSource() + "\n////////////////////////////////////");
        System.out.println(driver.getTitle() + "\n////////////////////////////////////");
        assertThat(driver.getPageSource()).contains("Whitelabel Error Page");
        driver.quit();
    }

    @Test
    public void shouldRedirectToLoginPage() {
        driver.get("http://localhost:8080/register");
        assertThat(driver.getPageSource()).contains("Formularz rejestracji");
        assertThat(driver.getTitle()).isEqualTo("Zarejestruj się");
        NonAuthenticatedNavigation navigation = new NonAuthenticatedNavigation(driver);
        navigation.clickOnLoginPage();
        assertThat(driver.getPageSource()).contains("Zaloguj się");
        assertThat(driver.getTitle()).isEqualTo("Strona logowania");
        driver.quit();
    }

}
