
// Variables via id
const startButton = document.getElementById("start-recording");
const stopButton = document.getElementById("stop-recording");
const recordingStatus = document.getElementById("recording-status");

// Event listeners
startButton.addEventListener("click", function(){
	startButton.disabled = true;
	stopButton.disabled = false;
	recordingStatus.textContent = "Status: Recording...";
});