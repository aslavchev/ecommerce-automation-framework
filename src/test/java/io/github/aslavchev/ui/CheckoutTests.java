package io.github.aslavchev.ui;

import io.github.aslavchev.data.ProductDataProvider;
import io.github.aslavchev.ui.pages.*;
import io.github.aslavchev.utils.TestConfig;
import io.github.aslavchev.utils.TestDataReader;
import io.github.aslavchev.utils.UserData;
import io.qameta.allure.Description;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CheckoutTests extends BaseTest {
    @Test(groups = {"e2e", "regression", "critical", "ui", "slow"}, dataProvider = "checkoutData")
    @Description("Test Case 16: Place Order: Login before Checkout (Data-Driven)")
    public void testPlaceOrderLogInBeforeCheckout(String testName,
                                                  String productName,
                                                  String expectedPrice,
                                                  String cardName,
                                                  String cardNumber,
                                                  String cvc,
                                                  String expiryMonth,
                                                  String expiryYear) {
        // Arrange
        String email = TestConfig.email();
        String password = TestConfig.password();
        String expectedQuantity = "1";

        // Load expected user data from CSV based on current user
        UserData expectedUser = TestDataReader.getUserData(email);

        SoftAssert soft = new SoftAssert();

        // ===== ACT: LOGIN =====
        new LoginPage(driver)
                .navigateToLogin()
                .login(email, password)
                .waitForLoginSuccess();

        // ===== ACT: ADD PRODUCT TO CART =====
        new ProductsPage(driver)
                .navigateProducts()
                .addProductToCartByName(productName);

        // ===== ACT: PROCEED TO CHECKOUT =====
        CartPage cartPage = new ProductsPage(driver).clickViewCart();
        cartPage.clickProceedToCheckout();

        CheckoutPage checkoutPage = new CheckoutPage(driver);


        /// ===== ASSERT: VERIFY DELIVERY ADDRESS =====
        String actualDeliveryFullName = checkoutPage.getDeliveryFullName();
        String actualDeliveryStreet = checkoutPage.getDeliveryStreet();
        String actualDeliveryCityStatePostcode = checkoutPage.getDeliveryCityStatePostcode();
        String actualDeliveryCountry = checkoutPage.getDeliveryCountry();
        String actualDeliveryPhone = checkoutPage.getDeliveryPhone();

        soft.assertEquals(actualDeliveryFullName, expectedUser.fullName, "Delivery: Full Name");
        soft.assertEquals(actualDeliveryStreet, expectedUser.street, "Delivery: Street");
        soft.assertEquals(actualDeliveryCityStatePostcode, expectedUser.cityStatePostcode, "Delivery: City/State/Postcode");
        soft.assertEquals(actualDeliveryCountry, expectedUser.country, "Delivery: Country");
        soft.assertEquals(actualDeliveryPhone, expectedUser.phone, "Delivery: Phone");

        // ===== ASSERT: VERIFY BILLING ADDRESS =====
        String actualBillingFullName = checkoutPage.getBillingFullName();
        String actualBillingStreet = checkoutPage.getBillingStreet();
        String actualBillingCityStatePostcode = checkoutPage.getBillingCityStatePostcode();
        String actualBillingCountry = checkoutPage.getBillingCountry();
        String actualBillingPhone = checkoutPage.getBillingPhone();

        soft.assertEquals(actualBillingFullName, expectedUser.fullName, "Billing: Full Name");
        soft.assertEquals(actualBillingStreet, expectedUser.street, "Billing: Street");
        soft.assertEquals(actualBillingCityStatePostcode, expectedUser.cityStatePostcode, "Billing: City/State/Postcode");
        soft.assertEquals(actualBillingCountry, expectedUser.country, "Billing: Country");
        soft.assertEquals(actualBillingPhone, expectedUser.phone, "Billing: Phone");

        // ===== ASSERT: VERIFY ORDER SUMMARY =====
        var names = checkoutPage.getProductNames();
        var prices = checkoutPage.getProductPrices();
        var quantities = checkoutPage.getProductQuantities();
        var totals = checkoutPage.getProductTotals();

        // Verify at least one product exists
        soft.assertTrue(names.size() > 0, "Cart should contain at least one product");

        // Extract actual values
        String actualProductName = names.get(0);
        String actualProductPrice = prices.get(0);
        String actualProductQuantity = quantities.get(0);
        String actualProductTotal = totals.get(0);
        String actualOverallTotal = checkoutPage.getOverallTotal();

        // Verify product details
        soft.assertEquals(actualProductName, productName, "Product: Name");
        soft.assertEquals(actualProductPrice, expectedPrice, "Product: Unit Price");
        soft.assertEquals(actualProductQuantity, expectedQuantity, "Product: Quantity");
        soft.assertEquals(actualProductTotal, expectedPrice, "Product: Total");
        soft.assertEquals(actualOverallTotal, expectedPrice, "Order: Overall Total");

        // ===== ACT: PLACE ORDER =====
        checkoutPage
                .enterOrderComment("Automated test - please ignore")
                .clickPlaceOrder();


        /// ===== ACT: ENTER PAYMENT DETAILS =====
        OrderConfirmationPage confirmation = new PaymentPage(driver)
                .enterPaymentDetails(cardName, cardNumber, cvc, expiryMonth, expiryYear)
                .clickPayAndConfirm();

        // ===== ASSERT: VERIFY ORDER CONFIRMATION =====
        soft.assertTrue(confirmation.isOrderPlacedHeaderVisible());
        soft.assertEquals(confirmation.getOrderPlacedHeader(), "ORDER PLACED!");
        soft.assertEquals(confirmation.getConfirmationMessage(), "Congratulations! Your order has been confirmed!");

        // Execute all soft assertions
        soft.assertAll();
    }

    // ==================================
    // 🔽 DATA PROVIDER AT THE BOTTOM 🔽
    // ==================================
    @DataProvider(name = "checkoutData")
    public Object[][] getCheckoutData() {
        return TestDataReader.readCheckoutPaymentData(
                "checkout-payment.csv",
                ProductDataProvider::getProductPrice);
    }
}
