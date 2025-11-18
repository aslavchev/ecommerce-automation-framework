package io.github.aslavchev.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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

}
