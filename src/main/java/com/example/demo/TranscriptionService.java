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
	
	// HTTP client used by this service to send requests to the OpenAI API
	private final RestClient restClient;
	
	// Service used to store cumulative token usage
	private final GlobalStatsService statsService;
	
	// Constructor
	// Spring injects the shared GlobalStatsService instance when it creates the TranscriptionService
	public TranscriptionService(GlobalStatsService statsService) {
		
		// Store the injected GlobalStatsService so other methods in this class can access the global token counters
		// Completed transcription requests can update token totals
		this.statsService = statsService;
		
		// Create the HTTP client used for outgoing requests to OpenAI
		// RestClient is safe for use from multiple threads 
		// So create the client once with the service and reuse it		
		this.restClient = RestClient.create();
	}
	
	// Retrieve the OpenAI API key when a transcription request requires it
	// This allows the Spring application context to start without requiring
	// the API key until the external OpenAI service is actually used
	private String getApiKey() {
		
		// Read the API key dynamically from the operating system environment
		String apiKey = System.getenv(API_KEY_ENVIRONMENT_VARIABLE);
		
		// Validate that the required environment variable exists and contains a value
		if(apiKey == null || apiKey.isBlank()) {
			
			// Identify the missing configuration without exposing any secret value
			throw new IllegalStateException("OPENAI_API_KEY environment variable is not configured.");
		}
		
		// Return the key for server-side authentication with OpenAI
		return apiKey;
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
				// Validate runtime configuration at the point where that configuration is actually required
				.headers(headers -> headers.setBearerAuth(getApiKey()))
				
				// Tell OpenAI that the request body contains multiple form-data parts
				.contentType(MediaType.MULTIPART_FORM_DATA)
				
				// Supply the model and audio parts created above
				// Creates the multipart body
				.body(requestBody)
				
				// Execute the HTTP request and begin processing the HTTP response
				.retrieve()
				
				// Convert the OpenAI JSON response into the Java response DTO
				.body(OpenAiTranscriptionResponse.class);
		
		// Record the token usage returned by OpenAI
		// These values are added to the cumulative totals for the lifetime of the current server process
		// These fields belong to the Spring managed GlobalStatsService, and begin at zero when the application process restarts
		statsService.addTokens(
				response.usage().inputTokens(),
				response.usage().outputTokens()
				);
		

		// Return the transcription text extracted from the OpenAI response
		return response.text();
		
	}
}
