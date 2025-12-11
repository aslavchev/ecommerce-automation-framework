package io.github.aslavchev.api;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.testng.annotations.BeforeClass;

/**
 * Base class for all API tests using REST Assured.
 * Configures base URI, path, and default JSON parser.
 */
public class BaseAPITest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://automationexercise.com";
        RestAssured.basePath = "/api";
        // Force REST Assured to parse text/html responses as JSON
        RestAssured.defaultParser = Parser.JSON;
    }
}
