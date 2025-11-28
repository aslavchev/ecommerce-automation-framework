package io.github.aslavchev.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TestDataReader - Utility class for reading test data from CSV files
 * Used by TestNG DataProviders to externalize test data
 */
public class TestDataReader {

    /**
     * Reads checkout payment data from CSV file and enriches with product prices
     * CSV columns: testName, productName, cardName, cardNumber, cvc, expiryMonth, expiryYear
     * Returns: testName, productName, productPrice, cardName, cardNumber, cvc, expiryMonth, expiryYear (8 params)
     *
     * @param csvFileName Name of CSV file in src/test/resources/testdata/
     * @param productPriceLookup Function to get price for product name
     * @return 2D Object array for TestNG DataProvider
     * @throws RuntimeException if file not found or parsing fails
     */
    public static Object[][] readCheckoutPaymentData(String csvFileName, java.util.function.Function<String, String> productPriceLookup) {
        String filePath = "src/test/resources/testdata/" + csvFileName;
        List<Object[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header row
            br.readLine();

            // Read data rows
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 7) {  // testName, productName, cardName, cardNumber, cvc, expiryMonth, expiryYear
                    String testName = values[0];
                    String productName = values[1];
                    String productPrice = productPriceLookup.apply(productName);  // Lookup price from catalog
                    String cardName = values[2];
                    String cardNumber = values[3];
                    String cvc = values[4];
                    String expiryMonth = values[5];
                    String expiryYear = values[6];

                    // Build row with 8 params (insert productPrice)
                    rows.add(new Object[]{testName, productName, productPrice, cardName, cardNumber, cvc, expiryMonth, expiryYear});
                } else {
                    throw new IllegalArgumentException(
                        "Invalid CSV row (expected 7 columns): " + line
                    );
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }

        // Convert List to 2D Object array
        return rows.toArray(new Object[0][]);
    }
}
