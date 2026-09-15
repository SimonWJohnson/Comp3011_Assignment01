package com.example.demo;

public class ErrorResponse {

	// UTC timestamp showing when the error occurred
	private String timestamp;
	
	// HTTP status code asscociated with the error
	private int status;
	
	// Standard HTTP error description
	private String error;
	
	// Human-readable description of the specific error
	private String message;
	
	// API endpoint at which the error occurred
	private String path;
	
	// Constructor
	// Create the ErrorResponse containing all required info to describe an unsuccessful API request
	public ErrorResponse(String timestamp, int status, String error, String message, String path) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}
	
	// Getters
	// UTC timestamp
	public String getTimestamp() {
		return timestamp;
	}
	
	// HTTP status code
	public int getStatus() {
		return status;
	}

	// Standard HTTP error description
	public String getError() {
		return error;
	}

	// Human-readable error message
	public String getMessage() {
		return message;
	}

	// API path at which error occurred
	public String getPath() {
		return path;
	}
}
