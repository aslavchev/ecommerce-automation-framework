package io.github.aslavchev.api;



import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class APIHelper {
    public static Response getAllProducts() {
        return given()
                    .log().all()
                .when()
                    .get("/productsList");
    }

    public static Response searchProduct(String searchTerm) {
        return given()
                    .formParam("search_product", searchTerm)
                .when()
                    .post("/searchProduct");
    }
    public static Response searchProductNoParams() {
        return given()
                .log().all()
                .when()
                .post("/searchProduct");
    }
    public static Response verifyLogin(String email, String password) {
        return given()
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("/verifyLogin");
    }
    public static Response getAllBrands() {
        return given()
                .log().all()
                .when()
                .get("/brandsList");
    }
}
