package pages;

import locators.CartPageLocators;
import utils.WebDriverUtils;

public class CartPage {

    public String getCartPageLabelText() {
        return WebDriverUtils.getText(CartPageLocators.CART_LABEL);
    }
}