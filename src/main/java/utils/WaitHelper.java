package utils;

import base.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;

import java.time.Duration;
import java.util.function.Function;

public class WaitHelper {
    private static final int DEFAULT_TIMEOUT = 30;
    private static final int DEFAULT_POLLING_INTERVAL = 1;

    private static Wait<WebDriver> createWait() {
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(DEFAULT_POLLING_INTERVAL))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class)
                .ignoring(NoSuchElementException.class);
    }

    private static Wait<WebDriver> createWait(int timeoutInSeconds) {
        return new FluentWait<>(DriverManager.getDriver())
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofSeconds(DEFAULT_POLLING_INTERVAL))
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class)
                .ignoring(NoSuchElementException.class);
    }

    public static WebElement waitForElementToBeVisible(By locator) {
        return createWait().until(driver -> {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed() ? element : null;
        });
    }

    public static WebElement waitForElementToBeClickable(By locator) {
        return createWait().until(driver -> {
            WebElement element = driver.findElement(locator);
            return (element.isDisplayed() && element.isEnabled()) ? element : null;
        });
    }

    public static WebElement waitForElementToBeClickable(WebElement webElement) {
        return createWait().until(driver -> {
            return (webElement.isDisplayed() && webElement.isEnabled()) ? webElement : null;
        });
    }

    public static WebElement waitForElementToBePresent(By locator) {
        return createWait().until(driver -> driver.findElement(locator));
    }

    public static WebElement waitForElementToBePresent(WebElement element) {
        return createWait().until(driver -> {
            if (element.isDisplayed()) {
                return element;
            }
            return null;
        });
    }

    public static boolean waitForElementToBeInvisible(By locator) {
        return createWait().until(driver -> {
            try {
                WebElement element = driver.findElement(locator);
                return !element.isDisplayed();
            } catch (NoSuchElementException e) {
                return true;
            }
        });
    }

    public static boolean waitForTextToBePresentInElement(By locator, String text) {
        return createWait().until(driver -> {
            WebElement element = driver.findElement(locator);
            return element.getText().contains(text);
        });
    }

    public static WebElement waitForElementWithCustomCondition(By locator, Function<WebElement, Boolean> condition) {
        return createWait().until(driver -> {
            WebElement element = driver.findElement(locator);
            return condition.apply(element) ? element : null;
        });
    }

    public static WebElement waitForElementWithTimeout(By locator, int timeoutInSeconds) {
        return createWait(timeoutInSeconds).until(driver -> {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed() ? element : null;
        });
    }

    public static <T> T waitUntil(Function<WebDriver, T> condition) {
        return createWait().until(condition);
    }

}