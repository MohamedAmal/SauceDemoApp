package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class DriverManager {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<String> userDataDir = new ThreadLocal<>();

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
        // Create unique user data directory for this thread
        String userDataDirPath = createUniqueUserDataDir();
        options.addArguments("--user-data-dir=" + userDataDirPath);

        // Essential arguments for CI environment
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Additional stability arguments for CI
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-web-security");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-features=VizDisplayCompositor");

        // Use incognito mode
        options.addArguments("--incognito");

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        // Create unique profile directory
        String profileDirPath = createUniqueUserDataDir();
        options.addArguments("--profile", profileDirPath);

        // Disable caching
        options.addPreference("browser.cache.disk.enable", false);
        options.addPreference("browser.cache.memory.enable", false);
        options.addPreference("browser.cache.offline.enable", false);
        options.addPreference("network.http.use-cache", false);

        // Additional preferences for stability
        options.addPreference("media.navigator.enabled", false);
        options.addPreference("media.peerconnection.enabled", false);

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

        // Create unique user data directory for this thread
        String userDataDirPath = createUniqueUserDataDir();
        options.addArguments("--user-data-dir=" + userDataDirPath);

        // Essential arguments for CI environment
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");

        // Additional stability arguments for CI
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-web-security");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-features=VizDisplayCompositor");

        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        return new EdgeDriver(options);
    }

    private static String createUniqueUserDataDir() {
        try {
            // Create a more unique directory name using thread ID and timestamp
            String threadId = String.valueOf(Thread.currentThread().getId());
            String timestamp = String.valueOf(System.currentTimeMillis());
            String uniqueId = UUID.randomUUID().toString().substring(0, 8);

            String dirName = String.format("selenium-profile-%s-%s-%s", threadId, timestamp, uniqueId);
            Path tempDir = Files.createTempDirectory(dirName);

            // Store the path for cleanup
            String dirPath = tempDir.toAbsolutePath().toString();
            userDataDir.set(dirPath);

            return dirPath;
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
                // Ignore quit exceptions
            }
            driver.remove();

            // Clean up user data directory
            cleanupUserDataDir();
        }
    }

    private static void cleanupUserDataDir() {
        String dirPath = userDataDir.get();
        if (dirPath != null) {
            try {
                File dir = new File(dirPath);
                if (dir.exists()) {
                    deleteDirectory(dir);
                }
            } catch (Exception ignored) {
                // Ignore cleanup exceptions
            } finally {
                userDataDir.remove();
            }
        }
    }

    private static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}