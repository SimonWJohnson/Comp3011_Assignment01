package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

// Regression tests for the audio transcription controller
// The external STT service will be stubbed so these tests verify 
// the application's controller behaviour without calling OpenAI

@SpringBootTest
@AutoConfigureMockMvc
public class AudioControllerTests {

	// MockMvc sends simulated HTTP requests through Spring MVC
	@Autowired
	private MockMvc mockMvc;
	
	// Replace the real TranscriptionService Bean with a Mockito mock for the duration of the test
	@MockitoBean
	private TranscriptionService transcriptionService;
	
	@Test
	void transcribeReturnsTextFromTranscriptionService() throws Exception{
		
		// Arrange - create a simulated audio file uploaded by the browser
		MockMultipartFile audioFile = new MockMultipartFile(
				"audio", "recording.webm", "audio/webm", "fake audio data".getBytes()
				);
		
		// Configure the mocked service to return a known transcription whenever it recieves a MultipartFile
		when(transcriptionService.transcribe(any(MultipartFile.class))).thenReturn("Test transcription");
		
		// Act and Assert - send the multipart request through Spring MVC
		// and verify the controller returns the service result successfully
		mockMvc.perform(multipart("/api/v1/audio/transcribe")
				.file(audioFile))
				.andExpect(status().isOk())
				.andExpect(content().string("Test transcription"));
	}		
}
