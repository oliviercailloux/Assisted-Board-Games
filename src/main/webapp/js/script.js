// The id of the game currently being played in the page.
let currentGameId;
// id of setInterval function
let intervalID;
// Duration time entered by player
let inputDuration = 0;
// Increment time entered by player
let inputIncrement = 0;

// Remaining time of white player
const whitePlayerTime = {
    hours: 0,
    minutes: 0,
    seconds: 0
};

// Remaining time of black player
const blackPlayerTime = {
    hours: 0,
    minutes: 0,
    seconds: 0
};

/**
 * Displays a real-time clock
 */
const displayTime = () => {
	const player = document.getElementById("status").textContent.split(" ")[0];
	const time = player === "White" ? whitePlayerTime : blackPlayerTime;
	const id = player === "White" ? "White-player-clock" : "Black-player-clock";
	const clock = document.getElementById(id);

	if (time.hours === 0 && time.minutes === 0 && time.seconds === 0) {
		if (whitePlayerTime.seconds === 0 && blackPlayerTime.seconds === 0)
    		clearInterval(intervalID);
    	clock.innerText = "00:00:00";
    	return;
    }

    let minutes = time.minutes;
    
    if (time.seconds === 59) {
        minutes = minutes - 1;

        if (time.minutes > 0) {
            time.minutes = minutes;

        } else if (time.hours > 0) {
        	time.hours = time.hours - 1;
            time.minutes = 59;
            minutes = time.minutes;
        }
    }
    
    if (minutes.toString().length === 1) {
        minutes = "0".concat(minutes);
    }
    
    let hours = time.hours;
    
    if (hours.toString().length === 1) {
        hours = "0".concat(hours);
    }
    
    let seconds = time.seconds;
        
    if (time.seconds > 0) {
        time.seconds = seconds - 1;
    } else if (time.seconds === 0 && time.minutes > 0) {
    	time.seconds = 59;
    } else if (time.seconds === 0 && time.minutes === 0 && time.hours > 0) {
    	time.seconds = 59;
    }
    
    if (seconds.toString().length === 1) {
    	seconds = "0".concat(seconds);
    }
    
	clock.innerText = `${hours}:${minutes}:${seconds}`;
};

/**
 * Sends a GET request to server to get the remaining time, parses it's response if successful
 */
const getRemainingTime = (player, id) => {
	$.ajax({
		url: `api/v1/game/${currentGameId}/clock/${player}`,
		type: 'GET',
		crossDomain: true
	}).done(data => parseRemainingTime(player, id, data));
};

/**
 * Parses reaming time, obtained from the server
 */
const parseRemainingTime = (player, id, gameTime) => {
	document.getElementById(id).innerText = "00:00:00";

	let hours;
	const hoursIndex = gameTime.indexOf("H");
	let minutes;
	const minutesIndex = gameTime.indexOf("M");
	let seconds;
	const secondsIndex = gameTime.indexOf("S");

	if (hoursIndex !== -1)
		hours = gameTime.substring(gameTime.indexOf("T") + 1, hoursIndex);
	else
		hours = "00";

	if (hoursIndex === -1 && minutesIndex !== -1)
		minutes = gameTime.substring(gameTime.indexOf("T") + 1, minutesIndex);
	else if (minutesIndex !== -1)
		minutes = gameTime.substring(hoursIndex + 1, minutesIndex);
	else
		minutes = "00";

	if (secondsIndex !== -1)
		seconds = Math.round(gameTime.substring(minutesIndex + 1, secondsIndex));
	else
		seconds = "00";

	const time = player === "white" ? whitePlayerTime : blackPlayerTime;

	hours = Number(hours);
	minutes = Number(minutes);
	seconds = Number(seconds);

	if (seconds === 60) {
		minutes += 1;
		seconds = 0;
	}
	
	if (minutes === 60) {
		hours += 1;
		minutes = 0;
	}

	time.hours = hours;
	time.minutes = minutes;
	time.seconds = seconds;
};

/**
 * Stores provided by the player time to a global variable, in order to use it later to change the remaining time
 */
const changeRemainingTime = () => {
	const duration = document.getElementById("duration").value;
	const increment = document.getElementById("increment").value;

	if (duration.trim() === '') {
		alert("Please enter correct duration time!");
		return;
	} else if (duration < 60) {
		alert("Minimum game duration time is 600 seconds (10 minutes)!");
		return;
	} else if (duration > 36000) {
		alert("Maximum game duration time is 36000 seconds (10 hours)!");
		return;
	} else if (increment.trim() !== '' && increment < 0) {
		alert("Minimum increment value is 0 seconds (no increment)!");
		return;
	} else if (increment.trim() !== '' && increment > 60) {
		alert("Maximum increment value is 60 seconds!");
		return;
	}

	inputDuration = duration;
	
	// If the increment wasn't provided by the player use the default value
	if (increment.trim() !== '')
		inputIncrement = increment;

	document.getElementById("duration").disabled = true;
	document.getElementById("increment").disabled = true;
	document.getElementById("time-btn").disabled = true;
}

/**
 * Asks the server to create a new game and loads it to the UI if successful.
 */
var newGame = function() {
	document.getElementById("duration").disabled = true;
	document.getElementById("increment").disabled = true;
	document.getElementById("time-btn").disabled = true;

	$.ajax({
		url: 'http://localhost:8080/api/v1/game/new',
		type: 'POST',
		// data: `duration=${inputDuration}&increment=${inputIncrement}`,
		data: {duration: inputDuration, increment: inputIncrement},
		crossDomain: true
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

	if (inputDuration === 0) {
		document.getElementById("duration").value = "";
		document.getElementById("increment").value = "";
	}

	inputDuration = 0;
	inputIncrement = 0;
	clearInterval(intervalID);

	window.onload = getRemainingTime("white", "White-player-clock");
	window.onload = getRemainingTime("black", "Black-player-clock");

	intervalID = window.onload = window.setInterval(() => {
		displayTime();
	}, 1000);
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