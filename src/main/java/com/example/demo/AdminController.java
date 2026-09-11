package com.example.demo;

import java.time.Duration;
import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
	
	private final Instant serverStart = Instant.now();
	
	@GetMapping("/uptime")
	public UptimeResponse getServerUpTime(){
		
		Instant now = Instant.now();
		double uptimeSeconds = Duration.between(serverStart, now).toMillis() / 1000.0;
		
		return new UptimeResponse(serverStart.toString(), now.toString(), uptimeSeconds);
	}
	
}
