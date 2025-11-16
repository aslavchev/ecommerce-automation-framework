package io.github.aslavchev.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


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

    /**
     * Constructor - receives WebDriver instance (Dependency Injection pattern)
     *
     * @param driver WebDriver instance from test class
     */
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductDetailsVisible() {
        return isElementDisplayed(productNameLocator) && isElementDisplayed(productPriceLocator);
    }

    public String getProductName() {
        return getText(productNameLocator);
    }

    public String getProductPrice() {
        return getText(productPriceLocator);
    }
}
