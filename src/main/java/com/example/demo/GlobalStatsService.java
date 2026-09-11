package com.example.demo;

import org.springframework.stereotype.Service;

// Create and manage an instance of this class for the application
// Spring services are singleton-scoped by default
// Token counters persist while the server process is live

@Service
public class GlobalStatsService {
	
	private long inputTokens = 0;
	private long outputTokens= 0;
	
	// Getters
	public long getInputTokens() {
		return inputTokens;
	}
	
	public long getOutputTokens() {
		return outputTokens;
	}
	
	// Method to increment token count
	public void addTokens(long inputTokens, long outputTokens) {
		this.inputTokens += inputTokens;
		this.outputTokens = outputTokens;
	}
}
