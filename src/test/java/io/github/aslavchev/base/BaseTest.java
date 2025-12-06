package io.github.aslavchev.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * BaseTest - Foundation for all test classes
 *
 * Manages WebDriver lifecycle and provides common test utilities:
 * - WebDriverManager handles driver binaries automatically
 * - @BeforeMethod ensures test independence (fresh browser per test)
 * - Screenshot captured on failure and attached to Allure report
 */
public class BaseTest {

    protected WebDriver driver;
    protected static final String BASE_URL = "https://automationexercise.com";

    @BeforeMethod
    public void setUp() {
        // Browser selection via system property: -Dbrowser=chrome/firefox
        String browser = System.getProperty("browser", "chrome").toLowerCase();

        // Detect CI environment (GitHub Actions sets CI=true)
        boolean isCI = System.getenv("CI") != null;

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                if (isCI) {
                    firefoxOptions.addArguments("--headless");
                    firefoxOptions.addArguments("--no-sandbox");
                    firefoxOptions.addArguments("--disable-dev-shm-usage");
                    firefoxOptions.addArguments("--window-size=1920,1080");
                }

                firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();

                if (isCI) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--window-size=1920,1080");
                }

                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--start-maximized");
                driver = new ChromeDriver(chromeOptions);
                break;
        }

        System.out.println("🌐 Browser: " + browser);

        // Handle consent popup if it appears
        handleConsentPopup();

    }


    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(result.getName());
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @Attachment(value = "Screenshot: {testName}", type = "image/png")
    public byte[] captureScreenshot(String testName) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    protected void navigateToHomePage() {
        driver.get(BASE_URL);
    }

    private void handleConsentPopup() {
        try {
            // Navigate to any page first
            driver.get(BASE_URL);

            // Wait for consent button and click it
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement consentButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector("button.fc-button.fc-cta-consent.fc-primary-button")
                    )
            );
            consentButton.click();

        } catch (Exception e) {
            // If not found, remove popup
            ((JavascriptExecutor) driver).executeScript(
                    "document.querySelectorAll('.fc-dialog, .fc-dialog-overlay, .fc-consent-root').forEach(e => e.remove());"
            );
        }
    }
}
