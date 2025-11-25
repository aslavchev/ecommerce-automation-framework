package io.github.aslavchev.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage{
    // -------------------- LOCATORS --------------------
    private By nameOnCardInput = By.cssSelector("input[data-qa='name-on-card']");
    private By cardNumberInput = By.cssSelector("input[data-qa='card-number']");
    private By cvcInput = By.cssSelector("input[data-qa='cvc']");
    private By expirationMonthInput = By.cssSelector("input[data-qa='expiry-month']");
    private By expirationYearInput = By.cssSelector("input[data-qa='expiry-year']");
    private By payAndConfirmButton = By.cssSelector("button[data-qa='pay-button']");

    private By successMessageLocator = By.cssSelector("#success_message .alert-success");


    /**
     * Constructor - receives WebDriver instance (Dependency Injection pattern)
     *
     * @param driver WebDriver instance from test class
     */
    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enter the cardholder's name.
     */
    public void enterNameOnCard(String name) {
        type(nameOnCardInput, name);
    }

    /**
     * Enter the card number.
     */
    public void enterCardNumber(String number) {
        type(cardNumberInput, number);
    }

    /**
     * Enter the CVC security code.
     */
    public void enterCvc(String cvc) {
        type(cvcInput, cvc);
    }

    /**
     * Enter expiration month.
     */
    public void enterExpiryMonth(String month) {
        type(expirationMonthInput, month);
    }

    /**
     * Enter expiration year.
     */
    public void enterExpiryYear(String year) {
        type(expirationYearInput, year);
    }
    // -------------------- COMBINED ACTION --------------------


    /**
     * Convenience method to enter all payment details in one call.
     * Recommended for positive, clean-flow tests.
     */
    public PaymentPage  enterPaymentDetails(String name, String number, String cvc, String month, String year) {
        enterNameOnCard(name);
        enterCardNumber(number);
        enterCvc(cvc);
        enterExpiryMonth(month);
        enterExpiryYear(year);
        return this;
    }

    // -------------------- ACTIONS --------------------

    /**
     * Clicks the "Pay and Confirm Order" button.
     */
    public OrderConfirmationPage clickPayAndConfirm() {
        click(payAndConfirmButton);
        return new OrderConfirmationPage(driver);
    }

    public String getSuccessMessage() {
        return waitForElementVisible(successMessageLocator).getText().trim();
    }

    public boolean isOrderSuccessMessageVisible() {
        return isElementDisplayed(successMessageLocator);
    }
}
