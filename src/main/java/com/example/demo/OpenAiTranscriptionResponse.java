package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

// Java Data Transfer Object (DTO)
// Represents the JSON response returned by the OpenAI transcription API
// Models the transcription text and token usage required by the application
public record OpenAiTranscriptionResponse (String text, Usage usage){

	// Represents token usage information returned with the transcription
	public record Usage(
			
			@JsonProperty("input_tokens")
			long inputTokens,
			
			@JsonProperty("output_tokens")
			long outputTokens
			
			) {
		
	}

}
