var chess = undefined;

function DemoHelper(game_src, game_dst, idx) {
    this.game = game_dst;
    this.idx = idx;
    game_src._helper = this;
    
    this.queueMove = function (game, move) {
        move.noforward = true;
        this.game.queueMove(move);
    }
}


function changed() {

    var name = $("#gcombo").val();

    if (name == "new") {
        newGame();
    } else if (name == "two") {
        twoBoards();
    } else if (name == "checkers") {
        new DraughtsBoard(reset());
    } else if (name == "checkers-fen") {
        new DraughtsBoard(reset(), {fen:"B:W18,24,27,28,K10,K15:B12,16,20,K22,K25,K29"});
    } else {
        url = 'PGN/'+name+'.pgn';
        $.get(url, pgnLoaded, "text");
    }
    
    return false;
}

function changed2() {

    var partie = document.getElementById('game-id').value

    if (partie == "") {
        alert("FEN cannot be empty");
    } else {
        new DraughtsBoard(reset(), {fen: partie});
    }
}



function newGame() {
    new ChessBoard(reset());
}


function pgnLoaded(data) {
    var g_options = { pgn: data };
    var v_options = { hasControls: false };
    new ChessBoard(reset(), g_options, v_options);
}

function reset() {
    $("#content").empty();
    return $("#content");
}

function twoBoards() {

    var pdiv = reset();
    var div1 = $("<div>");
    pdiv.append(div1);

    var div2 = $("<div>");
    pdiv.append(div2);
    
    var g_options = {};
    var v_options = {play: {w:true} };
    var board1 = new ChessBoard(div1, g_options, v_options);

    v_options = {play: {b:true}, flipped:true };
    var board2 = new ChessBoard(div2, g_options, v_options);

    new DemoHelper(board1.game, board2.game, 1);
    new DemoHelper(board2.game, board1.game, 2);
}

jQuery(function($) {
    changed();
});

