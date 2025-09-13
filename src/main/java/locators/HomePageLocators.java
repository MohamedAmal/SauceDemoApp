package locators;

import org.openqa.selenium.By;

public class HomePageLocators {
    public static final By USERNAME_INPUT = By.id("user-name");
    public static final By PASSWORD_INPUT = By.id("password");
    public static final By SUBMIT_BUTTON = By.cssSelector("input[type='submit']");
    public static final By LOGIN_LOGO = By.className("login_logo");
}