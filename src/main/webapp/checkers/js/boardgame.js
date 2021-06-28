/*
 * webBoardGame - Generic support for board games
 *
 * Copyright (c) 2013 Aurelien Naldi
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


/* Define Game: a set of "generic" functions */
Game = {

init : function(defaults, options, gameExtra) {
    this.settings = {}
    for (var k in defaults) {
        this.settings[k] = defaults[k];
    }
    
    if (options) {
        for (var k in options) {
            this.settings[k] = options[k];
        }
    }

    this.game = {
      header        : [],
      moves         : [],
      moveQueue     : [],
      annotations   : [],
      transitions   : [],
      
      curmove       : 0,
      moveclock     : 0,
    };
    if (gameExtra) {
        for (var k in gameExtra) {
            this.game[k] = gameExtra[k];
        }
    }
},



genBoard: function(h,w) {
    this.height = h;
    this.width = w;
    var theboard = new Array(this.height);
    for (var row=0 ; row<this.height ; row++) {
        theboard[row] = new Array(this.width);
        for (var col=0 ; col<this.width ; col++) {
            var color = this.getPlaceType(row, col);
            var rank = String.fromCharCode( col + ('a').charCodeAt(0) );
            var pos = [row,col];
            var name = rank+(this.height-row);
            theboard[row][col] = { content: '-', color:color, pos: pos, name: name };
        }
    }
    
    this._pieces = new Array();
    this._board = theboard;
/*
    this._board.removed = new Array();
    this._board.pieces = this._pieces
*/
},

getVirtualBoard : function() {
    var vboard = [];
    for (var i in this._board) {
        var row = this._board[i];
        var vrow = [];
        for (var j in row) {
            var piece = row[j].content;
            if (piece == "-") {
                vrow.push("-");
            } else {
                vrow.push([piece.piece.toUpperCase(), piece.color]);
            }
        }
        vboard.push(vrow);
    }
    return vboard;
},

// get the type of places for classical board games (chess, checkers)
getPlaceType : function(j,k) {
    if ( ((j+k) % 2) == 0) {
        return "l";
    } else {
        return "d";
    }
},

boardData: function() {
    return this._board;
},

getMoveCount : function() {
    return this.game.transitions.length + this.game.moveQueue.length;
},

getCurMoveNumber : function() {
    return this.game.curmove;
},

runTransitions : function(transitions, respectPause) {
    for (var i in transitions) {
        var transition = transitions[i];
      
        if (transition[0] == "i") {
            this.runInfoTransition(transition);
        } else {
            this.runTransition(transition, respectPause);
        }
    }
},

// Empty implementation for info transitions
runInfoTransition: function(transition) {
},

queueMove: function() {
    if (arguments.length < 1) {
        return;
    } else if (arguments.length == 1) {
        this.game.moveQueue.push(arguments[0]);
    } else {
        this.game.moveQueue.push(arguments);
    }

    if (this.game.curmove == this.game.transitions.length) {
        return this.next();
    }
},


runTransition: function(transition, respectPause) {
    var type  = transition[0];
    var id    = transition[1];
    var piece = this._pieces[id];

    switch(type) {
        case 'r':
            piece.cell.content = "-";
            // FIXME: store removed pieces somehow
            //this.boardData().removed.push(piece);
            piece.cell = type;
            
          break;
        case 'm':
            piece.cell.content = "-";
            var to = transition[2];
            var cell = this.boardData()[to[0]][to[1]];
            cell.content = piece;
            piece.cell = cell;
            break;
        case 'a':
            var to = transition[2];
            var cell = this.boardData()[to[0]][to[1]];
            cell.content = piece;
            piece.cell = cell;
          break;
        case 't':
            if (piece.color=="w") {
                piece.piece = transition[2].toUpperCase()
            } else {
                piece.piece = transition[2].toLowerCase();
            }
            piece.transformed = true;
            break;
        case 'p':
            // pause request: honor it only if asked to
            if (respectPause) {
                this.updateView();
                console.log("FIXME: pause between steps");
            }
            break;
        default:
            // should not happen!
            console.log("Unknown type of transition: "+type);
    }
},

updateView : function() {
    if (this._view) {
      this._view.fillBoard();
    }
},

/* Browse history */

goStart : function() {
    while ( this._backward() ) {
    }
    this.updateView();
},

goEnd : function() {
    while ( this._forward() ) {
    }
    this.updateView();
},

goCloseEnd : function() {
    while ( this._forward() ) {
    }
    if ( this._backward() ) {
        this.updateView();
    }
},

_forward : function(respectPause) {
    if (this.game.curmove == this.game.transitions.length && this.game.moveQueue.length > 0) {
        var move = this.game.moveQueue.shift();
        this.move2transition(move);
    }
    
    if (this.game.curmove >= this.game.transitions.length) {
        return false;
    }

    this.runTransitions(this.game.transitions[this.game.curmove].forward, respectPause);
    this.game.curmove++;
    this.game.active = (this.game.active == "w") ? "b" : "w";
    return true;
},

_backward : function(respectPause) {
    if (this.game.curmove > 0) {
      this.game.curmove--;
      this.runTransitions(this.game.transitions[this.game.curmove].backward, respectPause);
      this.game.active = (this.game.active == "w") ? "b" : "w";
      return true;
    }
    return false;
},

next : function() {
    if (this.isFast()) {
        var c = 0;
        while (c<5 && this._forward()) {
            c++;
        }
    } else {
        this._forward(true);
    }
    
    this.updateView();
},
prev : function() {
    if (this.isFast()) {
        var c = 0;
        while (c<5 && this._backward()) {
            c++;
        }
    } else {
        this._backward(true);
    }
    
    this.updateView();
},

isFast : function() {
    var stamp = new Date().getTime();
    var fast = false;
    if (this.laststamp && (stamp-this.laststamp) < 200) {
        fast = true;
    }
    this.laststamp = stamp;
    return fast;
},

/* Utility Functions */

isVirtualFree : function(vboard, j,k) {
    if (this.isOnBoard(j,k)) {
        return (vboard[j][k] == "-");
    }
    return false;
},

isVirtualPiece : function(vboard, j,k, type, color) {
    type = type.toUpperCase();
    if (this.isOnBoard(j,k)) {
        content = vboard[j][k];
        if (content && content != "-" && content[0].toUpperCase() == type && content[1] == color) {
            return true;
        }
    }
    return false;
},

isVirtualEnnemy : function(vboard, j,k, color) {
    if (this.isOnBoard(j,k)) {
        content = vboard[j][k];
        if (!content) {
            console.log("nada?");
        }
        if (content != "-" && content[1] != color) {
            return true;
        }
    }
    return false;
},

isFree : function(j,k) {
    if (this.isOnBoard(j,k)) {
        return (this.boardData()[j][k].content == "-");
    }
    return false;
},

isPiece : function(j,k, type, color) {
    type = type.toUpperCase();
    if (this.isOnBoard(j,k)) {
        content = this.boardData()[j][k].content;
        if (content != "-" && content.piece.toUpperCase() == type && content.color == color) {
            return true;
        }
    }
    return false;
},

isEnnemy : function(j,k, color) {
    if (this.isOnBoard(j,k)) {
        content = this.boardData()[j][k].content;
        if (content == "-") {
        } else if (content.color != color) {
            return true;
        }
    }
    return false;
},

isOnBoard : function(j,k) {
    return (j>=0 && j<this.height && k>=0 && k<this.width);
},

guessColor : function(tpl) {
    if (tpl == tpl.toUpperCase()) { 
        return "w";
    }
    return "b";
},
  
algebraic2Coord : function(algebraic) {
        row = 8 - parseInt(algebraic.substr(1, 1));
        col = algebraic.substr(0, 1).charCodeAt(0) - ('a').charCodeAt(0);
        return [row, col];
},

coord2Algebraic : function(row, col) {
    file = String.fromCharCode( col + ('a').charCodeAt(0) );
    rank = (8 - row) + ''; 
    return file + rank;
},

pieceAt : function(algebraic) {
    console.log("pieceat "+algebraic);
    var square = this.algebraic2Coord(algebraic);
    return this._board[square[0]][square[1]].content;
},

pieceOn : function(j,k) {
    if ( !this.isOnBoard(j,k)) {
        return undefined;
    }
    return this._board[j][k].content;
},

cellAt : function(j,k) {
    if ( !this.isOnBoard(j,k)) {
        return undefined;
    }
    return this._board[j][k];
},




};  // END of Game methods

