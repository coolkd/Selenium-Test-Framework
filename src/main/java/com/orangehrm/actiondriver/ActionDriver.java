package com.orangehrm.actiondriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.oranngeHRM.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	// private static final Logger logger =
	// LoggerManager.getLogger(ActionDriver.class);
	private static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("WebDriver instance is created");
	}

	// method to enter text into an input field -- Avoid code duplication - fix the
	// multiple calling method
	public void enterText(By by, String value) {
		try {
			applyBorder(by, "green");
			waitForElementToBeVisible(by);
			// driver.findElement(by).clear();
			// driver.findElement(by).sendKeys(value);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on " + getElementDescription(by) + "-->" + value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("unable to enter the value:" + e.getMessage());
		}

	}

	// Method to get text from an input field
	public String getText(By by) {
		try {
			applyBorder(by, "green");
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get the text:" + e.getMessage());
			return "";
		}

	}

	// Method to compare two text -- change the return type
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				logger.info("Text validation PASSED for {} | Expected='{}' " + "| Actual='{}'",
						getElementDescription(by), expectedText, expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Text Comparison Failed!",
						"Text Verified Successfully! " + actualText + " equals " + expectedText);
				return true;
			} else {
				applyBorder(by, "red");
				logger.error("Text validation FAILED for {} | Expected='{}' | Actual='{}'", getElementDescription(by),
						expectedText, actualText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Compare Text",
						"Text Comparison Failed! " + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			logger.error("Unable to compare text for {}. Error: {}", getElementDescription(by), e.getMessage());
		}
		return false;

	}

	// Method to check if an element is displayed
	/*
	 * public boolean isDisplayed(By by) { try { waitForElementToBeVisible(by);
	 * boolean isDisplayed = driver.findElement(by).isDisplayed(); if (isDisplayed)
	 * { System.out.println("Element is visible"); return isDisplayed; } else {
	 * return isDisplayed; } } catch (Exception e) {
	 * System.out.println("element is not displayed:" + e.getMessage()); return
	 * false;
	 * 
	 * }
	 * 
	 * }
	 */

	// Simplified the method and remove redundant condition
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element is displayed " + getElementDescription(by));
			ExtentManager.logStep("Element is displayed: " + getElementDescription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed: ",
					"Element is displayed: " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Element is not displayed:" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed: ",
					"Element is not displayed: " + getElementDescription(by));
			return false;

		}

	}

	// Wait for page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded successfully");
		} catch (Exception e) {
			logger.error("Page did not laod within " + timeOutInSec + e.getMessage());
		}

	}

	// Scroll to an element
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to locate element:" + e.getMessage());
		}
	}

	// method to click element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked an element: " + elementDescription);
			logger.info("Clicked an element-->" + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("unable to click the element:" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "unable to click the element:",
					elementDescription + "_Unable to click");
			logger.error("Unable to click element");
		}

	}

	// Wait for element to be clickable
	public void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("element is not clickable:" + e.getMessage());
		}
	}

	// wait for element to be Visible
	public void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("Element is not visible:" + e.getMessage());
		}
	}

	// Method to get the description of an element using By locator

	public String getElementDescription(By locator) {

		if (driver == null) {
			return "Driver is null";
		}

		if (locator == null) {
			return "Locator is null";
		}

		try {
			WebElement element = driver.findElement(locator);

			String name = element.getDomProperty("name");
			String id = element.getDomProperty("id");
			String text = element.getText();
			String className = element.getDomProperty("class");
			String placeholder = element.getDomProperty("placeholder");

			if (isNotEmpty(name)) {
				return "Element with name: " + name;
			} else if (isNotEmpty(id)) {
				return "Element with id: " + id;
			} else if (isNotEmpty(text)) {
				return "Element with text: " + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with class: " + className;
			} else if (isNotEmpty(placeholder)) {
				return "Element with placeholder: " + placeholder;
			} else {
				return "Element located by: " + locator.toString();
			}

		} catch (Exception e) {
			logger.error("Unable to get element description for locator: {}", locator, e);
			return "Element not found for locator: " + locator.toString();
		}
	}

	private boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}

	// Utility Method to Border an element
	public void applyBorder(By by, String color) {
		try {
			// Locate the element
			WebElement element = driver.findElement(by);
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied the border with color " + color + " to element " + getElementDescription(by));
		} catch (Exception e) {

			logger.warn("Failed to apply the border to an element: " + getElementDescription(by), e);
		}

	}
	// =================Select Methods============

	// Method to select dropdown by visible text
	public void selectByVisibleText(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByVisibleText(value);
			applyBorder(by, "green");
			logger.info("Selected dropdown value: " + value);
		}

		catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to select dropdown value: " + value, e);

		}

	}

	// Method to select dropdown by value
	public void selectByValue(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByValue(value);
			applyBorder(by, "green");
			logger.info("Selected dropdown by value: " + value);
		}

		catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to select dropdown by value: " + value, e);

		}

	}

	// Method to select dropdown by index
	public void selectByIndex(By by, int index) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByIndex(index);
			applyBorder(by, "green");
			logger.info("Selected dropdown by index: " + index);
		}

		catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to select dropdown by index: " + index, e);
		}
	}

	// Method to get all options from dropdown
	public List<String> getDropDownOptions(By by) {
		List<String> optionsList = new ArrayList<>();
		try {
			WebElement dropdownElement = driver.findElement(by);
			Select select = new Select(dropdownElement);
			for (WebElement option : select.getOptions()) {
				optionsList.add(option.getText());
			}
			applyBorder(by, "green");
			logger.info("Retrieved dropdown options for: " + getElementDescription(by));

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get dropdown options: " + e.getMessage());
		}
		return optionsList;

	}

