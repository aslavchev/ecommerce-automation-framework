package io.github.aslavchev.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage - Foundation for all Page Object classes
 * <p>
 * Architectural decisions:
 * - All page objects extend this class (DRY principle)
 * - Common wait strategies centralized here (no Thread.sleep in tests)
 * - WebDriver instance passed via constructor (Dependency Injection)
 * - Protected methods allow child classes to use utilities
 */
public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Timeout configuration (single source of truth)
    private static final int DEFAULT_TIMEOUT_SECONDS = 10;


    protected static final String BASE_URL = "https://automationexercise.com";

    /**
     * Constructor - receives WebDriver instance (Dependency Injection pattern)
     *
     * @param driver WebDriver instance from test class
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    /**
     * Wait for element to be visible on the page
     * Handles dynamic content loading, prevents NoSuchElementException
     *
     * @param locator Element locator (By.id, By.cssSelector, etc.)
     * @return WebElement once it's visible
     */
    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable (visible + enabled)
     * Prevents ElementClickInterceptedException when clicking covered elements
     *
     * @param locator Element locator
     * @return WebElement once it's clickable
     */
    protected WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Click element with built-in wait
     * Action methods include waits to keep test code clean
     *
     * @param locator Element to click
     */
    protected void click(By locator) {
        waitForElementClickable(locator).click();
    }

    /**
     * Type text into element with built-in wait
     * Clears field first to ensure clean state
     *
     * @param locator Element to type into
     * @param text    Text to enter
     */
    protected void type(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get text from element with built-in wait
     *
     * @param locator Element to get text from
     * @return Text content of the element
     */
    protected String getText(By locator) {
        return waitForElementVisible(locator).getText();
    }

    /**
     * Check if element is displayed on the page
     * Returns false if element not found (doesn't throw exception)
     *
     * @param locator Element to check
     * @return true if element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Navigate to specific URL
     *
     * @param url URL to navigate to
     */
    protected void navigateTo(String url) {
        driver.get(url);
    }

    /**
     * Get current page URL
     *
     * @return Current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Get current page title
     *
     * @return Page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Removes ad overlay elements from the page using JavaScript
     */
    protected void removeAdOverlays() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "document.querySelectorAll('[id^=\"ftdiv\"], [id^=\"ftAdMarker\"], iframe[src*=\"flashtalking\"], iframe[src*=\"ad\"]')" +
                        ".forEach(e => e.remove());"
        );
    }

    /**
     * Removes Google ad elements from the page using JavaScript
     */
    protected void removeGoogleAds() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript(
                "document.querySelectorAll('[id^=\"aswift_\"], [id^=\"ad_position\"], iframe[src*=\"ads\"], iframe[id^=\"aswift_\"]').forEach(e => e.remove());"
        );
    }
    /**
     * Remove consent popup dialogs
     */
    protected void removeConsentPopup() {
        ((JavascriptExecutor) driver).executeScript(
                "document.querySelectorAll('.fc-dialog, .fc-consent-root, .fc-dialog-overlay').forEach(e => e.remove());"
        );
    }

    /**
     * Scroll element into center of viewport
     *
     * @param element WebElement to scroll into view
     */
    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", element
        );
    }

    /**
     * Click element using JavaScript
     * Bypasses Selenium's visibility checks
     *
     * @param element WebElement to click
     */
    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Gets text of a child element inside a given parent element.
     *
     * @param parent By locator of the container element
     * @param child By locator of the nested element
     * @return Trimmed text value of the nested element
     */
    protected String getNestedText(By parent, By child) {
        WebElement parentEl = waitForElementVisible(parent);
        WebElement childEl = parentEl.findElement(child);
        return childEl.getText().trim();
    }

    /**
     * Returns the trimmed text of a child element at a specific index inside a parent element.
     *
     * @param parent Locator of the parent container
     * @param child Locator of the repeated child elements
     * @param index Zero-based index of the child element
     * @return Trimmed text of the child element
     */
    protected String getNestedTextFromList(By parent, By child, int index) {
        WebElement parentEl = waitForElementVisible(parent);
        return parentEl.findElements(child).get(index).getText().trim();
    }

}
