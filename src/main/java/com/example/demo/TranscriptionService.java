package com.example.demo;

import org.springframework.stereotype.Service;


// Handles communication between the Spring backend and the external speech-to-text Cloud service
// AudioController delegates transcription to --> TranscriptionService communicates with --> OpenAI
// @Service makes TranscriptionService a Spring managed Bean
// TranscriptionService will be injected into AudioController through its constructor
@Service
public class TranscriptionService {

}
