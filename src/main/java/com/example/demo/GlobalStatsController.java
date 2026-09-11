package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/global")
public class GlobalStatsController {
	
	// Enable controller to access GlobalStatsService via dependency injection
	private final GlobalStatsService statsService;
	
	// Constructor
	public GlobalStatsController(GlobalStatsService statsService) {
		this.statsService = statsService;
	}
	
	@GetMapping("/stats")
	public GlobalStatsResponse getGlobalStats() {
		return new GlobalStatsResponse(
				statsService.getInputTokens(), 
				statsService.getOutputTokens()
				);
	};
}
