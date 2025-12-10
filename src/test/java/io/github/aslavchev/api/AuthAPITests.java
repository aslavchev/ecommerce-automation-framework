package io.github.aslavchev.api;

import io.github.aslavchev.api.base.BaseAPITest;
import io.github.aslavchev.utils.TestConfig;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * API tests for Authentication endpoints.
 * Validates login verification with valid/invalid credentials and parameter validation.
 */
@Listeners(io.github.aslavchev.listeners.RetryListener.class)
public class AuthAPITests extends BaseAPITest {

    @Test(groups = {"api", "regression", "smoke"})
    @Description("API-7: Verify login with valid credentials")
    public void loginWithValidCredentialsReturnsSuccess() {
        // Arrange
        String email = TestConfig.email();
        String password = TestConfig.password();

        // Act
        Response response = APIHelper.verifyLogin(email, password);

        String responseBody = response.getBody().asString();

        // Assert
        assertThat("Status code", response.getStatusCode(), is(200));
        assertThat("Response code is 200", from(responseBody).getInt("responseCode"), is(200));
        assertThat("Success message exists", from(responseBody).getString("message"), is(notNullValue()));
        assertThat("Success message confirms user exists",
            from(responseBody).getString("message"),
            containsString("User exists!"));

        System.out.println("✅ Login verification successful for valid credentials");
    }

    @Test(groups = {"api", "regression"})
    @Description("API-10: Verify login with invalid credentials")
    public void loginWithInvalidCredentialsReturnsUserNotFound() {
        // Arrange
        String invalidEmail = "invalid@example.com";
        String invalidPassword = "wrongpassword";

        // Act
        Response response = APIHelper.verifyLogin(invalidEmail,invalidPassword);

        String responseBody = response.getBody().asString();

        // Assert
        assertThat("Status code", response.getStatusCode(), is(200));
        assertThat("Response code is 404", from(responseBody).getInt("responseCode"), is(404));
        assertThat("Error message exists", from(responseBody).getString("message"), is(notNullValue()));
        assertThat("Error message indicates user not found",
            from(responseBody).getString("message"),
            containsString("User not found!"));

        System.out.println("✅ API correctly handles invalid credentials");
    }

    @Test(groups = {"api", "regression"})
    @Description("API-8: Verify login without email parameter")
    public void loginWithoutEmailParameterReturns400() {
        // Arrange
        String password = TestConfig.password();

        // Act
        Response response =
            given()
                .log().all()
                .formParam("password", password)
            .when()
                .post("/verifyLogin");

        String responseBody = response.getBody().asString();

        // Assert
        assertThat("Status code", response.getStatusCode(), is(200));
        assertThat("Response code is 400", from(responseBody).getInt("responseCode"), is(400));
        assertThat("Error message exists", from(responseBody).getString("message"), is(notNullValue()));
        assertThat("Error message mentions missing parameter",
            from(responseBody).getString("message"),
            containsString("email or password parameter is missing"));

        System.out.println("✅ API correctly validates missing email parameter");
    }
}
