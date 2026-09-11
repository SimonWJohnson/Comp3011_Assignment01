package com.example.demo;

// Response object used to represent server uptime information as JSON
// Returns server start time, current UTC time, and uptime in seconds
public class UptimeResponse {


	private String utcServerStart; // UTC timestamp recorded when server starts, RFC 3339 date-time string
	private String utcNow; // Current UTC timestamp when response is generated - RFC 3339 date-time string
	private double serverUptimeSeconds; // Number of seconds the server has been running - non-negative floating point number
	
	// Constructor
	// Create an uptime response containing the required server timing information
	public UptimeResponse(String utcServerStart, String utcNow, double serverUptimeSeconds) {
		this.utcServerStart = utcServerStart;
		this.utcNow = utcNow;
		this.serverUptimeSeconds = serverUptimeSeconds;
	}
	
	
	// This is the JSON shape represented as a Java object
	// Get the UTC timestamp at the server start
	public String getUtcServerStart(){
		return utcServerStart;
	}
	
	// Get the current UTC timestamp
	public String getUtcNow() {
		return utcNow;
	}
	
	// Get the total server uptime in seconds
	public double getServerUptimeSeconds() {
		return serverUptimeSeconds;
	}
	
}
