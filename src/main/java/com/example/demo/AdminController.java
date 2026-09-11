package com.example.demo;

import java.time.Duration;
import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
	
	// Record the server start time when this controller is created
	private final Instant serverStart = Instant.now();
	
	// Handle GET requests to /api/v1/admin/uptime
	@GetMapping("/uptime")
	public UptimeResponse getServerUpTime(){
		
		// Record the current UTC time when the request is processed
		Instant now = Instant.now();
		
		// Calculate the elapsed server runtime in milliseconds
		// Convert the result to seconds
		double uptimeSeconds = Duration.between(serverStart, now).toMillis() / 1000.0;
		
		// Return the server timing info as an UptimeResponse object
		// Spring will automatically serialise this object into JSON
		return new UptimeResponse(serverStart.toString(), now.toString(), uptimeSeconds);
	}
	
}
