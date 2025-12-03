package io.github.aslavchev.api;

import io.github.aslavchev.api.base.BaseAPITest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * API tests for Product-related endpoints.
 * Validates product catalog retrieval, search, and data structure.
 */
public class ProductAPITests extends BaseAPITest {

    @Test(groups = {"api", "regression", "smoke"})
    public void testGetAllProducts() {
        // Arrange - No parameters needed for GET

        // Act
        Response response = APIHelper.getAllProducts();

        // Extract JSON from HTML body
        String responseBody = response.getBody().asString();

        // Assert
        assertThat("Status code", response.getStatusCode(), is(200));
        assertThat("Response code is 200", from(responseBody).getInt("responseCode"), is(200));
        assertThat("Products exist", from(responseBody).getList("products"), is(notNullValue()));
        assertThat("Products not empty", from(responseBody).getList("products").size(), greaterThan(0));

        System.out.println("✅ API returned " + from(responseBody).getList("products").size() + " products");
    }

    @Test(groups = {"api", "regression"})
    public void testGetAllBrands() {
        // Arrange - No parameters needed for GET

        // Act
        Response response = APIHelper.getAllBrands();

        // Extract JSON from HTML body
        String responseBody = response.getBody().asString();

        // Assert
        assertThat("Status code", response.getStatusCode(), is(200));
        assertThat("Response code is 200", from(responseBody).getInt("responseCode"), is(200));
        assertThat("Brands exist", from(responseBody).getList("brands"), is(notNullValue()));
        assertThat("Brands not empty", from(responseBody).getList("brands").size(), greaterThan(0));

        System.out.println("✅ API returned " + from(responseBody).getList("brands").size() + " brands");
    }
}
