package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class DriverManager {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void setDriver(String browserName) {
        setDriver(browserName, false);
    }

    public static void setDriver(String browserName, boolean headless) {
        WebDriver webDriver = createDriver(browserName.toLowerCase(), headless);
        try {
            webDriver.manage().window().maximize();
        } catch (Exception ignored) {
            // In headless mode maximize may fail, rely on --window-size
        }
        driver.set(webDriver);
    }

    private static WebDriver createDriver(String browserName, boolean headless) {
        switch (browserName) {
            case "chrome":
                return createChromeDriver(headless);
            case "firefox":
                return createFirefoxDriver(headless);
            case "edge":
                return createEdgeDriver(headless);
            default:
                throw new IllegalArgumentException("Browser not supported: " + browserName);
        }
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        addCommonChromeArguments(options, headless);
        return new ChromeDriver(options);
    }

    private static void addCommonChromeArguments(ChromeOptions options, boolean headless) {
        String userDataDir = createTempUserDataDir();
        options.addArguments("--user-data-dir=" + userDataDir);

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--incognito");

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        options.addPreference("browser.cache.disk.enable", false);
        options.addPreference("browser.cache.memory.enable", false);
        options.addPreference("browser.cache.offline.enable", false);
        options.addPreference("network.http.use-cache", false);

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");

        if (headless) {
            options.addArguments("--headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }

        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        String userDataDir = createTempUserDataDir();
        options.addArguments("--user-data-dir=" + userDataDir);

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        return new EdgeDriver(options);
    }

    private static String createTempUserDataDir() {
        try {
            Path tempDir = Files.createTempDirectory("selenium-profile-" + UUID.randomUUID());
            tempDir.toFile().deleteOnExit();
            return tempDir.toAbsolutePath().toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create temp user data dir", e);
        }
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            try {
                webDriver.quit();
            } catch (Exception ignored) {
            }
            driver.remove();
        }
    }
}
