package com.example.rest.utils;

public class Utils {
	
	/*
	 * common method to generate json format for REST response
	 */
	public static String generateJsonResponse(String msg) {
		return "{\"data\": " + msg + "}";
	}
}
