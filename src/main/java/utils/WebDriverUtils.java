
package utils;

import base.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class WebDriverUtils {

    public static WebElement findElement(By locator) {
        return WaitHelper.waitForElementToBeVisible(locator);
    }

    public static List<WebElement> findElements(By locator) {
        return WaitHelper.waitUntil(driver -> {
            List<WebElement> elements = driver.findElements(locator);
            return elements.isEmpty() ? null : elements;
        });
    }

    public static WebElement findElementWithin(By parentLocator, By childLocator) {

        return WaitHelper.waitUntil(driver -> {
            try {
                WebElement parent = findElement(parentLocator);
                WebElement child = parent.findElement(childLocator);
                return child.isDisplayed() ? child : null;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return null;
            }
        });
    }

    public static WebElement findElementWithin(WebElement parentWebElement, By childLocator) {
        return WaitHelper.waitUntil(driver -> {
            try {
                WebElement child = parentWebElement.findElement(childLocator);
                return child.isDisplayed() ? child : null;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return null;
            }
        });
    }

    public static void click(By locator) {
        WebElement element = WaitHelper.waitForElementToBeClickable(locator);
        element.click();
    }

    public static void click(WebElement webElement) {
        WebElement element = WaitHelper.waitForElementToBeClickable(webElement);
        element.click();
    }

    public static void sendKeysWithClear(By locator, String text) {
        WebElement element = WaitHelper.waitForElementToBeVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    public static void sendKeys(By locator, String text) {
        WebElement element = WaitHelper.waitForElementToBeVisible(locator);
        element.sendKeys(text);
    }

    public static String getText(By locator) {
        WebElement element = WaitHelper.waitForElementToBeVisible(locator);
        return element.getText();
    }

    public static String getAttribute(By locator, String attributeName) {
        WebElement element = WaitHelper.waitForElementToBePresent(locator);
        return element.getAttribute(attributeName);
    }

    public static boolean isElementDisplayed(By locator) {
        try {
            WebElement element = WaitHelper.waitForElementToBePresent(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isElementDisplayed(WebElement webElement) {
        try {
            WaitHelper.waitForElementToBePresent(webElement);
            return webElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static void selectDropdownByIndex(By locator, int index) {
        WebElement dropdown = WaitHelper.waitForElementToBeVisible(locator);
        Select select = new Select(dropdown);
        select.selectByIndex(index);
    }

    public static boolean isElementEnabled(By locator) {
        try {
            WebElement element = WaitHelper.waitForElementToBePresent(locator);
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public static void scrollToElement(By locator) {
        WebElement element = WaitHelper.waitForElementToBePresent(locator);
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void hoverOverElement(By locator) {
        WebElement element = WaitHelper.waitForElementToBeVisible(locator);
        Actions actions = new Actions(DriverManager.getDriver());
        actions.moveToElement(element).perform();
    }

    public static void doubleClick(By locator) {
        WebElement element = WaitHelper.waitForElementToBeClickable(locator);
        Actions actions = new Actions(DriverManager.getDriver());
        actions.doubleClick(element).perform();
    }

    public static void rightClick(By locator) {
        WebElement element = WaitHelper.waitForElementToBeClickable(locator);
        Actions actions = new Actions(DriverManager.getDriver());
        actions.contextClick(element).perform();
    }

    public static void pressKey(Keys key) {
        Actions actions = new Actions(DriverManager.getDriver());
        actions.sendKeys(key).perform();
    }

    public static void pressKeyOnElement(By locator, Keys key) {
        WebElement element = WaitHelper.waitForElementToBeVisible(locator);
        element.sendKeys(key);
    }

    public static List<String> getTextFromElements(By locator) {
        WaitHelper.waitForElementToBePresent(locator);
        return DriverManager.getDriver().findElements(locator)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public static int getElementCount(By locator) {
        try {
            WaitHelper.waitForElementToBePresent(locator);
            return DriverManager.getDriver().findElements(locator).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public static void clickByJavaScript(By locator) {
        WebElement element = WaitHelper.waitForElementToBePresent(locator);
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].click();", element);
    }

    public static void navigateToUrl(String url) {
        DriverManager.getDriver().get(url);
    }

    public static String getCurrentUrl() {
        return DriverManager.getDriver().getCurrentUrl();
    }

    public static String getPageTitle() {
        return DriverManager.getDriver().getTitle();
    }

    public static void refreshPage() {
        DriverManager.getDriver().navigate().refresh();
    }

    public static void navigateBack() {
        DriverManager.getDriver().navigate().back();
    }

    public static void navigateForward() {
        DriverManager.getDriver().navigate().forward();
    }
}