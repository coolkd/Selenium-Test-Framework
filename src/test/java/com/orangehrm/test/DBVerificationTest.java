package com.orangehrm.test;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.oranngeHRM.utilities.DBConnection;
import com.oranngeHRM.utilities.DataProviders;
import com.oranngeHRM.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());

	}

	@Test(dataProvider = "emplVerification", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameVerificationFromDB(String empID, String empName) {
		ExtentManager.logStep("Login with admin credentials");
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));

		SoftAssert softAssert = getSoftAssert();

		ExtentManager.logStep("Click on PIM Tab");
		homePage.clickOnPIMTab();

		ExtentManager.logStep("Search for employee");
		homePage.employeeSearch(empName);

		ExtentManager.logStep("Get the employee name from the DB");
		String employee_id = empID;

		// Fetch the data into a Map

		Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);

		String emplFirstName = employeeDetails.get("firstName");
		String emplMiddleName = employeeDetails.get("middleName");
		String emplLastName = employeeDetails.get("lasttName");

		String emplfirstAndMiddleName = (emplFirstName + " " + emplMiddleName).trim();

		// Validation for first and middle name
		ExtentManager.logStep("Verify the employee first and middle name");
		softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(emplfirstAndMiddleName),
				"First and Middle name are not matching");

		// Validation for last name
		ExtentManager.logStep("Verify the employee last name");
		softAssert.assertTrue(homePage.verifyEmployeeLastName(emplLastName));

		ExtentManager.logStep("DB Validation Completed");

		softAssert.assertAll();
	}

}
