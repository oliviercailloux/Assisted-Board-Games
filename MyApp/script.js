var idGame;

function createNewBoard() {
    idGame = servletCreateGame();
    document.getElementById("board").innerHTML = servletGetGame(idGame);
}

function getSuggestedMoves() {
    document.getElementById("suggestions").innerHTML = servletGetHelp();
}

function newMove() {
    var from = document.getElementById("from");
    var to = document.getElementById("to");
    servletAddMove(idGame, from, to);
    document.getElementById("board").innerHTML = servletGetGame(idGame);
}

function loadGame() {
	idGame = document.getElementById("idGame");
    document.getElementById("board").innerHTML = servletGetGame(idGame);
}