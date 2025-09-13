package tests;

import base.BaseTest;
import constants.TestConstants;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductsPage;

@Epic("Authentication")
@Feature("Login Functionality")
public class HomePageTests extends BaseTest {

    @Test(description = "Verify successful login with valid credentials")
    @Story("User Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("This test verifies that a user can successfully log in with valid credentials")
    @Issue("AUTH-001")
    @TmsLink("TC-001")
    public void testSuccessfulLogin() {
        HomePage homePage = new HomePage();

        Allure.step("Navigate to login page and enter credentials", () -> {
            homePage.login();
        });

        ProductsPage productsPage = new ProductsPage();

        Allure.step("Verify products page is displayed", () -> {
            Assert.assertTrue(productsPage.isProductsLabelDisplayed(),
                    "Products label should be displayed after successful login");
            Assert.assertEquals(productsPage.getProductsPageLabelText(),
                    TestConstants.EXPECTED_PRODUCTS_LABEL,
                    "Products page label text should match expected value");
        });
    }
}
