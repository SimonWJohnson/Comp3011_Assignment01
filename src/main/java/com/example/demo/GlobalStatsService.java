package com.example.demo;

import org.springframework.stereotype.Service;

// Create and manage an instance of this class for the application
// Spring services are singleton-scoped by default
// Token counters persist while the server process is live

@Service
public class GlobalStatsService {
	
	// Store cumulative token usage for the lifetime of the current server process
	private long inputTokens = 0;
	private long outputTokens= 0;
	
	// Getters
	// Get the current cumulative input token count
	public long getInputTokens() {
		return inputTokens;
	}
	
	// Get the current cumulative output token count
	public long getOutputTokens() {
		return outputTokens;
	}
	
	// Method to increment token count
	// Add token usage from a completed speech-to-text request to the global totals
	public void addTokens(long inputTokens, long outputTokens) {
		this.inputTokens += inputTokens;
		this.outputTokens += outputTokens;
	}
}
