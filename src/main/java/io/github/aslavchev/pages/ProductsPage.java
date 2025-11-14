package io.github.aslavchev.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductsPage - Page Object for All Products page
 * Supports product browsing, search, and category filtering (Test Cases: 8, 9, 18)
 */
public class ProductsPage extends BasePage {

    // ========== LOCATORS ==========

    // Page verification
    private By allProductsHeadingLocator = By.cssSelector("h2.title.text-center");
    private By searchInputLocator = By.id("search_product");
    private By searchButtonLocator = By.id("submit_search");
    private By productNamesLocator = By.cssSelector(".productinfo.text-center p");

    // Category sidebar locators
    private By categoryHeadingLocator = By.cssSelector(".left-sidebar h2");
    private By womenCategoryLocator = By.cssSelector("a[href='#Women']");
    private By menCategoryLocator = By.cssSelector("a[href='#Men']");
    private By kidsCategoryLocator = By.cssSelector("a[href='#Kids']");

    // Women subcategories (expand after clicking Women)
    private By womenDressSubcategoryLocator = By.cssSelector("a[href='/category_products/1']");
    private By womenTopsSubcategoryLocator = By.cssSelector("a[href='/category_products/2']");

    // Men subcategories
    private By menTshirtsSubcategoryLocator = By.cssSelector("a[href='/category_products/3']");

    /**
     * Constructor - receives WebDriver instance (Dependency Injection pattern)
     *
     * @param driver WebDriver instance from test class
     */
    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigate to products page
     *
     * @return Products page for method chaining
     */
    public ProductsPage navigateProducts() {
        navigateTo(BASE_URL + "/products");  // BasePage method
        waitForElementVisible(allProductsHeadingLocator);
        return this;  // Return this for method chaining
    }

    public boolean isAllProductsPageDisplayed() {
        return isElementDisplayed(allProductsHeadingLocator)
                && getText(allProductsHeadingLocator).contains("ALL PRODUCTS");
    }

    /**
     * Search for product by Name
     *
     * @param productName The name of the product we are searching
     */
    public void searchForProduct(String productName) {
        type(searchInputLocator, productName);
        click(searchButtonLocator);
        waitForElementVisible(allProductsHeadingLocator);
    }

    public boolean isSearchedProductsHeadingDisplayed() {
        return isElementDisplayed(allProductsHeadingLocator)
                && getText(allProductsHeadingLocator).contains("SEARCHED PRODUCTS");
    }

    /**
     * Get all product names from the current page
     *
     * @return List of product names as strings
     */
    public List<String> getProductNames() {
        List<WebElement> productNames = driver.findElements(productNamesLocator);
        return productNames.stream().map(WebElement::getText).collect(Collectors.toList());
    }


    public boolean doSearchResultsContain(String searchTerm) {
        List<String> productNames = getProductNames();
        return productNames.stream()
                .anyMatch(name -> name.toLowerCase().contains(searchTerm.toLowerCase()));
    }

    /**
     * Check if category sidebar is visible
     */
    public boolean isCategorySidebarVisible() {
        return isElementDisplayed(categoryHeadingLocator);
    }

    /**
     * Click on Women category in sidebar
     */
    public void clickWomenCategory() {
        click(womenCategoryLocator);
    }

    /**
     * Click on Men category in sidebar
     */
    public void clickMenCategory() {
        click(menCategoryLocator);
    }

    /**
     * Click on Kids category in sidebar
     */
    public void clickKidsCategory() {
        click(kidsCategoryLocator);
    }

    /**
     * Click on Women > Dress subcategory
     */
    public void clickWomenDressSubcategory() {
        clickWomenCategory();  // Expand Women category first
        click(womenDressSubcategoryLocator);
        waitForElementVisible(allProductsHeadingLocator);
    }

    /**
     * Click on Men > Tshirts subcategory
     */
    public void clickMenTshirtsSubcategory() {
        clickMenCategory();
        click(menTshirtsSubcategoryLocator);
        waitForElementVisible(allProductsHeadingLocator);
    }

    /**
     * Get the current category page heading
     */
    public String getCategoryHeading() {
        return getText(allProductsHeadingLocator);
    }

    /**
     * Verify category page is displayed with specific text
     */
    public boolean isCategoryPageDisplayed(String expectedCategory) {
        String heading = getCategoryHeading();
        return heading.toUpperCase().contains(expectedCategory.toUpperCase());
    }
}