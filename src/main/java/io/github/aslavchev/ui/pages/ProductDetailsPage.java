package io.github.aslavchev.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * ProductDetailsPage - Page Object for Product Details page
 * Displays individual product information (Test Case 8)
 */
public class ProductDetailsPage extends BasePage {
    //Locators
    private By productNameLocator = By.cssSelector(".product-information h2");
    private By productPriceLocator = By.cssSelector(".product-information span span");
    private By productAvailabilityLocator = By.cssSelector(".product-information p:nth-of-type(2)");
    private By productConditionLocator = By.cssSelector(".product-information p:nth-of-type(3)");
    private By productBrandLocator = By.cssSelector(".product-information p:nth-of-type(4)");

    private By productQuantityLocator = By.id("quantity");
    private By addToCartButtonLocator = By.cssSelector("button.cart");
    private By viewCartModalLinkLocator = By.linkText("View Cart");

    /**
     * Constructor - receives WebDriver instance (Dependency Injection pattern)
     *
     * @param driver WebDriver instance from test class
     */
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductDetailsVisible() {
        // Wait for product details to load
        waitForElementVisible(productNameLocator);
        return isElementDisplayed(productNameLocator) && isElementDisplayed(productPriceLocator);
    }

    public String getProductName() {
        return getText(productNameLocator);
    }

    public String getProductPrice() {
        return getText(productPriceLocator);
    }

    /**
     * Set product quantity
     *
     * @param quantity Number of items to add
     */
    public void setQuantity(int quantity) {
        removeAdOverlays();
        removeGoogleAds();
        removeConsentPopup();
        type(productQuantityLocator,String.valueOf(quantity));
    }

    /**
     * Click Add to Cart button
     */
    public void addToCart() {
        click(addToCartButtonLocator);
    }

    /**
     * Add product to cart and navigate to cart page
     *
     * @return CartPage instance
     */
    public CartPage addToCartAndViewCart() {
        addToCart();
        click(viewCartModalLinkLocator);
        return new CartPage(driver);
    }
}
