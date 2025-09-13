package tests;

import base.BaseTest;
import constants.TestConstants;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductsPage;

@Epic("E-commerce")
@Feature("Product Management")
@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class ProductsPageTests extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod
    @Step("Login before each test") // This will show as setup step in Allure
    public void loginBeforeEachTest() {
        HomePage homePage = new HomePage();
        homePage.login();
        productsPage = new ProductsPage();
    }

    @Test(description = "Verify user can logout successfully")
    @Story("User Logout")
    @Severity(SeverityLevel.CRITICAL)
    public void testUserLogout() {
        HomePage homePageAfterLogout = productsPage.logout();

        String currentUrl = homePageAfterLogout.getCurrentUrl();
        Assert.assertEquals(currentUrl, TestConstants.EXPECTED_LOGIN_URL,
                "URL should redirect to login page after logout");
        Assert.assertTrue(homePageAfterLogout.isLoginLogoDisplayed(),
                "Login logo should be displayed after logout");
    }

    @Test(description = "Verify cart page displays correct label")
    @Story("Shopping Cart")
    @Severity(SeverityLevel.NORMAL)
    public void testCartPageLabel() {
        CartPage cartPage = productsPage.navigateToCart();
        String cartLabelText = cartPage.getCartPageLabelText();

        Assert.assertEquals(cartLabelText, TestConstants.EXPECTED_CART_LABEL,
                "Cart page should display correct label text");
    }

    @Test(description = "Verify prices can be sorted from low to high")
    @Story("Product Sorting")
    @Severity(SeverityLevel.NORMAL)
    public void testPriceSortingLowToHigh() {
        boolean isSorted = productsPage.isPricesSortedLowToHigh();
        Assert.assertTrue(isSorted, "Prices should be sorted from low to high");
    }

    @Test(description = "Verify prices can be sorted from high to low")
    @Story("Product Sorting")
    @Severity(SeverityLevel.NORMAL)
    public void testPriceSortingHighToLow() {
        boolean isSorted = productsPage.isPricesSortedHighToLow();
        Assert.assertTrue(isSorted, "Prices should be sorted from high to low");
    }

    @Test(description = "Verify product page shows correct title when clicked")
    @Story("Product Details")
    @Severity(SeverityLevel.NORMAL)
    public void testProductPageTitle() {
        boolean isTitleCorrect = productsPage.isProductPageTitleCorrect();
        Assert.assertTrue(isTitleCorrect, "Product page should display correct title");
    }

    @Test(description = "Verify cart badge displays correct color and count after adding product")
    @Story("Shopping Cart")
    @Severity(SeverityLevel.NORMAL)
    public void testCartBadgeAfterAddingProduct() {
        productsPage.addFirstProductToCart();

        String bgColor = productsPage.getCartBadgeBackgroundColor();
        Assert.assertEquals(bgColor, TestConstants.EXPECTED_CART_BADGE_COLOR,
                "Cart badge should display red color");

        String cartNumber = productsPage.getCartBadgeText();
        Assert.assertEquals(cartNumber, "1",
                "Cart badge should display count of 1");
    }

    @Test(description = "Verify Add to Cart button changes to Remove after clicking")
    @Story("Shopping Cart")
    @Severity(SeverityLevel.NORMAL)
    public void testAddToCartButtonUpdate() {
        productsPage.addFirstProductToCart();
        boolean isRemoveButtonDisplayed = productsPage.isRemoveButtonDisplayedForFirstProduct();
        Assert.assertTrue(isRemoveButtonDisplayed,
                "Remove button should be displayed after adding product to cart");
    }

    @Test(description = "Verify complete order flow from product to confirmation")
    @Story("Order Management")
    @Severity(SeverityLevel.BLOCKER)
    public void testCompleteOrderFlow() {
        String orderConfirmationText = productsPage.completeDirectOrder();
        Assert.assertEquals(orderConfirmationText, TestConstants.EXPECTED_ORDER_CONFIRMATION,
                "Order confirmation message should match expected text");
    }

    @Test(description = "Verify multiple products can be added and verified in cart")
    @Story("Shopping Cart")
    @Severity(SeverityLevel.NORMAL)
    public void testMultipleProductsAddition() {
        boolean allProductsVerified = productsPage.addMultipleProductsAndVerify();
        Assert.assertTrue(allProductsVerified,
                "All added products should be verified in the cart");
    }
}
