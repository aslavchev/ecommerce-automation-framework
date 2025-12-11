package io.github.aslavchev.data;

import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ProductDataProvider - Centralized product data for test suite
 * Provides shared product catalog across multiple test classes
 */
public class ProductDataProvider {

    private static final String PRODUCTS_CSV = "src/test/resources/testdata/products.csv";

    /**
     * Product catalog as Map for easy lookup
     * Key: Product name, Value: Product price
     */
    private static Map<String, String> productCatalog;

    /**
     * Load product catalog from CSV (lazy initialization)
     */
    private static void loadProductCatalog() {
        if (productCatalog != null) return;

        productCatalog = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUCTS_CSV))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    productCatalog.put(parts[0], parts[1]); // name -> price
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load product catalog: " + PRODUCTS_CSV, e);
        }
    }

    /**
     * Get product price by name
     * @param productName Product name
     * @return Product price (e.g., "Rs. 500")
     */
    public static String getProductPrice(String productName) {
        loadProductCatalog();
        String price = productCatalog.get(productName);
        if (price == null) {
            throw new IllegalArgumentException("Product not found in catalog: " + productName);
        }
        return price;
    }

    /**
     * DataProvider for CartTests: Product pairs
     * Returns: testName, product1, product2
     */
    @DataProvider(name = "productPairs")
    public static Object[][] getProductPairs() {
        return new Object[][] {
            { "Blue Top and Men Tshirt", "Blue Top", "Men Tshirt" },
            { "Blue Top and Sleeveless Dress", "Blue Top", "Sleeveless Dress" },
            { "Men Tshirt and Winter Top", "Men Tshirt", "Winter Top" }
        };
    }
}
