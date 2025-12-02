package io.github.aslavchev.api;

import io.github.aslavchev.api.base.BaseAPITest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.path.json.JsonPath.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * API tests for Product-related endpoints.
 * Validates product catalog retrieval, search, and data structure.
 */
public class ProductAPITests extends BaseAPITest {
    @Test(groups = {"api", "regression", "smoke"})
    public void testGetAllProducts() {
        // Get response
        Response response =
            given()
                .log().all()
            .when()
                .get("/productsList");

        // Extract JSON from HTML body
        String responseBody = response.getBody().asString();

        // Validate status and JSON content
        assertThat("Status code", response.getStatusCode(), is(200));
        assertThat("Response contains JSON", responseBody, containsString("{"));
        assertThat("Response code is 200", from(responseBody).getInt("responseCode"), is(200));
        assertThat("Products exist", from(responseBody).getList("products"), is(notNullValue()));
        assertThat("Products not empty", from(responseBody).getList("products").size(), greaterThan(0));
    }
}
