package io.github.aslavchev.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
    private By viewProductButtonsLocator = By.cssSelector("a[href*='/product_details/']");

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

    // Add to cart functionality (hover overlay)
    private By productCardLocator = By.cssSelector(".product-image-wrapper");
    private By addToCartButtonLocator = By.cssSelector("a.add-to-cart");
    private By continueShoppingButtonLocator = By.cssSelector(".modal-footer .btn-success");
    private By viewCartModalLinkLocator = By.cssSelector(".modal-content a[href='/view_cart']");

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

    /**
     * Opens the first product's details page
     *
     * @return ProductDetailsPage instance for the opened product
     */
    public ProductDetailsPage openFirstProductDetails() {
        removeAdOverlays();         // 1. remove ads
        removeGoogleAds();          // 2. remove google ads

        List<WebElement> viewButtons = driver.findElements(viewProductButtonsLocator);
        if (!viewButtons.isEmpty()) {
            viewButtons.get(0).click();
        }
        return new ProductDetailsPage(driver);
    }

    /**
     * Add product to cart by index (0-based)
     * Why: TC-12 adds products from the products page using hover action
     * <p>
     * Implementation notes:
     * - Must hover over product card to reveal the Add to Cart overlay
     * - Uses card.findElement() to search WITHIN that specific card
     * - This ensures clicking the correct button when adding 7th product (index=6)
     */
    public void addProductToCartByIndex(int index) {
        removeAdOverlays();
        removeGoogleAds();
        removeConsentPopup();

        List<WebElement> productCards = driver.findElements(productCardLocator);
        if (index < productCards.size()) {
            WebElement card = productCards.get(index);

            // Scroll card into view first
            scrollIntoView(card);

            // Hover over this specific card to reveal overlay
            Actions actions = new Actions(driver);
            actions.moveToElement(card).perform();

            // Find add button WITHIN this card's context and use JS click
            WebElement addButton = card.findElement(addToCartButtonLocator);
            jsClick(addButton);
        }
    }

    /**
     * Add first product to cart (convenience method)
     * Why: Keeps test code clean for common case
     */
    public void addFirstProductToCart() {
        addProductToCartByIndex(0);

    }

    public void clickContinueShopping() {
        click(continueShoppingButtonLocator);
    }

    /**
     * Click View Cart in modal
     * Why: TC-12 clicks View Cart after adding products
     * Returns: CartPage for fluent chaining
     */
    public CartPage clickViewCart() {

        // Wait for modal to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".modal-content a[href='/view_cart']")));

        click(viewCartModalLinkLocator);
        return new CartPage(driver);
    }
}