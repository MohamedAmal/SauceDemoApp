package locators;

import org.openqa.selenium.By;

public class ProductsPageLocators {
    public static final By PRODUCT_LABEL = By.className("product_label");
    public static final By SHOPPING_CART_LINK = By.className("shopping_cart_link");
    public static final By MENU_BUTTON = By.cssSelector("[class='bm-burger-button']");
    public static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    public static final By CART_BUTTON = By.cssSelector("a[class*='shopping_cart_link']");
    public static final By PRICE_LABELS = By.cssSelector("[class='inventory_item_price']");
    public static final By FILTER_DROPDOWN = By.className("product_sort_container");
    public static final By PRODUCT_DIV = By.className("inventory_item");
    public static final By PRODUCT_TITLE = By.className("inventory_item_name");
    public static final By PRODUCT_PAGE_TITLE = By.className("inventory_details_name");
    public static final By CART_BADGE = By.cssSelector(".fa-layers-counter.shopping_cart_badge");
    public static final By ADD_TO_CART_BUTTON = By.cssSelector(".btn_primary.btn_inventory");
    public static final By REMOVE_BUTTON = By.cssSelector(".btn_secondary.btn_inventory");
    public static final By CHECKOUT_BUTTON = By.className("checkout_button");
    public static final By FIRST_NAME_INPUT = By.id("first-name");
    public static final By LAST_NAME_INPUT = By.id("last-name");
    public static final By POSTAL_CODE_INPUT = By.id("postal-code");
    public static final By CONTINUE_BUTTON = By.cssSelector("[type='submit']");
    public static final By FINISH_BUTTON = By.className("btn_action");
    public static final By ORDER_CONFIRMATION_TITLE = By.tagName("h2");
    public static final By CONTINUE_SHOPPING_BUTTON = By.cssSelector("[class='btn_secondary']");
    public static final By CART_ITEM_NAME = By.className("inventory_item_name");
}