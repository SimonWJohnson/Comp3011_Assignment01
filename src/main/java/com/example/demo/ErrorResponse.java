package com.example.demo;

public class ErrorResponse {

	// Store the error message returned to the client
	private String error;
	
	// Constructor
	// Create the ErrorResponse containing the supplied error message
	public ErrorResponse(String error) {
		this.error = error;
	}
	
	// Getter
	public String getError() {
		return error;
	}
}
