/**
 * The id of the game currently being played in the page.
 */
var currentGameId;

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