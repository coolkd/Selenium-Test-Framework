package com.oranngeHRM.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;

public class DBConnection {

	private static final String DB_URL = "jdbc:mysql://localhost:3306/rahulshettyacademy";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "KuldipMysql@2026";
	private static final Logger logger = BaseClass.logger;

	public static Connection getDBDonnection() {
		try {
			logger.info("Starting DB Connection...");
			Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			logger.info("DB Connection Successful");
			return conn;
		} catch (SQLException e) {
			logger.error("Error while stablishing the DB connection");
			e.printStackTrace();
			return null;
		}

	}

	// Get the employee details from DB and store in a map
	public static Map<String, String> getEmployeeDetails(String employee_id) {
		String query = "SELECT emp_firstname, emp_middle_name, emp_lastname "
				+ "FROM hs_hr_employee WHERE employee_id = " + employee_id;

		Map<String, String> employeeDetails = new HashMap<>();

		try (Connection conn = getDBDonnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			logger.info("Executing query: " + query);
			if (rs.next()) {
				String firstName = rs.getString("emp_firstname");
				String middleName = rs.getString("emp_middle_name");
				String lasttName = rs.getString("emp_lastname");

				// Store in a Map
				employeeDetails.put("firstName", firstName);
				employeeDetails.put("middleName", middleName != null ? middleName : "");
				employeeDetails.put("lasttName", lasttName);

				logger.info("Query Executed Successfully");
				logger.info("Employee Data Fetched: " + employeeDetails);
			} else {
				logger.error("Employee Not Found");
			}

		} catch (Exception e) {
			logger.error("Error while executing query");
			e.printStackTrace();

		}
		return employeeDetails;

	}

}