//===================JavaScript Utility Methods===================

//Method to click using JavaScript
	public void clickUsingJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			applyBorder(by, "green");
			logger.info("Clicked element using JavaScript: " + getElementDescription(by));
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to click using JavaScript: " + e);

		}

	}

	// Method to highlight an element using JavaScript
	public void highlightElementJS(By by) {
		try {
			WebElement element = driver.findElement(by);

			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid green';", element);
			applyBorder(by, "green");
			logger.info("Highlighted element using JavaScript: " + getElementDescription(by));

		} catch (Exception e) {

			applyBorder(by, "red");

			logger.error("Unable to highlight element using JavaScript: " + getElementDescription(by), e);
		}
	}

	// Method to scroll to the bottom of the page
	public void scrollToBottom() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");

		logger.info("Scrolled to the bottom of the page.");
	}

//============================Window and Frame Handling===============================

//Method to switch between browser windows
	public void switchToWindow(String windowTitle) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(windowTitle)) {
					logger.info("Switch to window: " + windowTitle);
					return;
				}
			}
			logger.warn("Window with title " + windowTitle + "not found.");
		} catch (Exception e) {
			logger.error("Unable to switch window", e);

		}
	}

	// Method to switch to iframe
	public void switchToFrame(By by) {
		try {
			driver.switchTo().frame(driver.findElement(by));
			logger.info("Switched to iframe: " + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to switch to iframe: " + getElementDescription(by), e);
			throw e; // Optional: rethrow if you want the test to fail
		}
	}

	// Method to switch back to the default content
	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
		logger.info("Switched back to the default content.");
	}

//================================Alert Handling===============================
	// Method to accept an alert popup
	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
			logger.info("Alert Accepted.");

		} catch (Exception e) {
			logger.error("No alert found to accept.", e);
		}
	}

	// Method to dismiss an alert popup
	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
			logger.info("Alert Dismissed.");

		} catch (Exception e) {
			logger.error("No alert found to dismiss.", e);
		}

	}

	public String getAlertText() {
		try {
			return driver.switchTo().alert().getText();

		} catch (Exception e) {
			logger.error("No alert text found.", e);
			return "";
		}
	}

