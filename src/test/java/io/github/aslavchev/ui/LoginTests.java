package io.github.aslavchev.ui;

import io.github.aslavchev.ui.pages.LoginPage;
import io.github.aslavchev.utils.TestConfig;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class LoginTests extends BaseTest {

    @Test(groups = {"smoke", "regression", "critical", "ui"})
    @Description("Verify successful login displays correct username")
    public void validLoginDisplaysUsername() {
        // Arrange
        String email = TestConfig.email();
        String password = TestConfig.password();

        // Act
        LoginPage loginPage = new LoginPage(driver)
                .navigateToLogin()
                .login(email, password)
                .waitForLoginSuccess();

        // Assert
        assertTrue(loginPage.isLoggedIn());
        assertEquals(loginPage.getLoggedInUsername(), "asl");
    }

    @Test(groups = {"regression", "ui"})
    @Description("Verify error message for invalid credentials")
    public void invalidLoginShowsErrorMessage() {
        // Arrange
        String email = TestConfig.email();

        // Act
        LoginPage loginPage = new LoginPage(driver)
                .navigateToLogin()
                .login(email, "wrongpassword123")
                .waitForErrorMessage();

        // Assert
        assertTrue(loginPage.isErrorMessageDisplayed());
        assertEquals(loginPage.getErrorMessage(), "Your email or password is incorrect!");
        assertFalse(loginPage.isLoggedIn());
    }
}
