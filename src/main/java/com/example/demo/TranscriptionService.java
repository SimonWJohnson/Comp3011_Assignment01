package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.io.IOException;

// Handles communication between the Spring backend and the external speech-to-text Cloud service
// AudioController delegates transcription to --> TranscriptionService communicates with --> OpenAI
// TranscriptionService acts as a client when it sends another request to OpenAI
// @Service makes TranscriptionService a Spring managed Bean
// TranscriptionService will be injected into AudioController through its constructor
@Service
public class TranscriptionService {

	// Name the operating system environment variable containing the OpenAI key
	// Stores the environment variable NAME only, NOT the key itself
	private static final String API_KEY_ENVIRONMENT_VARIABLE = "OPENAI_API_KEY";
	
	// OpenAI endpoint used to convert uploaded audio into text - where the request is sent
	private static final String TRANSCRIPTION_API_URL = "https://api.openai.com/v1/audio/transcriptions";
	
	// Speech-to-text model used by the transcription request - what processes the audio
	private static final String TRANSCRIPTION_MODEL = "gpt-4o-mini-transcribe";
	
	// API key read dynamically from the operating system environment at runtime
	private final String apiKey;
	
	// HTTP client used by this service to send requests to the OpenAI API
	private final RestClient restClient;
	
	// Service used to store cumulative token usage
	private final GlobalStatsService statsService;
	
	// Constructor
	// Spring injects the shared GlobalStatsService instance when it creates the TranscriptionService
	public TranscriptionService(GlobalStatsService statsService) {
		
		// Store the injected GlobalStatsService so other methods in this class can access the global token counters
		// COmpleted transcription requests can update token totals
		this.statsService = statsService;
		
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
	// IOException may occur while reading the uploaded audio file
	// If this occurs, the exception propagates to the global exception handler
	public String transcribe(MultipartFile audioFile) throws IOException {
				
		// Build the multipart/form-data body required by the OpenAI transcription API
		// The outgoing request body is a multi-value map because a multipart form contains named parts
		// The 'model' part contains the model name
		// The 'file' part contains the audio resource
		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
		
		// Tell OpenAI which STT model should process the recording
		requestBody.add("model", TRANSCRIPTION_MODEL);
		
		// Convert the uploaded MultipartFile into a Resource that can be attached as the file part of the outgoing multipart HTTP request
		ByteArrayResource audioResource = new ByteArrayResource(audioFile.getBytes()) {
			
			// Preserve the original filename and extension so OpenAI can identify the audio format, extension-bearing filename, and appropriate content type
			@Override
			public String getFilename() {
				return audioFile.getOriginalFilename();
			}
		};
		
		// Add the recorded audio as the 'file' part of the multipart request
		requestBody.add("file", audioResource);
		
		// Send the multipart transcription request to the OpenAI API
		//String response = restClient.post()
		OpenAiTranscriptionResponse response = restClient.post()
				
				// Outgoing HTTP request destination
				.uri(TRANSCRIPTION_API_URL)
				
				// Authenticate server-side request using the API key
				// Outgoing authentication header required by OpenAI, created only for the Spring to OpenAI request
				// The key remains on the backend and is never exposed / sent to the browser
				.headers(headers -> headers.setBearerAuth(apiKey))
				
				// Tell OpenAI that the request body contains multiple form-data parts
				.contentType(MediaType.MULTIPART_FORM_DATA)
				
				// Supply the model and audio parts created above
				// Creates the multipart body
				.body(requestBody)
				
				// Execute the HTTP request and begin processing the HTTP response
				.retrieve()
				
				// Temporarily receive the OpenAI JSON response as a String
				// This will be replaced with a legitimate Java response DTO
				//.body(String.class);
				.body(OpenAiTranscriptionResponse.class);
		
		//return null;
		// Return the transcription text extracted from the OpenAi response
		return response.text();
		
	}
}
