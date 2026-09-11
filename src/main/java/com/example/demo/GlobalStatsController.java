package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/global")
public class GlobalStatsController {
	
	// Service containing the global token usage state
	private final GlobalStatsService statsService;
	
	// Constructor
	// Enable controller to access GlobalStatsService via dependency injection
	public GlobalStatsController(GlobalStatsService statsService) {
		this.statsService = statsService;
	}
	
	// Handle GET requests to /api/v1/global/stats
	@GetMapping("/stats")
	public GlobalStatsResponse getGlobalStats() {
		
		// Return the current token totals as asGlobalResponse object;
		// Spring automatically serialises this object into JSON
		return new GlobalStatsResponse(
				statsService.getInputTokens(), 
				statsService.getOutputTokens()
				);
	};
}
