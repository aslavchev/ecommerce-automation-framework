package io.github.aslavchev.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderConfirmationPage extends BasePage {

    // ===== LOCATORS =====
    private By orderPlacedHeader = By.cssSelector("[data-qa='order-placed']");
    private By confirmationMessage = By.cssSelector(".title + p"); // the <p> under header
    private By downloadInvoiceButton = By.cssSelector("a[href^='/download_invoice/']");
    private By continueButton = By.cssSelector("[data-qa='continue-button']");

    /**
     * Constructor - receives WebDriver instance (Dependency Injection pattern)
     *
     * @param driver WebDriver instance from test class
     */
    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
    }


    // ===== GETTERS / ASSERTIONS =====
    public String getOrderPlacedHeader() {
        return waitForElementVisible(orderPlacedHeader).getText().trim();
    }

    public String getConfirmationMessage() {
        return waitForElementVisible(confirmationMessage).getText().trim();
    }

    public boolean isOrderPlacedHeaderVisible() {
        return isElementDisplayed(orderPlacedHeader);
    }

    public boolean isConfirmationMessageVisible() {
        return isElementDisplayed(confirmationMessage);
    }

    public boolean isDownloadInvoiceButtonVisible() {
        return isElementDisplayed(downloadInvoiceButton);
    }

    public boolean isContinueButtonVisible() {
        return isElementDisplayed(continueButton);
    }
}
