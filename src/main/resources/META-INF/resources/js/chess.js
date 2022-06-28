document.getElementById('btn-dark-mode').onclick = function () {
    document.body.classList.toggle('dark-mode');
}

let GM = new GameManager();
GM.initialiseChessBoard();

document.getElementById('btn-new-game').onclick = function() {
    GM.newGame();
}
document.getElementById('btn-load-game').onclick = function () {
    GM.loadGame();
}
