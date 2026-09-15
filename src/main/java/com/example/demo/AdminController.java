package com.example.demo;

import java.time.Duration;
import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
	
	// Record the server start time when this controller is created
	private final Instant serverStart = Instant.now();
	
	// Create context for shutdown POST request
	private final ConfigurableApplicationContext context;
	
	// Track whether a graceful shutdown has already been requested
	// AtomicBoolean allows multiple request threads to safely check and update this value without creating a race condition
	// Check whether AtomicBoolean is false - if it is, change it to true as one indivisible operation
	private final AtomicBoolean shutdownRequested = new AtomicBoolean(false);
	
	// Constructor
	// Enable the controller to access the Spring app context via dependency injection
	public AdminController(ConfigurableApplicationContext context) {
		this.context = context;
	}
	
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
	
	// Pseudocode
	/*
	 * POST /shutdown
	 * Server accepts request
	 * 202 response returned
	 * {"message":"Graceful shutdown requested"}
	 * Spring app begins graceful shutdown
	 * App exits
	 * */
	
	// Handle POST requests to /api/v1/admin/shutdown
	@PostMapping("/shutdown")
	//public ResponseEntity<ShutdownResponse> shutdownServer(){
		// ResponseEntity = Entire HTTP response including status code
		// ShutdownResponse = Body of HTTP response
	public ResponseEntity<?> shutdownServer(){
		// ? wildcard allows the method to return two different body types:
			// 202 ShutdownResponse
			// 409 ErrorResponse
		
		// Atomically check whether shutdown has already been requested
		// Change the value from false to true if this is the first request
		if(!shutdownRequested.compareAndSet(false, true)) {
			// compareAndSet returned false, meaning the value was already true
			// A shutdownRequest is therefore already being processed
			// Create and return the ErrorResponse as per API contract
			return ResponseEntity.status(409).body(new ErrorResponse(
					Instant.now().toString(),
					409,
					"Conflict",
					"Graceful shutdown is already in progress.",
					"/api/v1/admin/shutdown"
					));
		}
		
		// Create the response before beginning shutdown process
		ShutdownResponse response = new ShutdownResponse("Graceful shutdown requested.");
		
		// Begin shutdown shortly after the HTTP response is returned
		// Create a separate thread to perform the shutdown
		Thread shutdownThread = new Thread(() -> {
			
			try {
				// Pause this shutdown thread for 500ms to give the current HTTP request time to return a response to the client
				Thread.sleep(500);
			}
			catch(InterruptedException e) {
				// If the shutdown thread is interrupted while sleeping, restore its interrupted status
				Thread.currentThread().interrupt();
			}
			
			// Close the Spring app context to begin the shutdown process
			context.close();		
		});
		
		// Start the shutdown thread
		// Executed independently from the current HTTP request thread
		shutdownThread.start();
		
		// Return HTTP status 202 Accepted
		// Return ShutdownResponse object as JSON to acknowledge shutdown request
		return ResponseEntity.accepted().body(response);
	}
	
}
