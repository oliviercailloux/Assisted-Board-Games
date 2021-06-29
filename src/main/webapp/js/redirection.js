// Triggers a function after a button click, here we are going to redirect the user to a selected game page
document.getElementById("Chess").onclick = () => {
	console.log('Selected chess');

	window.location.assign("chess.html");
};

document.getElementById("Checkers").onclick = () => {
	console.log('Selected checkers');

	window.location.assign("checkers/checkers.html");
}