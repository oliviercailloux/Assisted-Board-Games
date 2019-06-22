var puzzle = {};

/**
 * Loads a game given its gameId.
 */
var loadPuzzle = function(gameId) {
	// if no gameId is given, get it from user's input
	if (gameId === undefined) {
		gameId = $('#game-id').val();
		// if user didn't provide a game id, alter him
		if (!gameId) {
			alert('Please provide a game Id');
			return;
		}
	}
	puzzle.id = gameId;
	$.ajax({
		url: 'http://localhost:8080/api/v1/game/' + gameId + '/moves',
		crossDomain: true,
	}).done(function(moves) {
		puzzle.moves = moves;
		puzzle.startMove = 1;
		puzzle.move = 1;
		puzzle.length = 3;
		loadFen('rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1');
		for (var i = 0; i < puzzle.startMove; i++) {
			game.move({
				from: puzzle.moves[i].from.toLowerCase(),
				to: puzzle.moves[i].to.toLowerCase(),
			});
		}
		board.position(game.fen());
		if (game.turn() === 'b') {
			board.flip();
		}
	});
};

/**
 * Loads a FEN position to the board.
 */
var loadFen = function(fen) {
	board.position(fen);
};

var isExpectedMove = function(src, dst) {
	var expectedMove = puzzle.moves[puzzle.move];
	console.log(expectedMove.from + ',' + expectedMove.to);
	console.log(src + ',' + dst);
	if (src.toLowerCase() != expectedMove.from.toLowerCase() || dst.toLowerCase() != expectedMove.to.toLowerCase()) {
		return false;
	}
	return true;
};