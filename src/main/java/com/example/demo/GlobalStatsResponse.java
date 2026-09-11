package com.example.demo;

// Response object used to represent global token usage stats as JSON
public class GlobalStatsResponse {
	
	private long inputTokens; // Total number of input tokens consumed since the server started
	private long outputTokens; // Total number of output tokens produced since the server started

	
	// Constructor
	// Create a global statistics response containing the current token totals
	public GlobalStatsResponse(long inputTokens, long outputTokens) {
		this.inputTokens = inputTokens;
		this.outputTokens = outputTokens;
	}
	
	// Getters
	// Get the total number of input tokens consumed
	public long getInputTokens() {
		return inputTokens;
	}
	
	// Get the total number of output tokens produced
	public long getOutputTokens() {
		return outputTokens;
	}
}
