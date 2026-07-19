package com.oranngeHRM.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtility {

	// Method to send the get request

	public static Response sendGetRequest(String endPoint) {
		return RestAssured.given().when().get(endPoint);

	}

	// Method to send the post request
	public static void sendPostRequest(String endPoint, String payLoad) {
		RestAssured.given().header("Content-Type", "application/json")
		.body(payLoad)
		.when()
		.post(endPoint);

	}

	// Method to validate the response status
	public static boolean validateStatusCode(Response response, int statusCode) {
		return response.getStatusCode() == statusCode;

	}

	// Method to extract value from JSON response
	public static String getJsonValue(Response response, String value) {
		return response.jsonPath().getString(value);

	}

}
