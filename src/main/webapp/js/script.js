var currentGameId;
var newGame=function(){
	$.ajax({
		url: 'http://localhost:8080/game/new',
		type: 'GET',
		crossDomain: true,
	}).done(loadGame);
};
var loadGame=function(gameId){
	if(gameId===undefined){
		gameId=$('#game-id').text();
	}
	$.ajax({
		url: 'http://localhost:8080/game/get?gid=' + gameId,
		type: 'GET',
		crossDomain: true,
	}).done(loadFen);
	currentGameId=gameId;
};
var loadFen=function(fen){
	game.load(fen);
};
var playMove=function(src,dst){
	$.ajax({
		url: 'http://localhost:8080/game/move?gid=' + currentGameId + '&from=' + src.toUpperCase() + '&to=' + dst.toUpperCase(),
		type: 'GET',
		crossDomain: true,
	}).done(loadFen);
};