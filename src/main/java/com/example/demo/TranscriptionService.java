package com.example.demo;

import org.springframework.stereotype.Service;


// Handles communication between the Spring backend and the external speech-to-text Cloud service
// AudioController delegates transcription to --> TranscriptionService communicates with --> OpenAI
// @Service makes TranscriptionService a Spring managed Bean
// TranscriptionService will be injected into AudioController through its constructor
@Service
public class TranscriptionService {

	// Name the operating system environment variable containing the OpenAI key
	// Stores the the environment variable NAME only, NOT the key itself
	private static final String API_KEY_ENVIRONMENT_VARIABLE = "OPENAI_API_KEY";
	
	// API key read dynamically from the operating system environment at runtime
	private final String apiKey;
	
	public TranscriptionService() {
		
		// Read the OpenAI API key dynamically from the operating system environment (runtime retrieval)
		// The API key must NEVER be hard-coded, logged, returned to clients, or sent to the browser
		this.apiKey = System.getenv(API_KEY_ENVIRONMENT_VARIABLE);
		
		// Fail safely if the required environment variable is not available
		// variable doesn't exist || variable exists but is empty
		if(this.apiKey == null || this.apiKey.isBlank()) {
			throw new IllegalStateException(
					// Missing configuration is named, but the value is not exposed
					"OPENAI_API_KEY environment variable is not configured."
					);
		}
	}
}
