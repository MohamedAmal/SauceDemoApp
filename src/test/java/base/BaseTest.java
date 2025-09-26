package base;

import constants.TestConstants;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class BaseTest {
//    @Optional("chrome") String browser,
//    @Optional("false") String headless,
    @BeforeMethod
    @Parameters({"browser", "headless"})
    public void setUp( String browser,
                       String headless,
                      ITestResult result) {
        boolean isHeadless = Boolean.parseBoolean(headless);

        // Set Allure environment info
        setAllureEnvironment(browser, isHeadless);

        DriverManager.setDriver(browser, isHeadless);
        DriverManager.getDriver().get(TestConstants.BASE_URL);

        // Log test start
        System.out.println("Starting test: " + result.getMethod().getMethodName());
        System.out.println("Browser: " + browser + " | Headless: " + isHeadless);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot("Test Failed: " + result.getMethod().getMethodName());
            attachLogs();
        }

        DriverManager.quitDriver();
        System.out.println("Test completed: " + result.getMethod().getMethodName());
    }

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] takeScreenshot(String screenshotName) {
        if (DriverManager.getDriver() != null) {
            return ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    @Attachment(value = "Test Logs", type = "text/plain")
    public String attachLogs() {
        return "Browser: " + System.getProperty("browser", "chrome") +
                "\nTest URL: " + DriverManager.getDriver().getCurrentUrl() +
                "\nTest Title: " + DriverManager.getDriver().getTitle();
    }

    private void setAllureEnvironment(String browser, boolean headless) {
        Allure.addAttachment("Browser", browser);
        Allure.addAttachment("Headless Mode", String.valueOf(headless));
        Allure.addAttachment("Base URL", TestConstants.BASE_URL);
        Allure.addAttachment("Test Environment", "QA");
    }
}