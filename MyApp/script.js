/*** Global Vars ***/
var idGame;

function createNewBoard() {
	createNewGameReq().then(function(res){
		idGame = res;
		getGameReq(idGame).then(function(res){
			displayBoard(res);
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

function displayBoard(board){
	var pieces = board.split('');
	var position;
	for(var i=0; i<64; i++){
		position = getPosition(i);
		var htmlCode = getHtmlCode(pieces[i]);
		if(htmlCode == "0"){
			continue;
		}else if (htmlCode == "-1"){
			break;
		}else{
			document.getElementById(position).innerHTML = htmlCode ;
		}
	}
}

function getPosition(n){
	var number = 8 - Math.trunc(n/8);
	var rest = n%8;
	var letter;
	switch(rest){
	case 0:
		letter = "a";
		break;
	case 1:
		letter = "b";
		break;
	case 2:
		letter = "c";
		break;
	case 3:
		letter = "d";
		break;
	case 4:
		letter = "e";
		break;
	case 5:
		letter = "f";
		break;
	case 6:
		letter = "g";
		break;
	case 7:
		letter = "h";
		break;
	default:
		letter = "";
	}

	return letter + number.toString();
}

function getHtmlCode(piece){
	switch(piece){
	case 'r':
		return "&#9814;";
	case 'R':
		return "&#9820;";
	case 'n':
		return "&#9816;";
	case 'N':
		return "&#9822;";
	case 'b':
		return "&#9815;";
	case 'B':
		return "&#9821;";
	case 'q':
		return "&#9813;";
	case 'Q':
		return "&#9819;";
	case 'k':
		return "&#9812;";
	case 'K':
		return "&#9818;";
	case 'p':
		return "&#9817;";
	case 'P':
		return "&#9823;";
	case '':
		return "";
	case 'S':
		return "-1";
	default:
		return "0";
	}
}

/********* REQUEST PART ***********/
function createNewGameReq () {
	return fetch("http://localhost:8080/mychessgame/v1/game", {
		method: "GET",
		mode: "no-cors",
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
		mode: "no-cors",
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

