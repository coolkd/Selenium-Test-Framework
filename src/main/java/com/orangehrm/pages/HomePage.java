package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {

	private ActionDriver actionDriver;

	// Define locators using By class
	private By adminTab = By.xpath("//li[1]//a[1]//span[1]");
	private By userIdButton = By.xpath("//p[@class='oxd-userdropdown-name']");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");

	private By pimTab = By.xpath("//span[text()='PIM']");
	private By employeeSearch = By
			.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
	private By searchButton = By.xpath("//button[text()=' Search ']");
	private By employeeFirstAndMiddleName = By.xpath("//div[@class='oxd-table-card']/div/div[3]");
	private By employeeLastname = By.xpath("//div[@class='oxd-table-card']/div/div[4]");

	// Initialize the ActionDriver object by passing WebDriver instance
	/*
	 * public HomePage(WebDriver driver) { this.actionDriver = new
	 * ActionDriver(driver); }
	 */

	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	// Method to verify if admin tab is visible
	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
	}

	// Verify orangeHRM Logo to be displayed
	public boolean verifyOrangeHRMLogo() {
		return actionDriver.isDisplayed(orangeHRMLogo);
	}

	// Method to navigate to PIM tab
	public void clickOnPIMTab() {
		actionDriver.click(pimTab);
	}

	// Employee Search
	public void employeeSearch(String value) {
		actionDriver.enterText(employeeSearch, value);
		actionDriver.click(searchButton);
		actionDriver.scrollToElement(employeeFirstAndMiddleName);

	}

	// Verify employee first and middle name
	public boolean verifyEmployeeFirstAndMiddleName(String emplFirstAndMiddleNameFromDB) {
		return actionDriver.compareText(employeeFirstAndMiddleName, emplFirstAndMiddleNameFromDB);
	}

	// Verify employee last name
	public boolean verifyEmployeeLastName(String emplLastNameFromDB) {
		return actionDriver.compareText(employeeLastname, emplLastNameFromDB);
	}

	// Method to perform logout operation
	public void logout() {
		actionDriver.click(userIdButton);
		actionDriver.click(logoutButton);
	}

}
