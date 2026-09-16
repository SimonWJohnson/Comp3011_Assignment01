package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestClient;


// Handles communication between the Spring backend and the external speech-to-text Cloud service
// AudioController delegates transcription to --> TranscriptionService communicates with --> OpenAI
// TranscriptionService acts as a client when it sends another request to OpenAI
// @Service makes TranscriptionService a Spring managed Bean
// TranscriptionService will be injected into AudioController through its constructor
@Service
public class TranscriptionService {

	// Name the operating system environment variable containing the OpenAI key
	// Stores the the environment variable NAME only, NOT the key itself
	private static final String API_KEY_ENVIRONMENT_VARIABLE = "OPENAI_API_KEY";
	
	// OpenAI endpoint used to convert uploaded audio into text - where the request is sent
	private static final String TRANSCRIPTION_API_URL = "https://api.openai.com/v1/audio/transcriptions";
	
	// Speech-to-text model used by the transcription request - what processes the audio
	private static final String TRANSCRIPTION_MODEL = "gpt-40-mini-transcribe";
	
	// API key read dynamically from the operating system environment at runtime
	private final String apiKey;
	
	// HTTP client used by this service to send requests to the OpenAI API
	private final RestClient restClient;
	
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
		
		// Create the HTTP client used for outgoing requests to OpenAI
		// RestClient is safe for use from multiple threads 
		// So create the client once with the service and reuse it		
		this.restClient = RestClient.create();
	}
	
	// Receive the uploaded audio file from the AudioController
	// The OpenAI transcription request will be implemented here
	public String transcribe(MultipartFile audioFile) {
		
		// Temporary diagnostic message to confirm controller-to-service delegation
		// DO NOT log the API key or audio contents
		System.out.println("Audio received by TranscriptionService");
		
		return null;
		
	}
}
