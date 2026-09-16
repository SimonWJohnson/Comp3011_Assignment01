package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class GlobalStatsControllerTests {

	@Test
	void getGlobalStatsReturnsCurrentTokenTotals() {
		
		// Create a mock GlobalStatsService
		GlobalStatsService statsService = mock(GlobalStatsService.class);
		
		//Define the token totals the mock service should return
		when(statsService.getInputTokens()).thenReturn(123L);
		when(statsService.getOutputTokens()).thenReturn(45L);
		
		// Create the controller and inject the mock service
		GlobalStatsController controller = new GlobalStatsController(statsService);
		
		// Call the controller method
		GlobalStatsResponse response = controller.getGlobalStats();
		
		// Verify that the controller returned the expected token totals
		assertEquals(123L, response.getInputTokens());
		assertEquals(45L, response.getOutputTokens());
	}
}
