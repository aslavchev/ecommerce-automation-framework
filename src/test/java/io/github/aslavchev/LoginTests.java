package io.github.aslavchev;

import io.github.aslavchev.base.BaseTest;
import io.github.aslavchev.pages.LoginPage;
import io.qameta.allure.Description;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class LoginTests extends BaseTest {

    @Test
    @Description("Verify successful login displays correct username")
    public void testValidLogin() {
        // Arrange
        String email = System.getenv("TEST_USER_EMAIL");
        String password = System.getenv("TEST_USER_PASSWORD");

        if (email == null || password == null) {
            throw new IllegalStateException("TEST_USER_EMAIL and TEST_USER_PASSWORD env vars required");
        }

        LoginPage loginPage = new LoginPage(driver);

        //Act
        loginPage.navigateToLogin();
        loginPage.login(email, password);
        loginPage.waitForLoginSuccess();

        //Assert
        assertTrue(loginPage.isLoggedIn(), "User should be logged in - 'Logged in as' text should be visible");
        assertEquals(loginPage.getLoggedInUsername(), "asl", "Logged in username should be 'asl'");
    }

    @Test
    @Description("Verify error message for invalid credentials")
    public void testInvalidLogin() {
        // Arrange
        String email = System.getenv("TEST_USER_EMAIL");
        if (email == null) {
            throw new IllegalStateException("TEST_USER_EMAIL env var required");
        }

        LoginPage loginPage = new LoginPage(driver);

        //Act
        loginPage.navigateToLogin();
        loginPage.login(email, "wrongpassword");
        loginPage.waitForErrorMessage();

        //Assert
        assertTrue(loginPage.isErrorMessageDisplayed());
        assertEquals(loginPage.getErrorMessage(), "Your email or password is incorrect!");
        assertFalse(loginPage.isLoggedIn());
    }
}
