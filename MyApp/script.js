/*** Global Vars ***/
var id_game;

function createNewBoard() {
    createNewGameReq().then(function(res){
        id_game = res;
        getGameReq(res).then(function(resp){
            displayBoard(resp);
        });
    });

}

function getSuggestedMoves() {
    getHelpReq(id_game).then(function(res){
        document.getElementById("suggestions").innerHTML = res;
    });
}

function newMove() {
    var from = document.getElementById("from").value;
    var to = document.getElementById("to").value;

    addMoveReqBIS(id_game, from, to).then(function(res){ 
        displayBoard(res);
    });
}

function loadGame() {
	idGameToLoad = document.getElementById("idGame").value;
    id_game = idGameToLoad;
    getGameReq(idGameToLoad).then(function(res){
        displayBoard(res);
    });
}

function displayBoard(board){
	board = board.replace(/\n/g, "");
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
	case 'R':
		return "&#9814;";
	case 'r':
		return "&#9820;";
	case 'N':
		return "&#9816;";
	case 'n':
		return "&#9822;";
	case 'B':
		return "&#9815;";
	case 'b':
		return "&#9821;";
	case 'Q':
		return "&#9813;";
	case 'q':
		return "&#9819;";
	case 'K':
		return "&#9812;";
	case 'k':
		return "&#9818;";
	case 'P':
		return "&#9817;";
	case 'p':
		return "&#9823;";
	case ' ':
		return "&nbsp;";
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
            console.log("getGame Req : ", response);
            return response.toString();
        })
        .catch(function (error){
            console.log("An error occurend : ", error);
        });
}

function getHelpReq(idGame){
    return fetch("http://localhost:8080/mychessgame/v1/help?game="+idGame, {
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

function addMoveReq(idGame, move){
    return fetch("http://localhost:8080/mychessgame/v1/game/move?game="+idGame, {
        method: "POST",
        mode: "cors",
        cache: "no-cache",
        headers: {
            "Content-Type": "text/plain",
        },
        redirect: "follow",
        referrer: "no-referrer",
        body:move,
    })
        .then(response => response.text())
        .then(function(response){
            return response.toString();
        }).catch(function (error){
            console.log("An error occurend : ", error);
        });
}

function addMoveReqBIS(idGame,from,to){
    return fetch("http://localhost:8080/mychessgame/v1/game/addMove?game="+idGame+"&from="+from+"&to="+to, {
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

