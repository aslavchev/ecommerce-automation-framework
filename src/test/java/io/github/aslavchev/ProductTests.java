package io.github.aslavchev;

import io.github.aslavchev.base.BaseTest;
import io.github.aslavchev.pages.ProductDetailsPage;
import io.github.aslavchev.pages.ProductsPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductTests extends BaseTest {

    @Test(groups = {"smoke", "regression", "ui"})
    @Description("Test Case 8: Verify All Products page navigation")
    public void testNavigationToProductsPage() {
        //Arrange
        ProductsPage productsPage = new ProductsPage(driver);

        //Act
        productsPage.navigateProducts();

        //Assert
        Assert.assertTrue(productsPage.isAllProductsPageDisplayed(),
                "All products page should be displayed");
    }

    @Test(groups = {"regression", "ui"})
    @Description("Test Case 9: Search products by keyword")
    public void testSearchProduct() {
        //Arrange
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateProducts();

        //Act
        productsPage.searchForProduct("Dress");

        //Assert
        Assert.assertTrue(productsPage.isSearchedProductsHeadingDisplayed(),
                "Searched products heading should be displayed");
        Assert.assertTrue(productsPage.doSearchResultsContain("Dress"),
                "Search results should contain products related to 'Dress'");
    }

    @Test(groups = {"regression", "ui"})
    @Description("Test Case 18: Filter products by category (Women > Dress)")
    public void testFilterByCategory() {
        //Arrange
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateProducts();

        //Verify categories sidebar is visible
        Assert.assertTrue(productsPage.isCategorySidebarVisible(),
                "Category sidebar should be visible");

        //Act
        productsPage.clickWomenDressSubcategory();

        //Assert
        Assert.assertTrue(productsPage.isCategoryPageDisplayed("WOMEN - DRESS"),
                "Women Dress category page should be displayed");
    }


    @Test(groups = {"regression", "ui"})
    @Description("Test case 8: View product details page")
    public void testViewProductDetails() {
        //Arrange
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateProducts();

        //Act
        ProductDetailsPage productDetailsPage = productsPage.openFirstProductDetails();

        //Assert
        Assert.assertTrue(productDetailsPage.isProductDetailsVisible(),
                "Product details should be visible");
    }
}
