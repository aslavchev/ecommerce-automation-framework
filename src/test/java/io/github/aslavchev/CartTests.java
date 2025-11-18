package io.github.aslavchev;

import io.github.aslavchev.base.BaseTest;
import io.github.aslavchev.pages.CartPage;
import io.github.aslavchev.pages.ProductDetailsPage;
import io.github.aslavchev.pages.ProductsPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * CartTests - Test suite for shopping cart functionality
 * Test Cases: 12, 13, 17
 */
public class CartTests extends BaseTest {
    @Test
    @Description("Test Case 12: Add product to cart and verify")
    public void testAddProductToCart() {
        //Arrange
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateProducts();

        //Act
        productsPage.addFirstProductToCart();
        CartPage cartPage = productsPage.clickViewCart();

        //Assert
        Assert.assertTrue(cartPage.getCartItemCount() > 0, "Cart should contain at least one product");
    }

    @Test
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

    @Test
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
        Assert.assertTrue(cartPage.isCartEmpty(),"Cart should be empty after removal");
    }
}
