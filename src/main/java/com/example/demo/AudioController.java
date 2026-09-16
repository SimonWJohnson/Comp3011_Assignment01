package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

// Handle HTTP requests relating to recorded audio and speech transcription
// Acts as a server when it receives the browser's request
@RestController // discover the class as a REST controller
@RequestMapping("/api/v1/audio") // establish the common base path
public class AudioController {
	
	// Service responsible for communication with the external speech-to-text CLoud service
	private final TranscriptionService transcriptionService;
	
	// Constructor dependency injection
	// Spring supplies the TranscriptionService Bean when creating this controller
	public AudioController(TranscriptionService transcriptionService) {
		this.transcriptionService = transcriptionService;
	}
	
	// Receives an audio recording uploaded from the browser
	// The browser cannot send Spring the JS Blob object directly - these bytes are sent as an HTTP multipart request
	// Spring represents the uploaded file on the java side as a MultipartFile
	@PostMapping("/transcribe")
	public void receiveAudio(@RequestParam("audio") MultipartFile audioFile) {
		
		/* Temp diagnostic tool to confirm that the uploaded audio file has reached the Spring backend successfully */
		System.out.println("Audio file received");
		System.out.println("Filename: " + audioFile.getOriginalFilename());
		System.out.println("Content type: " + audioFile.getContentType());
		System.out.println("Size: " + audioFile.getSize() + " bytes");
		
		// Delegate transcription responsibility to a transcription service
		transcriptionService.transcribe(audioFile);
		
	}
	
}
