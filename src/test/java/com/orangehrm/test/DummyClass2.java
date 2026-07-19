package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.oranngeHRM.utilities.ExtentManager;

public class DummyClass2 extends BaseClass {

	@Test
	public void dummyTest() {
		//ExtentManager.startTest("Dummy Test2 Test"); --This has been implemented in TestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying the title");
		assert title.equals("OrangeHRM") : "Test Failed - Title is not matching";

		System.out.println("Test Passed - Title is matching");
		ExtentManager.logStep("Validation Successful");

	}

}
