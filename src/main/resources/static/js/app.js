
// Variables via id
const startButton = document.getElementById("start-recording");
const stopButton = document.getElementById("stop-recording");
const recordingStatus = document.getElementById("recording-status");
const statusIndicator = document.getElementById("status-indicator");
const statusText = document.getElementById("status-text");
const transcriptionOutput = document.getElementById("transcription-output");

// Microphone access
let mediaStream = null;

// Audio data
let mediaRecorder = null;
let audioChunks = [];

// ## Event listeners, add/remove a state classification from this element ##

// Start recording
startButton.addEventListener("click", async function(){ 
	// Permit application to use live mic audio stream
	// requesting microphone access is asynchronous - the browser may need to permission-prompt the user and wait for a response
	mediaStream = await navigator.mediaDevices.getUserMedia({audio: true});
	// Tie a recorder object to the live mic stream
	mediaRecorder = new MediaRecorder(mediaStream);
	// reset the the audio chunk array for a fresh recording
	// each new recording begins with an empty collection of audio chunks
	audioChunks = [];
	
	// MediaRecorder generates recorded audio data through 'dataavailable" events
	// Store each piece of audio in the array so it can be combined into a single audio Blob when recording stops
	mediaRecorder.addEventListener("dataavailable", function(event){
		audioChunks.push(event.data);
	});
	
	// Start capturing audio from the microphone
	mediaRecorder.start();
	
	startButton.disabled = true;
	stopButton.disabled = false;
	statusText.textContent = "Recording...";
	statusIndicator.classList.add("recording");
});

// Stop recording
stopButton.addEventListener("click", function(){
	
	// finish the current recording, flush remaining audio data
	mediaRecorder.stop();
	
	//  close the MediaStream to deactivate the mic
	mediaStream .getTracks().forEach(function(track){track.stop();}); 
	
	
	// the final dataavailable event can occur as part of stopping -
	// create the Blob after the recorder has fully stopped
	mediaRecorder.addEventListener("stop", function(){ // **** move this into the Start handler at a later stage to avoid accumulating listeners- 
		
		// take all separate binary audio chunks and package them as one binary object
		// this binary object is what gets uploaded to the Spring backend 
		const audioBlob = new Blob(audioChunks, {type: mediaRecorder.mimeType});		
		
		// Create a FormData object to package the audio file for transmission in an HTTP multipart / formdata request
		// Browser-side container for the multipart HTTP request
		const formData = new FormData(); 
		
		// Add the recorded audio Blob to the multipart request
		// 'audio' must match @RequestParam("audio") in the AudioController
		formData.append("audio", audioBlob, "recording.webm");
		// "audio" = multipart field name
		// audioBlob = binary recording
		// "recording.webm" = filename sent to Spring
		// App.js -> HTTP -> AudioController.java
		// @RequestParam("audio") = same field name
		// MultipartFile audioFile = receives recording.webm
		
		// Send the recorded audio to the Spring backend
		// When fetch() receives a FormData body, the browser generates the correct Content-Type and multipart boundary automatically
		fetch("/api/v1/audio/transcribe", {
			method: "POST",
			body: formData
		})
		.then(function(response){
			
			// Convert the HTTP response body into transcription text
			return response.text();
		})
		.then(function(transcription){
			
			// Display the transcription returned by the backend
			transcriptionOutput.textContent = transcription;
		});	
		
	});
	
	startButton.disabled = false;
	stopButton.disabled = true;
	statusText.textContent = "Ready";
	statusIndicator.classList.remove("recording"); 
});