package com.orangehrm.test;


import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.oranngeHRM.utilities.ExtentManager;

public class DummyClass extends BaseClass {

	@Test
	public void dummyTest() {
		//Test Checkin
		//ExtentManager.startTest("Dummy Test1 Test"); --This has been implemented in TestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("Verifying the title");
		assert title.equals("OrangeHRM") : "Test Failed - Title is not matching";

		System.out.println("Test Passed - Title is matching");
		//ExtentManager.logSkip("This case is skipped");
		//throw new SkipException("Skipping the test as part of testing");

	}

}