//===========================Browser Actions=====================================
	public void refreshPage() {
		try {
			driver.navigate().refresh();
			ExtentManager.logStep("Page refreshed successfully.");
			logger.info("Page refreshed successfully.");
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to refresh page", e.getMessage());
			logger.error("Unable to refresh page: " + e);
		}
	}

	public String getCurrentURL() {
		try {
			String url = driver.getCurrentUrl();
			ExtentManager.logStep("Current URL fetched: " + url);
			logger.info("Current URL fetched: " + url);
			return url;
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to fetch current URL.", e.getMessage());
			logger.error("Unable to fetch current URl: " + e);
			return null;

		}
	}

	public void maximizeWindow() {
		try {
			driver.manage().window().maximize();
			ExtentManager.logStep("Browser window maximized.");
			logger.info("Browser window maximized.");
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to maximize window", e.getMessage());
			logger.error("Unable to maximize window: " + e);
		}

	}
	// =============================Advanced WebElement
	// Actions==============================

	public void moveToElement(By by) {

		String elementDescription = getElementDescription(by);

		try {

			WebElement element = driver.findElement(by);

			Actions actions = new Actions(driver);
			actions.moveToElement(element).perform();

			ExtentManager.logStep("Moved to element: " + elementDescription);
			logger.info("Moved to element: " + elementDescription);

		} catch (Exception e) {

			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to move to element", elementDescription);

			logger.error("Unable to move to element: " + elementDescription, e);

			throw e; // Optional
		}
	}

	public void dragAndDrop(By source, By target) {

		String sourceDescription = getElementDescription(source);
		String targetDescription = getElementDescription(target);

		try {

			WebElement sourceElement = driver.findElement(source);
			WebElement targetElement = driver.findElement(target);

			new Actions(driver).dragAndDrop(sourceElement, targetElement).perform();

			ExtentManager.logStep("Dragged element: " + sourceDescription + " and dropped on: " + targetDescription);

			logger.info("Dragged element: " + sourceDescription + " and dropped on: " + targetDescription);

		} catch (Exception e) {

			ExtentManager.logFailure(BaseClass.getDriver(),
					"Unable to drag and drop from '" + sourceDescription + "' to '" + targetDescription + "'",
					e.getMessage());

			logger.error("Unable to drag and drop from " + sourceDescription + " to " + targetDescription, e);

			throw e; // Optional but recommended
		}
	}

	public void doubleClick(By by) {

		String elementDescription = getElementDescription(by);

		try {

			WebElement element = driver.findElement(by);

			new Actions(driver).doubleClick(element).perform();

			ExtentManager.logStep("Double-clicked on element: " + elementDescription);
			logger.info("Double-clicked on element: " + elementDescription);

		} catch (Exception e) {

			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to double-click element: " + elementDescription,
					e.getMessage());

			logger.error("Unable to double-click element: " + elementDescription, e);

			throw e; // Optiona
		}
	}

	public void rightClick(By by) {
		String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.contextClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Right-clicked on element: " + elementDescription);
			logger.info("Right-clicked on element --> " + elementDescription);
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to right-click element: " + elementDescription,
					e.getMessage());

			logger.error("Unable to right-click element: " + elementDescription, e);

		}
	}

	public void sendKeysWithActions(By by, String value) {

		String elementDescription = getElementDescription(by);

		try {
			WebElement element = driver.findElement(by);

			new Actions(driver).sendKeys(element, value).perform();

			ExtentManager.logStep("Sent keys to element: " + elementDescription + " | Value: " + value);

			logger.info("Sent keys to element: " + elementDescription + " | Value: " + value);

		} catch (Exception e) {

			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to send keys to element: " + elementDescription,
					e.getMessage());

			logger.error("Unable to send keys to element: " + elementDescription, e);

			throw e;
		}
	}

	public void clearText(By by) {

		String elementDescription = getElementDescription(by);

		try {

			WebElement element = driver.findElement(by);
			element.clear();

			ExtentManager.logStep("Cleared text from element: " + elementDescription);
			logger.info("Cleared text from element: " + elementDescription);

		} catch (Exception e) {

			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to clear text from element: " + elementDescription,
					e.getMessage());

			logger.error("Unable to clear text from element: " + elementDescription, e);

			throw e; // Optional but recommended

		}
	}

	// Method to upload file
	public void uploadFile(By by, String filePath) {
		try {
			driver.findElement(by).sendKeys(filePath);
			applyBorder(by, "green");
			logger.info("Uploaded file: " + filePath);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to upload file: " + e.getMessage());
		}
	}

}
