package pages;

import constants.TestConstants;
import locators.HomePageLocators;
import utils.WebDriverUtils;

public class HomePage {

    public HomePage login() {
        return login(TestConstants.STANDARD_USERNAME, TestConstants.STANDARD_PASSWORD);
    }

    public HomePage login(String username, String password) {
        WebDriverUtils.sendKeys(HomePageLocators.USERNAME_INPUT, username);
        WebDriverUtils.sendKeys(HomePageLocators.PASSWORD_INPUT, password);
        WebDriverUtils.click(HomePageLocators.SUBMIT_BUTTON);
        return this;
    }

    public boolean isLoginLogoDisplayed() {
        return WebDriverUtils.isElementDisplayed(HomePageLocators.LOGIN_LOGO);
    }

    public String getCurrentUrl() {
        return WebDriverUtils.getCurrentUrl();
    }
}