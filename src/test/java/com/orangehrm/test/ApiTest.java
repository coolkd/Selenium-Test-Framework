package com.orangehrm.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.oranngeHRM.utilities.ApiUtility;
import com.oranngeHRM.utilities.ExtentManager;

import io.restassured.response.Response;

public class ApiTest {

	@Test
	public void verifyGetUserAPI() {

		SoftAssert softAssert = new SoftAssert();

		// Step1: Define API end point
		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API EndPoint: " + endPoint);

		// Step2: Send GET Request
		ExtentManager.logStep("Sending GET Request to the API");
		Response response = ApiUtility.sendGetRequest(endPoint);

		// Step3: Validate Status Code
		ExtentManager.logStep("Validating API Response Status Code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);
		softAssert.assertTrue(isStatusCodeValid, "Status code is not as expected");
		if (isStatusCodeValid) {
			ExtentManager.logStepValidationForAPI("Status Code Validation Passed!");
		} else {
			ExtentManager.logFailureAPI("Status Code Validation Failed!");
		}

		// Step4: Validate User Name
		ExtentManager.logStep("Validating response body for user name");
		String userName = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(userName);
		softAssert.assertTrue(isUserNameValid, "User name not valid");
		if (isUserNameValid) {
			ExtentManager.logStepValidationForAPI("Username Validation Passed");
		} else {
			ExtentManager.logFailureAPI("Username Validation Failed");
		}
		// Step 5: Validate email
		String expectedEmail = "Sincere@april.biz";
		String actualEmail = ApiUtility.getJsonValue(response, "email");

		ExtentManager.logStep("Validating email | Expected='" + expectedEmail + "' | Actual='" + actualEmail + "'");

		boolean isEmailValid = expectedEmail.equalsIgnoreCase(actualEmail);

		softAssert.assertTrue(isEmailValid,
				"Email validation failed. Expected='" + expectedEmail + "', Actual='" + actualEmail + "'");

		if (isEmailValid) {
			ExtentManager.logStepValidationForAPI(
					"Email Validation Passed | Expected='" + expectedEmail + "' | Actual='" + actualEmail + "'");
		} else {
			ExtentManager.logFailureAPI(
					"Email Validation Failed | Expected='" + expectedEmail + "' | Actual='" + actualEmail + "'");
		}

		softAssert.assertAll();
	}
}