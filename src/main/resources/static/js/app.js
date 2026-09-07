
// Variables via id
const startButton = document.getElementById("start-recording");
const stopButton = document.getElementById("stop-recording");
const recordingStatus = document.getElementById("recording-status");
const statusIndicator = document.getElementById("status-indicator");
const statusText = document.getElementById("status-text");

// Microphone access
let mediaStream = null;

// Audio data
let mediaRecorder = nul;
let audioChunks = [];

// ## Event listeners, add/remove a state classification from this element ##

// Start recording
startButton.addEventListener("click", async function(){ 
	// requesting microphone access is asynchronous - the browser may need to permission-prompt the user and wait for a response
	mediaStream = await navigator.mediaDevices.getUserMedia({audio: true});
	
	startButton.disabled = true;
	stopButton.disabled = false;
	//recordingStatus.textContent = "Status: Recording...";
	statusText.textContent = "Recording...";
	statusIndicator.classList.add("recording");
});

// Stop recording
stopButton.addEventListener("click", function(){
	startButton.disabled = false;
	stopButton.disabled = true;
	//recordingStatus.textContent = "Status: Ready";
	statusText.textContent = "Ready";
	statusIndicator.classList.remove("recording"); 
});