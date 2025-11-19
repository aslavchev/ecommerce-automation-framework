package io.github.aslavchev.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * CartPage - Page Object for Shopping Cart page
 * Supports cart viewing, quantity verification, and item removal (Test Cases: 12, 13, 17)
 */
public class CartPage extends BasePage {
    // ========== LOCATORS ==========
    private By cartTableLocator = By.id("cart_info_table");
    private By cartItemsLocator = By.cssSelector("#cart_info_table tbody tr");
    private By emptyCartMessageLocator = By.id("empty_cart");


    // Cart item details
    private By productNameLocator = By.cssSelector(".cart_description h4 a");
    private By productQuantityLocator = By.cssSelector(".cart_quantity button");
    private By deleteButtonLocator = By.cssSelector(".cart_quantity_delete");
    private By productPriceLocator = By.cssSelector(".cart_price p");
    private By productTotalLocator = By.cssSelector(".cart_total p");


    // Checkout


    /**
     * Constructor - receives WebDriver instance (Dependency Injection pattern)
     *
     * @param driver WebDriver instance from test class
     */
    public CartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate directly to cart page
     * Why: Direct navigation is faster and more reliable than clicking through UI
     */
    public void navigateToCart() {
        navigateTo(BASE_URL + "/view_cart");
    }


    /**
     * Get number of items in cart
     * Why: TC-12 verifies items were added by checking count > 0
     */
    public int getCartItemCount() {
        try {
            return driver.findElements(cartItemsLocator).size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get quantity of first product
     * Why: TC-13 needs to verify quantity = 4
     */
    public String getFirstProductQuantity() {
        return getText(productQuantityLocator);
    }

    /**
     * Remove first product from cart
     */
    public void removeFirstProduct() {

        int initialCount = getCartItemCount();
        click(deleteButtonLocator);

        // Wait for count to decrease
        wait.until(driver -> getCartItemCount() < initialCount);
    }

    /**
     * Check if cart is empty
     *
     * @return true if cart has no items
     */
    public boolean isCartEmpty() {
        return getCartItemCount() == 0;
    }

    /**
     * Get first product name in cart
     *
     * @return Product name
     */
    public String getFirstProductName() {
        return getText(productNameLocator);
    }

    /**
     * Get all product names in cart
     * @return List of product names
     */
    public List<String> getProductNames(){
        List<WebElement> elements = driver.findElements(productNameLocator);
        List<String > names = new ArrayList<>();
        for (WebElement element : elements) {
            names.add(element.getText());
        }
        return names;
    }

    /**
     * Get all product prices in cart
     * @return List of prices (e.g. "RS. 500")
     */
    public List<String> getProductPrices(){
        List<WebElement> elements = driver.findElements(productPriceLocator);
        List<String > prices = new ArrayList<>();
        for (WebElement element : elements) {
            prices.add(element.getText());
        }
        return prices;
    }

    /**
     * Get all product quantities in cart
     *
     * @return List of quantities
     */
    public List<String> getProductQuantities() {
        List<WebElement> elements = driver.findElements(productQuantityLocator);
        List<String> quantities = new ArrayList<>();
        for (WebElement element : elements) {
            quantities.add(element.getText());
        }
        return quantities;
    }

    /**
     * Get all product totals in cart
     *
     * @return List of totals
     */
    public List<String> getProductTotals() {
        List<WebElement> elements = driver.findElements(productTotalLocator);
        List<String> totals = new ArrayList<>();
        for (WebElement element : elements) {
            totals.add(element.getText());
        }
        return totals;
    }
}
