package io.github.aslavchev.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CheckoutPage extends BasePage{

    // -------------------- CONTAINERS --------------------
    private By deliveryContainer = By.id("address_delivery");
    private By billingContainer = By.id("address_invoice");

    // -------------------- ADDRESS FIELD LOCATORS -------------------
    private By fullName = By.cssSelector(".address_firstname.address_lastname");
    private By streetRows = By.cssSelector(".address_address1.address_address2");
    private By cityStatePostcode  = By.cssSelector(".address_city.address_state_name.address_postcode");
    private By country = By.cssSelector(".address_country_name");
    private By phone = By.cssSelector(".address_phone");

    // ===== REVIEW ORDER LOCATORS =====
    private By productRows = By.cssSelector("#cart_info tbody tr[id^='product-']");
    private By productName = By.cssSelector(".cart_description h4 a");
    private By productPrice = By.cssSelector(".cart_price p");
    private By productQuantity = By.cssSelector(".cart_quantity button");
    private By productTotal = By.cssSelector(".cart_total_price");

    private By totalAmount = By.xpath("//td[contains(.,'Total Amount')]/following-sibling::td/p");


    // -------------------- ACTION LOCATORS --------------------
    private By commentTextArea = By.cssSelector("#ordermsg textarea[name='message']");
    private By placeOrderButton = By.cssSelector("a.btn.check_out[href='/payment']");


    /**
     * Constructor - receives WebDriver instance (Dependency Injection pattern)
     * Verifies checkout page has loaded by waiting for delivery address container
     *
     * @param driver WebDriver instance from test class
     */
    public CheckoutPage(WebDriver driver) {
        super(driver);
        waitForElementVisible(deliveryContainer);
    }

    // -------------------- DELIVERY ADDRESS GETTERS --------------------
    /**
     * Get the full name from the delivery address section.
     * @return Full name as displayed on checkout page
     */
    public String getDeliveryFullName(){
        return getNestedText(deliveryContainer,fullName);
    }
    public String getDeliveryStreet() {
        return getNestedTextFromList(deliveryContainer, streetRows, 1);
    }

    public String getDeliveryCityStatePostcode() {
        return getNestedText(deliveryContainer, cityStatePostcode);
    }

    public String getDeliveryCountry() {
        return getNestedText(deliveryContainer, country);
    }

    public String getDeliveryPhone() {
        return getNestedText(deliveryContainer, phone);
    }

    // -------------------- BILLING ADDRESS GETTERS --------------------
    public String getBillingFullName() {
        return getNestedText(billingContainer, fullName);
    }

    public String getBillingStreet() {
        return getNestedTextFromList(billingContainer, streetRows, 1);
    }

    public String getBillingCityStatePostcode() {
        return getNestedText(billingContainer, cityStatePostcode);
    }

    public String getBillingCountry() {
        return getNestedText(billingContainer, country);
    }

    public String getBillingPhone() {
        return getNestedText(billingContainer, phone);
    }


    // ===== REVIEW ORDER =====
    public List<String> getProductNames() {
        List<String> list = new ArrayList<>();
        for (WebElement row : driver.findElements(productRows)) {
            list.add(row.findElement(productName).getText().trim());
        }
        return list;
    }

    public List<String> getProductPrices() {
        List<String> list = new ArrayList<>();
        for (WebElement row : driver.findElements(productRows)) {
            list.add(row.findElement(productPrice).getText().trim());
        }
        return list;
    }

    public List<String> getProductQuantities() {
        List<String> list = new ArrayList<>();
        for (WebElement row : driver.findElements(productRows)) {
            list.add(row.findElement(productQuantity).getText().trim());
        }
        return list;
    }

    public List<String> getProductTotals() {
        List<String> list = new ArrayList<>();
        for (WebElement row : driver.findElements(productRows)) {
            list.add(row.findElement(productTotal).getText().trim());
        }
        return list;
    }

    public String getOverallTotal() {
        return waitForElementVisible(totalAmount).getText().trim();
    }

    // -------------------- ACTION METHODS --------------------
    /**
     * Enter a comment for the order.
     *
     * @param text Comment text to enter
     */
    public CheckoutPage enterOrderComment(String text) {
        type(commentTextArea, text);
        return this;
    }

    /**
     * Click the "Place Order" button to proceed to payment.
     */
    public CheckoutPage clickPlaceOrder() {
        click(placeOrderButton);
        return this;
    }

}
