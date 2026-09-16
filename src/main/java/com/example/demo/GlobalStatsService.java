package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicLong;

// Create and manage an instance of this class for the application
// Spring services are singleton-scoped by default
// Token counters persist while the server process is live

@Service
public class GlobalStatsService {
	
	// Store cumulative token usage for the lifetime of the current server process
	// Thread-safe counters used to store cumulative token usage
	// AtomicLong prevents concurrent requests from losing updates 
	// when multiple transcriptions complete at the same time
	private final AtomicLong inputTokens = new AtomicLong(0);
	private final AtomicLong outputTokens = new AtomicLong(0);
	
	// Getters
	// Get the current value stored in the thread-safe counter
	public long getInputTokens() {
		return inputTokens.get();
	}
	
	// Get the current value stored in the thread-safe counter
	public long getOutputTokens() {
		return outputTokens.get();
	}
	
	// Method to increment token count
	// Atomically add this transcription's usage to the cumulative totals so concurrent updates are not lost
	public void addTokens(long inputTokens, long outputTokens) {
		// Atomically add this request's token usage to the shared counters
		this.inputTokens.addAndGet(inputTokens);
		this.outputTokens.addAndGet(outputTokens);
	}
}
