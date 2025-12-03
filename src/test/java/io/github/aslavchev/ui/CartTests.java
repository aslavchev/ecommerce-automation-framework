package io.github.aslavchev.ui;

import io.github.aslavchev.base.BaseTest;
import io.github.aslavchev.dataproviders.ProductDataProvider;
import io.github.aslavchev.ui.pages.CartPage;
import io.github.aslavchev.ui.pages.ProductDetailsPage;
import io.github.aslavchev.ui.pages.ProductsPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * CartTests - Test suite for shopping cart functionality
 * Test Cases: 12, 13, 17
 */
public class CartTests extends BaseTest {
    @Test(groups = {"regression", "ui"}, dataProvider = "productData")
    @Description("Test Case 12: Add product to cart and verify (Data-Driven)")
    public void testAddProductToCart(String testName, String product1, String product2) {
        // Arrange
        // Products are now passed as parameters from DataProvider
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateProducts();

        // Act
        productsPage.addProductToCartByName(product1);
        productsPage.clickContinueShopping();
        productsPage.addProductToCartByName(product2);
        CartPage cartPage = productsPage.clickViewCart();


        // Assert - Verify both products are in cart with correct details
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain 2 products");
        Assert.assertTrue(cartPage.getProductNames().contains(product1), "Cart should contain " + product1);
        Assert.assertTrue(cartPage.getProductNames().contains(product2), "Cart should contain " + product2);

        Assert.assertEquals(cartPage.getProductQuantities().size(), 2, "Should have 2 quantities");
        Assert.assertEquals(cartPage.getProductPrices().size(), 2, "Should have 2 prices");
        Assert.assertEquals(cartPage.getProductTotals().size(), 2, "Should have 2 totals");
    }

    @Test(groups = {"regression", "ui"})
    @Description("Test Case 13: Verify product quantity in cart")
    public void testVerifyProductQuantityInCart() {
        //Arrange
        int expectedQuantity = 4;
        ProductDetailsPage detailsPage = new ProductDetailsPage(driver);
        driver.get(BASE_URL + "/product_details/1");

        //Act
        detailsPage.setQuantity(expectedQuantity);
        CartPage cartPage = detailsPage.addToCartAndViewCart();

        //Assert
        String actualQuantity = cartPage.getFirstProductQuantity();
        Assert.assertEquals(actualQuantity, String.valueOf(expectedQuantity), "Product quantity should be " + expectedQuantity);
    }

    @Test(groups = {"regression", "ui"})
    @Description("Test Case 17: Remove product from cart")
    public void testRemoveProductFromCart() {
        //Arrange
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateProducts();
        productsPage.addFirstProductToCart();
        CartPage cartPage = productsPage.clickViewCart();

        Assert.assertTrue(cartPage.getCartItemCount() > 0, "Cart should have items");

        //Act
        cartPage.removeFirstProduct();

        //Assert
        Assert.assertTrue(cartPage.isCartEmpty(), "Cart should be empty after removal");
    }

    // ==================================
    // 🔽 DATA PROVIDER AT THE BOTTOM 🔽
    // ==================================
    @DataProvider(name = "productData")
    public Object[][] getProductData() {
        return ProductDataProvider.getProductPairs();
    }
}
