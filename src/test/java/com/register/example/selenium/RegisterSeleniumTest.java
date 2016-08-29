package com.register.example.selenium;

import com.register.example.selenium.configuration.ScreenshotTestRule;
import com.register.example.selenium.configuration.SeleniumTestBase;
import com.register.example.selenium.pageObjects.LoginPage;
import com.register.example.selenium.pageObjects.NonAuthenticatedNavigation;
import com.register.example.selenium.pageObjects.RegisterPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.context.annotation.Profile;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Profile("!test")
public class RegisterSeleniumTest extends SeleniumTestBase {

    public static WebDriver driver;

    @Rule
    public ScreenshotTestRule screenshotTestRule = new ScreenshotTestRule(driver);

    @BeforeClass
    public static void setUp() {
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
    }

    @AfterClass
    public static void tearDown(){
        driver.quit();
    }

    @Test
    public void shouldRegisterWithCorrectData() {
        driver.get("http://localhost:8080/register");
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.typeName("userTest");
        registerPage.typeLastName("userTest");
        registerPage.typeEmail("userTestXXX@poczta.pl");
        registerPage.typeLogin("userTestXXX");
        registerPage.typePassword("zaq1@WSX");
        registerPage.typeConfirmPassword("zaq1@WSX");
        registerPage.clickOnRegisterButton();
        System.out.println(driver.getPageSource() + "\n////////////////////////////////////");
        System.out.println(driver.getTitle() + "\n////////////////////////////////////");
        assertThat(driver.getPageSource()).contains("Twoje konto zostalo stworzone");
        assertThat(driver.getTitle()).isEqualTo("Zarejestruj się");

        driver.get("http://localhost:8080/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.typeUserName("userTest");
        loginPage.typePassword("zaq1@WSX");
        loginPage.clickOnLoginButton();
        System.out.println(driver.getPageSource() + "\n////////////////////////////////////");
        System.out.println(driver.getTitle() + "\n////////////////////////////////////");
        assertThat(driver.getPageSource()).contains(" <span>Nieprawidłowy użytkownik lub hasło</span>");
    }


    @Test
    public void shouldRegisterFailedWithExistingAccount() {
        driver.get("http://localhost:8080/register");
        RegisterPage registerPage = new RegisterPage(driver);
        registerPage.typeName("user");
        registerPage.typeLastName("user");
        registerPage.typeEmail("user@poczta.pl");
        registerPage.typeLogin("user");
        registerPage.typePassword("zaQ1");
        registerPage.typeConfirmPassword("zaQ1");
        registerPage.clickOnRegisterButton();
        System.out.println(driver.getPageSource() + "\n////////////////////////////////////");
        System.out.println(driver.getTitle() + "\n////////////////////////////////////");
        assertThat(driver.getPageSource()).contains("Podany email lub login jest juz wykorzystany");
    }

    @Test
    public void shouldActivationFailedWithNotExistingToken() {
        driver.get("http://localhost:8080/register/registrationConfirm?token=dupa");
        System.out.println(driver.getPageSource() + "\n////////////////////////////////////");
        System.out.println(driver.getTitle() + "\n////////////////////////////////////");
        assertThat(driver.getPageSource()).contains("Bledny link weryfikacyjny");
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
    }

}
