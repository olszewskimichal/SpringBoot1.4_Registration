package com.register.example.selenium;

import com.register.example.selenium.configuration.SeleniumTestBase;
import com.register.example.selenium.pageObjects.AuthenticatedNavigation;
import com.register.example.selenium.pageObjects.LoginPage;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@Profile("!test")
@Ignore
public class LoginSeleniumTest extends SeleniumTestBase {

    public WebDriver driver;

    @Before
    public void setUp() throws IOException {
        driver = browserConfiguration.firefox();
        driver.manage().window().maximize();
    }

    @Test
    public void shouldLoginWithCorrectAuthentication() {
        driver.get("http://localhost:8080/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.typeUserName("admin");
        loginPage.typePassword("admin");
        loginPage.clickOnLoginButton();
        System.out.println(driver.getPageSource() + "\n////////////////////////////////////");
        System.out.println(driver.getTitle() + "\n////////////////////////////////////");
        assertThat(driver.getPageSource()).contains("<span>Witamy</span>\n" +
                "                <strong>przykladoweImie</strong>");
        assertThat(driver.getTitle()).isEqualTo("Strona głowna");
        AuthenticatedNavigation authenticatedNavigation = new AuthenticatedNavigation(driver);
        assertThat(authenticatedNavigation.getLoginName()).isEqualTo("admin");
        authenticatedNavigation.clickOnloginName();
        authenticatedNavigation.clickOnLogout();
        assertThat(driver.getTitle()).isEqualTo("Strona logowania");
        driver.quit();
    }

    @Test
    public void shouldGetErrorWhenLoginWithIncorrectAuthentication() {
        driver.get("http://localhost:8080/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.typeUserName("admin");
        loginPage.typePassword("dupa");
        loginPage.clickOnLoginButton();
        assertThat(driver.getPageSource()).contains("Nieprawidłowy użytkownik lub hasło");
        assertThat(driver.getTitle()).isEqualTo("Strona logowania");
        driver.quit();
    }

    @Test
    public void shouldRedirectToRegisterPage() {
        driver.get("http://localhost:8080/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickOnRegisterLink();
        assertThat(driver.getPageSource()).contains("Formularz rejestracji");
        assertThat(driver.getTitle()).isEqualTo("Zarejestruj się");
        driver.quit();
    }

    @Test
    @Ignore
    //Zablokuje inne testy TODO do poprawy
    public void sshouldBlockUserAfter3badCredentials() throws InterruptedException {
        driver.get("http://localhost:8080/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.typeUserName("admin4");
        loginPage.typePassword("dupa");
        loginPage.clickOnLoginButton();

        loginPage.typeUserName("admin4");
        loginPage.typePassword("dupa");
        loginPage.clickOnLoginButton();

        loginPage.typeUserName("admin4");
        loginPage.typePassword("dupa");
        loginPage.clickOnLoginButton();

        loginPage.typeUserName("admin4");
        loginPage.typePassword("dupa");
        loginPage.clickOnLoginButton();
        assertThat(driver.getPageSource()).contains("Twoje konto zostało zablokowane");
        assertThat(driver.getTitle()).isEqualTo("Strona logowania");

        Thread.sleep(1000*320);

        loginPage.typeUserName("admin4");
        loginPage.typePassword("dupa");
        loginPage.clickOnLoginButton();

        loginPage.typeUserName("admin4");
        loginPage.typePassword("dupa");
        loginPage.clickOnLoginButton();

        loginPage.typeUserName("admin4");
        loginPage.typePassword("dupa");
        loginPage.clickOnLoginButton();

        loginPage.clickOnLoginButton();
        assertThat(driver.getPageSource()).contains("Twoje konto zostało zablokowane");
        assertThat(driver.getTitle()).isEqualTo("Strona logowania");

        Thread.sleep(1000*60*70);

        loginPage.typeUserName("admin4");
        loginPage.typePassword("dupa");
        loginPage.clickOnLoginButton();

        loginPage.typeUserName("admin4");
        loginPage.typePassword("dupa");
        loginPage.clickOnLoginButton();

        loginPage.typeUserName("admin4");
        loginPage.typePassword("dupa");
        loginPage.clickOnLoginButton();

        loginPage.clickOnLoginButton();
        assertThat(driver.getPageSource()).contains("Twoje konto zostało zablokowane");
        assertThat(driver.getTitle()).isEqualTo("Strona logowania");



        driver.quit();
    }
}
