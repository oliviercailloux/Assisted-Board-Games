/**
 * The id of the game currently being played in the page.
 */
var currentGameId;

/**
 * Asks the server to create a new game and loads it to the UI if successful.
 */
var newGame = function() {
	$.ajax({
		url: 'http://localhost:8080/game/new',
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
		url: 'http://localhost:8080/game/get?gid=' + gameId,
		crossDomain: true,
	}).done(loadFen);
	currentGameId = gameId;
};

/**
 * Loads a FEN position to the board.
 */
var loadFen = function(fen) {
	board.position(fen);
};

/**
 * Forward moves to the server and update position afterwards.
 */
var playMove = function(src, dst, promotion) {
	$.ajax({
		url: 'http://localhost:8080/game/${currenGameId}/move',
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