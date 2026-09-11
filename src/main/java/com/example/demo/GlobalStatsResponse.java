package com.example.demo;

public class GlobalStatsResponse {
	
	private long inputTokens;
	private long outputTokens;

	
	// Constructor
	public GlobalStatsResponse(long inputTokens, long outputTokens) {
		this.inputTokens = inputTokens;
		this.outputTokens = outputTokens;
	}
	
	// Getters
	public long getInputTokens() {
		return inputTokens;
	}
	
	public long getOutputTokens() {
		return outputTokens;
	}
}
