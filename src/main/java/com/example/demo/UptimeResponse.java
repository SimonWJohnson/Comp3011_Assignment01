package com.example.demo;

public class UptimeResponse {


	private String utcServerStart; // RFC 3339 date-time string
	private String utcNow; // RFC 3339 date-time string
	private double serverUptimeSeconds; // non-negative floating point number
	
	// Constructor
	public UptimeResponse(String utcServerStart, String utcNow, double serverUptimeSeconds) {
		this.utcServerStart = utcServerStart;
		this.utcNow = utcNow;
		this.serverUptimeSeconds = serverUptimeSeconds;
	}
	
	
	// This is the JSON shape represented as aJava object
	public String getUtcServerStart(){
		return utcServerStart;
	}
	
	public String getUtcNow() {
		return utcNow;
	}
	
	public double getServerUptimeSeconds() {
		return serverUptimeSeconds;
	}
	
}
