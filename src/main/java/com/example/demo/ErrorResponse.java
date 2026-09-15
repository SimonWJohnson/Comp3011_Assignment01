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
	public String getTimestamp() {
		return timestamp;
	}

	public int getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}
}
