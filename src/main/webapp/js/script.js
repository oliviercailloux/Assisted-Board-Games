/**
 * The id of the game currently being played in the page.
 */
let currentGameId;

/**
 * Sends a GET request to server to get the remaining time
 */
const getRemainingTime = (player, id) => {
	let xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", `api/v1/game/${currentGameId}/clock/${player}`, false);
    xmlHttp.send(null);

	const time = xmlHttp.responseText;
	let clock = document.getElementById(id);

	let hours;
	const hoursIndex = time.indexOf("H");
	let minutes;
	const minutesIndex = time.indexOf("M");
	let seconds;
	const secondsIndex = time.indexOf("S");

	if (hoursIndex !== -1)
		hours = time.substring(time.indexOf("T") + 1, hoursIndex);
	else
		hours = "00";

	if (hoursIndex === -1 && minutesIndex !== -1)
		minutes = time.substring(time.indexOf("T") + 1, minutesIndex);
	else if (minutesIndex !== -1)
		minutes = time.substring(hoursIndex + 1, minutesIndex);
	else
		minutes = "00";

	if (secondsIndex !== -1)
		seconds = Math.round(time.substring(minutesIndex + 1, secondsIndex));
	else
		seconds = "00";


	if (hours.length === 1)
		hours = "0".concat(hours);

	if (minutes.length === 1)
		minutes = "0".concat(minutes);

	if (seconds.length === 1)
		seconds = "0".concat(seconds);

	clock.innerText = `${hours}:${minutes}:${seconds}`;
};

/**
 * Sends a POST request to server to change the remaining time
 */
const changeRemainingTime = () => {
	const time = document.getElementById("time").value;

	if (time.trim() === '') {
		alert("Please enter correct value!");
		return;
	} else if (time < 600) {
		alert("Minimum time is 600 seconds (10 minutes)!");
		return;
	} else if (time > 36000) {
		alert("Maximum time is 36000 seconds (10 hours)!");
		return;
	}

	const http = new XMLHttpRequest();
	const parameters = `time=${time}`;
	http.open("POST", `api/v1/game/${currentGameId}/clock/change`, false);
	http.send(parameters);

	document.getElementById("time").disabled = true;
	document.getElementById("time-btn").disabled = true;

	getRemainingTime("black", "Black-player-clock");
	getRemainingTime("white", "White-player-clock");
}

/**
 * Asks the server to create a new game and loads it to the UI if successful.
 */
var newGame = function() {
	$.ajax({
		url: 'http://localhost:8080/api/v1/game/new',
		type: 'POST',
		crossDomain: true,
	}).done(loadGame);
};

/**
 * Loads a game given its gameId.
 */
var loadGame = function(gameId) {
	// if no gameId is given, get it from user's input
	if (gameId === undefined) {
		gameId = $('#game-id').val();
		// if user didn't provid a game id, alter him
		if (!gameId) {
			alert('Please provide a game Id');
			return;
		}
	}
	$.ajax({
		url: `http://localhost:8080/api/v1/game/${gameId}/moves`,
		crossDomain: true,
	}).done(loadMoves);
	currentGameId = gameId;

	document.getElementById("time").disabled = false;
	document.getElementById("time-btn").disabled = false;

	window.onload = getRemainingTime("black", "Black-player-clock");
	window.onload = getRemainingTime("white", "White-player-clock");

	// If the player doesn't change the default clock duration in 30 secs, then this possibility gets disabled to prevent cheating
	setTimeout(() => {
		document.getElementById("time").disabled = true;
		document.getElementById("time-btn").disabled = true;
	}, 30000);

	window.onload = window.setInterval(() => {
		 getRemainingTime("black", "Black-player-clock");
		 getRemainingTime("white", "White-player-clock");
	}, 10000);
};

/**
 * Loads a FEN position to the board.
 */
var loadFen = function(fen) {
	board.position(fen);
	update();
};

var loadMoves = function(moves) {
	game = Chess();
	for(var i in moves) {
		let move = moves[i];
		game.move({
			from: move.from.toLowerCase(),
			to: move.to.toLowerCase(),
		});
	}
	board.position(game.fen());
	update();
};

/**
 * Forward moves to the server and update position afterwards.
 */
var playMove = function(src, dst, promotion) {
	$.ajax({
		url: `http://localhost:8080/api/v1/game/${currentGameId}/move`,
		type: 'POST',
		contentType: 'application/json',
		dataType: 'json',
		data: JSON.stringify({
			from: src.toUpperCase(),
			to: dst.toUpperCase(),
			promotion: promotion || "NONE", // just to avoid passing null
		}),
		crossDomain: true,
	}).done(loadFen);
};