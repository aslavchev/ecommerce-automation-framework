package io.github.aslavchev.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


/**
 * LoginPage - Page Object for Login/Signup page
 * Provides methods to interact with login form and verify login state.
 */
public class LoginPage extends BasePage {
    // Locators
    private By emailField = By.cssSelector("input[data-qa='login-email']");
    private By passwordField = By.cssSelector("input[data-qa='login-password']");
    private By loginButton = By.cssSelector("button[data-qa='login-button']");

    // Success indicators
    private By loggedInText = By.xpath("//a[contains(text(), 'Logged in as')]");
    private By loggedInUsername = By.xpath("//a[contains(text(), 'Logged in as')]/b");

    // Error indicators
    private By errorMessage =  By.xpath("//p[normalize-space()='Your email or password is incorrect!']");


    private static final String LOGIN_URL = "https://automationexercise.com/login";

    /**
     * Constructor - receives WebDriver instance (Dependency Injection pattern)
     *
     * @param driver WebDriver instance from test class
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }


    // Methods
    /**
     * Navigate to login page
     * @return LoginPage for method chaining
     */
    public LoginPage navigateToLogin() {
        navigateTo(LOGIN_URL);  // BasePage method
        return this;  // Return this for method chaining
    }

    /**
     * Perform login with provided credentials
     * @param email User email
     * @param password User password
     */
    public void login(String email, String password) {
        type(emailField, email);
        type(passwordField, password);
        click(loginButton);
    }

    public boolean isLoggedIn() {
        return isElementDisplayed(loggedInText);
    }

    public String getLoggedInUsername() {
        return getText(loggedInUsername);
    }

    /**
     * Wait for successful login completion
     * Call after login() for positive scenarios
     */
    public void waitForLoginSuccess() {
        waitForElementVisible(loggedInText);  // Uses BasePage wait method
    }

    /**
     * Wait for error message after failed login
     * Call after login() for negative scenarios
     */
    public void waitForErrorMessage() {
        waitForElementVisible(errorMessage);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }

}
