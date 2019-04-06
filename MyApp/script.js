/*** Global Vars ***/
var idGame;

function createNewBoard() {
    idGame = createNewGameReq().then(function(res){
        idGame = res;
        getGameReq(idGame).then(function(res){
            var board = res.split('');
        });
    });
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

/********* REQUEST PART ***********/
function createNewGameReq () {
      return fetch("http://localhost:8080/mychessgame/v1/game", {
        method: "GET",
        mode: "cors",
        cache: "no-cache",
        headers: {
            "Content-Type": "text/plain",
        },
        redirect: "follow",
        referrer: "no-referrer",
        //body: JSON.stringify(data), // body data type must match "Content-Type" header
    })
        .then(response => response.text())
        .then(function(response){
            return response.toString();
        }).catch(function (error){
            console.log("An error occurend : ", error);
        });
}

function getGameReq(idGame){
    return fetch("http://localhost:8080/mychessgame/v1/game/getGame?game="+idGame, {
        method: "GET",
        mode: "cors",
        cache: "no-cache",
        headers: {
            "Content-Type": "text/plain",
        },
        redirect: "follow",
        referrer: "no-referrer",
    })
        .then(response => response.text())
        .then(function(response){
            return response.toString();
        }).catch(function (error){
            console.log("An error occurend : ", error);
        });
}

