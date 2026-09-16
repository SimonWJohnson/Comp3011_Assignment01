package com.example.demo;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;



// Provide for centralised exception handling for REST controllers
//Converts unexpected exceptions into the standard ErrorResponse as per API contract
@RestControllerAdvice // watches all controllers for exceptions
public class GlobalExceptionHandler {
	
	// Handle unexpected exceptions that are not handled elsewhere
	@ExceptionHandler(Exception.class) // handles anything derived from the general Exception class
	public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception exception, HttpServletRequest request) {
				
		// Create the standard error response required by API contract
		ErrorResponse response = new ErrorResponse(
				Instant.now().toString(), 500, "Internal Server Error", "An unexpected server error occurred.", request.getRequestURI()
				);
		// Return HTTP status 500 with ErrorResponse serialised as JSON
		return ResponseEntity.status(500).body(response);
	}
	
}
