package io.github.aslavchev.api;

import io.github.aslavchev.utils.TestDataReader;
import org.testng.annotations.DataProvider;
import io.github.aslavchev.api.base.BaseAPITest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;

import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * API tests for Search-related endpoints.
 * Validates product search functionality and parameter validation.
 */
public class SearchAPITests extends BaseAPITest {

    @Test(groups = {"api", "regression", "smoke"})
    public void testSearchProductValid() {
        // Arrange
        String searchTerm = "tshirt";

        // Act
        Response response = APIHelper.searchProduct(searchTerm);

        String responseBody = response.getBody().asString();

        // Assert
        assertThat("Status code", response.getStatusCode(), is(200));
        assertThat("Response code is 200", from(responseBody).getInt("responseCode"), is(200));
        assertThat("Products exist", from(responseBody).getList("products"), is(notNullValue()));
        assertThat("Products not empty", from(responseBody).getList("products").size(), greaterThan(0));

        // Verify all returned products have required fields
        from(responseBody).getList("products", Map.class).forEach(product -> {
            assertThat("Product has name", product.get("name"), is(notNullValue()));
            assertThat("Product has id", product.get("id"), is(notNullValue()));
            assertThat("Product has price", product.get("price"), is(notNullValue()));
        });

        System.out.println("✅ Search returned " + from(responseBody).getList("products").size() + " matching products");
    }

    @Test(groups = {"api", "regression"})
    public void testSearchProductMissingParameter() {
        // Arrange - No parameters to send

        // Act
        Response response = APIHelper.searchProductNoParams();

        String responseBody = response.getBody().asString();

        // Assert
        assertThat("Status code", response.getStatusCode(), is(200));
        assertThat("Response code is 400", from(responseBody).getInt("responseCode"), is(400));
        assertThat("Error message exists", from(responseBody).getString("message"), is(notNullValue()));
        assertThat("Error message mentions missing parameter",
                from(responseBody).getString("message"),
                containsString("search_product parameter is missing"));

        System.out.println("✅ API correctly validates missing search_product parameter");
    }

    @DataProvider(name = "searchTerms")
    public Object[][] getSearchTerms() {
        return TestDataReader.readSimpleTestData("search-terms.csv");
    }

    @Test(dataProvider = "searchTerms", groups = {"api", "regression"})
    public void testSearchProductDataDriven(Map<String, String> data) {
        // Arrange
        String searchTerm = data.get("searchTerm");

        // Act
        Response response = APIHelper.searchProduct(searchTerm);
        String responseBody = response.getBody().asString();

        // Assert
        assertThat("Status code", response.getStatusCode(), is(200));
        assertThat("Response code is 200", from(responseBody).getInt("responseCode"), is(200));
        assertThat("Products exist", from(responseBody).getList("products"), is(notNullValue()));
        assertThat("Products not empty", from(responseBody).getList("products").size(), greaterThan(0));

        System.out.println("✅ Search for '" + searchTerm + "' returned " +
                from(responseBody).getList("products").size() + " products");
    }
}
