package pages;

import constants.FilterOptions;
import constants.TestConstants;
import locators.ProductsPageLocators;
import org.openqa.selenium.WebElement;
import utils.ColorUtils;
import utils.PriceUtils;
import utils.WebDriverUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductsPage {

    public boolean isProductsLabelDisplayed() {
        return WebDriverUtils.isElementDisplayed(ProductsPageLocators.PRODUCT_LABEL);
    }

    public String getProductsPageLabelText() {
        return WebDriverUtils.getText(ProductsPageLocators.PRODUCT_LABEL);
    }

    public CartPage navigateToCart() {
        WebDriverUtils.click(ProductsPageLocators.CART_BUTTON);
        return new CartPage();
    }

    public HomePage logout() {
        WebDriverUtils.click(ProductsPageLocators.MENU_BUTTON);
        WebDriverUtils.click(ProductsPageLocators.LOGOUT_LINK);
        return new HomePage();
    }

    public boolean isPricesSortedLowToHigh() {
        filterBy(FilterOptions.PRICE_LOW_TO_HIGH);
        List<Double> prices = PriceUtils.extractPricesFromElements(ProductsPageLocators.PRICE_LABELS);
        return PriceUtils.isPricesSortedAscending(prices);
    }

    public boolean isPricesSortedHighToLow() {
        filterBy(FilterOptions.PRICE_HIGH_TO_LOW);
        List<Double> prices = PriceUtils.extractPricesFromElements(ProductsPageLocators.PRICE_LABELS);
        return PriceUtils.isPricesSortedDescending(prices);
    }

    private void filterBy(FilterOptions option) {
        WebDriverUtils.selectDropdownByIndex(ProductsPageLocators.FILTER_DROPDOWN, option.getIndex());
    }

    public String clickFirstProductAndGetTitle() {
        WebElement firstProduct = WebDriverUtils.findElements(ProductsPageLocators.PRODUCT_TITLE).get(0);
        String title = firstProduct.getText();
        WebDriverUtils.click(firstProduct);
        return title;
    }

    public boolean isProductPageTitleCorrect() {
        String expectedTitle = clickFirstProductAndGetTitle();
        String actualTitle = WebDriverUtils.getText(ProductsPageLocators.PRODUCT_PAGE_TITLE);
        return expectedTitle.equals(actualTitle);
    }

    public ProductsPage addFirstProductToCart() {
        WebElement firstProduct = WebDriverUtils.findElements(ProductsPageLocators.PRODUCT_DIV).get(0);
        WebElement addToCartButton = WebDriverUtils.findElementWithin(firstProduct, ProductsPageLocators.ADD_TO_CART_BUTTON);
        WebDriverUtils.click(addToCartButton);
        return this;
    }

    public String getCartBadgeBackgroundColor() {
        WebElement badge = WebDriverUtils.findElement(ProductsPageLocators.CART_BADGE);
        String cssColor = badge.getCssValue("background-color");
        return ColorUtils.convertToHex(cssColor);
    }

    public String getCartBadgeText() {
        return WebDriverUtils.getText(ProductsPageLocators.CART_BADGE);
    }

    public boolean isRemoveButtonDisplayedForFirstProduct() {
        WebElement firstProduct = WebDriverUtils.findElements(ProductsPageLocators.PRODUCT_DIV).get(0);
        WebElement removeButton = WebDriverUtils.findElementWithin(firstProduct, ProductsPageLocators.REMOVE_BUTTON);
        return WebDriverUtils.isElementDisplayed(removeButton);
    }

    public String completeDirectOrder() {
        addFirstProductToCart();
        navigateToCart();
        proceedToCheckout();
        fillCheckoutInformation();
        completeOrder();
        return getOrderConfirmationText();
    }

    private void proceedToCheckout() {
        WebDriverUtils.click(ProductsPageLocators.CHECKOUT_BUTTON);
    }

    private void fillCheckoutInformation() {
        WebDriverUtils.sendKeys(ProductsPageLocators.FIRST_NAME_INPUT, TestConstants.TEST_FIRST_NAME);
        WebDriverUtils.sendKeys(ProductsPageLocators.LAST_NAME_INPUT, TestConstants.TEST_LAST_NAME);
        WebDriverUtils.sendKeys(ProductsPageLocators.POSTAL_CODE_INPUT, TestConstants.TEST_POSTAL_CODE);
        WebDriverUtils.click(ProductsPageLocators.CONTINUE_BUTTON);
    }

    private void completeOrder() {
        WebDriverUtils.click(ProductsPageLocators.FINISH_BUTTON);
    }

    private String getOrderConfirmationText() {
        return WebDriverUtils.getText(ProductsPageLocators.ORDER_CONFIRMATION_TITLE);
    }

    public boolean addMultipleProductsAndVerify() {
        List<String> addedProductTitles = new ArrayList<>();

        // Add first product
        String firstProductTitle = addProductAndGetTitle(0);
        addedProductTitles.add(firstProductTitle);

        // Navigate to cart and continue shopping
        navigateToCart();
        continueShopping();

        // Add second product
        String secondProductTitle = addProductAndGetTitle(2);
        addedProductTitles.add(secondProductTitle);

        // Verify products in cart
        navigateToCart();
        return verifyProductsInCart(addedProductTitles);
    }

    private String addProductAndGetTitle(int productIndex) {
        WebElement product = WebDriverUtils.findElements(ProductsPageLocators.PRODUCT_DIV).get(productIndex);
        String productTitle = WebDriverUtils.findElementWithin(product, ProductsPageLocators.PRODUCT_TITLE).getText();
        WebElement addToCartButton = WebDriverUtils.findElementWithin(product, ProductsPageLocators.ADD_TO_CART_BUTTON);
        WebDriverUtils.click(addToCartButton);
        return productTitle;
    }

    private void continueShopping() {
        WebDriverUtils.click(ProductsPageLocators.CONTINUE_SHOPPING_BUTTON);
    }

    private boolean verifyProductsInCart(List<String> expectedTitles) {
        List<WebElement> cartItems = WebDriverUtils.findElements(ProductsPageLocators.CART_ITEM_NAME);

        for (WebElement item : cartItems) {
            String actualTitle = item.getText();
            if (!expectedTitles.contains(actualTitle)) {
                return false;
            }
        }
        return true;
    }
}