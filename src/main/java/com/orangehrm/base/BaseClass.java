package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.lang.reflect.Method;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.oranngeHRM.utilities.ExtentManager;
import com.oranngeHRM.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	// protected static WebDriver driver;
	// private static ActionDriver actionDriver;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	// Getter method for soft assert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	@BeforeSuite
	public void loadConfig() throws IOException {

		// Load Configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");

		// Start the Extent Report
		// ExtentManager.getReporter(); ---This has been implemented in TestListener

	}

	@BeforeMethod
	public void setup(Method method) throws IOException {
		String testName = method.getName();
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test execution started: " + testName);
		logger.info("Setting up webDriver for:" + this.getClass().getSimpleName(), testName);
		launchBrowser();
		configureBrowser();
		staticWait(2);

		logger.info("WebDriver Initialized and Browser Maximized");
		logger.trace("This is Trace Message");
		// logger.error("This is Error Message");
		logger.debug("This is debug message");
		// logger.fatal("This is fatal message");
		// logger.warn("This is warn message");

		/*
		 * // Initialize the actionDriver only once if (actionDriver == null) {
		 * actionDriver = new ActionDriver(driver);
		 * logger.info("ActionDriver instance is created. Thread ID: {}",
		 * Thread.currentThread().threadId()); }
		 */

		// Initialize ActionDriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info(("ActionDriver initialized for thread:" + Thread.currentThread().threadId()));
		ExtentManager.logStep("Browser launched and configured successfully");

	}

	// Initialize the WebDriver based on browser defined in config.properties file
	private synchronized void launchBrowser() {

		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {

			// Create ChromeOptions
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless=new"); // Run code in headless mode
			// Optional recommended arguments
			options.addArguments("--disable-gpu");
			options.addArguments("--window-size=1920,1080");
			options.addArguments("--disable-notifications");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-dev-shm-usage");
            
			//driver.set(new ChromeDriver());
			driver.set(new ChromeDriver(options));

			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is Created");

		}

		else if (browser.equalsIgnoreCase("firefox")) {

			driver.set(new FirefoxDriver());

			// Create FirefoxOptions
			// FirefoxOptions options = new FirefoxOptions();
			// options.addArguments("--headless"); // Run code in headless mode
			// options.addArguments("--disable gpu"); // Disable GPU for headless mode
			// options.addArguments("window-size=1920,1080"); //Set window size
			// options.addArguments("--disable notifications"); // Disable browser
			// notification
			// options.addArguments("--no-sandbox"); // Required for some CI/CD environment
			// list
			// options.addArguments("--disable-dev-shm-usage"); // Resolve issue in
			// resource-limited
			driver.set(new FirefoxDriver()); // New changes as per Thread
			//driver.set(new FirefoxDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Instance is Created");

		}

		else if (browser.equalsIgnoreCase("edge")) {
			driver.set(new EdgeDriver());

			// Create EdgeOptions
			// EdgeOptions options = new EdgeOptions();
			// options.addArguments("--headless"); // Run code in headless mode
			// options.addArguments("--disable gpu"); // Disable GPU for headless mode
			// options.addArguments("window-size=1920,1080"); //Set window size
			// options.addArguments("--disable notifications"); // Disable browser
			// notification
			// options.addArguments("--no-sandbox"); // Required for some CI/CD environment
			// list
			// options.addArguments("--disable-dev-shm-usage"); // Resolve issue in
			// resource-limited
			driver.set(new EdgeDriver()); // New changes as per Thread
			ExtentManager.registerDriver(getDriver());
			//driver.set(new EdgeDriver(options));
			logger.info("EdgeDriver Instance is Created");
		}

		else {
			throw new IllegalArgumentException("Browser not supported:" + browser);
		}

	}

	// Configure browser setting such as implicit wait, maximize the browser, and
	// navigate to the URL.
	private void configureBrowser() {

		// Impicit Wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// Maximize the browser
		getDriver().manage().window().maximize();

		// Navigate to URL
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			logger.error("Failed to Navigate to the URL:" + e.getMessage());
		}

	}

	@AfterMethod
	public synchronized void tearDown() {

		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				logger.error("Unable to quit the browser:" + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed");
		driver.remove();
		actionDriver.remove();
		// driver = null;
		// actionDriver = null;
		// ExtentManager.endTest(); -- This has been implemented in TestListener
	}

	// Getter method for prop
	public static Properties getProp() {
		return prop;
	}
	/*
	 * //Driver getter method public WebDriver getDriver() { return driver; }
	 */

	// Getter Method for WebDriver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			logger.error("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();

	}

	// Getter Method for ActionDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			logger.error("actionDriver is not initialized");
			throw new IllegalStateException("actionDriver is not initialized");
		}
		return actionDriver.get();

	}

	// Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}
