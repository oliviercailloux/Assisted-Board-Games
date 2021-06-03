/*
 * webChess 0.1.0 - Chess Library (using the PGN parser from Ben Marini's jchess)
 *
 * Copyright (c) 2008 Ben Marini
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

/* Constructor */
Draughts = function(options) {
    var gameExtra = {
      body              : '',
      annotations       : [],
      raw_annotations   : [],
    };
    
    this.init(this.defaults, options, gameExtra);
    
    if (options) {
        for (k in options) {
            this.settings[k] = options[k];
        }
    }

    this.w = 10;

    this.genBoard(this.w, this.w);

    // Load a fresh board position
    this.setupBoard( );
};

// Import Game's functions into Draughts's prototype
for (k in Game) {
    Draughts.prototype[k] = Game[k];
}

Draughts.prototype.defaults = {
    fen: "W:W31-50:B1-20",
}

Draughts.prototype.setupBoard = function() {
    

    // Draught's FEN examples:
    // "B:W18,24,27,28,K10,K15:B12,16,20,K22,K25,K29"
    // "B:W18,19,21,23,24,26,29,30,31,32:B1,2,3,4,6,7,9,10,11,12"
    // standard start: "W:W31-50:B1-20"
    var position = this.settings.fen;
    
    var positions = position.split(":");
    for (var idx in positions) {
        if (idx == 0) {
            continue;
        }
        var block = positions[idx];
        var color = block.charAt(0).toLowerCase();
        blocks = block.substring(1).split(",");
        for (var bidx in blocks) {
            var range = blocks[bidx].split("-");
            var piece = "p";
            var c = range[0].charAt(0).toLowerCase();
            if (c == "k") {
                range[0] = range[0].substring(1);
                piece = "k";
            }
            
            if (color == "w") {
                piece = piece.toUpperCase();
            }
            
            var start = parseInt(range[0]);
            var end = start;
            if (range.length == 2) {
                end = parseInt(range[1]);
            }
            
            // place the corresponding pieces!
            for (var i=start ; i<=end ; i++) {
                var n = (i-1)*2;
                var col = n % this.w;
                var row = (n-col) / this.w;
                col = col + 1 - (row%2);
                var pos = [row,col];
                this.createPiece( piece, pos, undefined );
            }
        }
    }
    this.game.active = "w";
};

Draughts.prototype.createPiece = function(tpl, pos, init) {
        var id = this._pieces.length;
        var color = this.guessColor(tpl);
        var code  = tpl.toUpperCase()+color;
        var cell = this._board[pos[0]][pos[1]];
        if (init == undefined) {
            init = cell;
        }
        var piece = { id: id , color: color, piece: tpl, cell:cell, init:init };
        cell.content = piece;
        this._pieces.push(piece);
        if (this._view) {
            this._view.createSprite(piece);
        }
        return piece;
};

Draughts.prototype.runInfoTransition = function(transition) {
    // TODO: info transition for checkers??
};

let count = 0;
var playerToPlay = " White to Move";
Draughts.prototype.move2transition = function(move) {
    

    if (this.game.curmove < this.game.transitions.length) {
        return false;
    }

    var src = move[0];
    var piece = this.pieceOn(src[0], src[1]);

    // check that the move is valid!
    var allowed = this.getAllowedMoves(piece);
    if (!allowed) {
        console.log("["+this.game.curmove+"] No allowed move! "+move[0]+"#"+move[1]);
        return false;
    }

    
    var source = piece.cell.pos;
    var FW = []
    var BW = [];
    var tmove = {forward:FW, backward:BW};
    
    var moveidx = 1;
    while (moveidx < move.length) {
        var dest = move[moveidx];
        var selected = undefined;
        for (var i in allowed) {
            pos = allowed[i];
            if (pos[0] == dest[0] && pos[1] == dest[1]) {
                selected = pos;
                break;
            }
        }
        
        if (!selected) {
            break;
        }

        FW.push(['m', piece.id, dest]);
        BW.unshift(['m', piece.id, source]);

        var extra = selected.extra;
        var next = selected.next;

        if (extra) {
            switch (extra[0]) {
                case 'k':
                    canPromote = true;
                    break;
                case 'r':
                    var pdest = this.cellAt(extra[1][0], extra[1][1]);
                    FW.push( ["r", pdest.content.id] );
                    BW.unshift( ["a", pdest.content.id, pdest.pos] );
                    var rcode = pdest.content.piece.toUpperCase()+pdest.content.color;
                    FW.push( ["i", "r", rcode, 1] );
                    BW.unshift( ["i", "r", rcode, -1] );
                    break;
            }
        }
        
        moveidx++;
        allowed = selected.next;
        source = dest;
        if (allowed) {
            FW.push( ["p"] );
            BW.unshift( ["p"] );
        }
    }
    
    // create kings when reaching the last row
    if (piece.piece.toLowerCase() == "p") {
        var lastrow = 0;
        if (piece.color == "b") {
            lastrow = this.w-1;
        }
        
        if (dest[0] == lastrow) {
            FW.push( ["t", piece.id, "k"] );
            BW.unshift( ["t", piece.id, piece.piece] );
        }
    }
     
    
    // actually perform the move
    this.game.transitions.push(tmove);
    this.next();
    

    
    
    if (this._helper && !move.noforward) {
        this._helper.queueMove(this, move);
    }
    count ++;
    if (count%2==1){
        playerToPlay = "Black To Move";
        
    } else {
        playerToPlay = "White To Move";
    }


    document.getElementById("whoPlays").innerHTML = playerToPlay;
    
};


Draughts.prototype.getAllAllowedMoves = function() {
    if (this.game.halfmove_number < this.game.transitions.length) {
        return {};
    }
    // compute all allowed moves for the current player
    var allAllowed = {}
    var mintake = 0;
    var color = this.game.active;
    for (idx in this._pieces) {
        var piece = this._pieces[idx]
        var allowed = this.findAllowedMoves(piece, mintake);
        if (!allowed || allowed.length < 1) {
            continue;
        }
        var curtake = allowed.takes;
        if (curtake > mintake) {
            allAllowed = {};
            mintake = curtake;
        }
        if (curtake == mintake) {
            allAllowed[piece.id] = allowed;
        }
    }
//console.log("Take at least "+mintake+" pieces");
    return allAllowed;
};

Draughts.prototype.findAllowedMoves = function(piece, mintake) {

    var type = piece.piece.toUpperCase();
    var color = piece.color;
    if (color != this.game.active || !piece.cell.pos) {
        return null;
    }

    var allowed = []
    if (!piece.cell.pos) {
        console.log("WTF? "+piece.cell);
    }
    var curr = piece.cell.pos[0];
    var curc = piece.cell.pos[1];
    var vboard = this.getVirtualBoard();
    
    // pawn moves
    if (type == 'P') {
        var allowed = this.getPawnTake(curr, curc, vboard, color);
        
        // add regular moves if no take is possible
        if (allowed.takes == 0) {
            var dr = (color=="w") ? -1 : 1;
            var tr = curr + dr;
            var tc = curc + 1;

            if (this.isFree(tr, tc)) {
                allowed.push([tr,tc]);
            }
            tc = curc-1
            if (this.isFree(tr, tc)) {
                allowed.push([tr,tc]);
            }
        }
//console.log("DMs: "+allowed);
    } else if (type == 'K') {

        var allowed = this.getKingTake(curr, curc, vboard, color);
        
        // add regular moves if no take is possible
        if (allowed.takes == 0) {
            for (var didx in this.directions) {
                var dr = this.directions[didx][0];
                var dc = this.directions[didx][1];
                var tr = curr+dr;
                var tc = curc+dc;

                while (this.isFree(tr, tc)) {
                    allowed.push([tr,tc]);
                    tr += dr;
                    tc += dc;
                }
            }
        }
    } else {
        console.log("No such type of piece");
        return null;
    }
    
    return allowed;
};

Draughts.prototype.getPawnTake = function(cr, cc, vboard, color) {
    var allowed = [];
    var mintake = 0;
    for (var didx in this.directions) {
        var dr = this.directions[didx][0];
        var dc = this.directions[didx][1];
        
        var rr = cr+dr;
        var tr = rr+dr;
        var rc = cc+dc;
        var tc = rc+dc;

        var result = undefined;
        if ( this.isVirtualFree(vboard, tr, tc) && this.isVirtualEnnemy(vboard, rr, rc, color) ) {
            // update vboard
            var piece  = vboard[cr][cc];
            var backup = vboard[rr][rc];
            vboard[cr][cc] = "-";
            vboard[rr][rc] = "-";
            vboard[tr][tc] = piece;
            
            // keep moving
            var allowed_next = this.getPawnTake(tr, tc, vboard, color);
            
            // restore vboard
            vboard[cr][cc] = piece;
            vboard[rr][rc] = backup;
            vboard[tr][tc] = "-";

            // add the move
            result = [tr,tc];
            result.extra = ["r", [rr,rc]];
            result.takes = allowed_next.takes+1;
            if (allowed_next.length > 0) {
                result.next = allowed_next;
            }
        }
        
        if (result) {
            var curtake = result.takes;
            if (curtake > mintake) {
                mintake = curtake;
                allowed = [];
            }
            if (curtake == mintake) {
                allowed.push(result);
            }
        }
    }
    
    allowed.takes = mintake;
    return allowed;
};




Draughts.prototype.getKingTake = function(cr, cc, vboard, color) {
    var allowed = [];
    var mintake = 0;
    for (var didx in this.directions) {
        var dir = this.directions[didx];
        var dr = dir[0];
        var dc = dir[1];

        // avance until the first obstacle
        var rr = cr+dr;
        var rc = cc+dc;
        while (this.isFree(rr, rc)) {
            rr += dr;
            rc += dc;
        }

        // stop if it is not an ennemy
        var result = undefined;
        if ( !this.isVirtualEnnemy(vboard, rr, rc, color) ) {
            continue;
        }
        
        // all free squares right after the ennemy are candidates for landing
        var tr = rr+dr;
        var tc = rc+dc;
        var piece  = vboard[cr][cc];
        var backup = vboard[rr][rc];
        while ( this.isVirtualFree(vboard, tr, tc) ) {
            // update vboard
            vboard[cr][cc] = "-";
            vboard[rr][rc] = "-";
            vboard[tr][tc] = piece;
            
            // keep moving
            var allowed_next = this.getKingTake(tr, tc, vboard, color);
            
            // restore vboard
            vboard[cr][cc] = piece;
            vboard[rr][rc] = backup;
            vboard[tr][tc] = "-";

            // add the move
            result = [tr,tc];
            result.extra = ["r", [rr,rc]];
            result.takes = allowed_next.takes+1;
            if (allowed_next.length > 0) {
                result.next = allowed_next;
            }

            var curtake = result.takes;
            if (curtake > mintake) {
                mintake = curtake;
                allowed = [];
            }
            if (curtake == mintake) {
                allowed.push(result);
            }
            
            tr += dr;
            tc += dc;
        }
        
    }
    
    allowed.takes = mintake;
    return allowed;
};


Draughts.prototype.getAllowedMoves = function(piece) {
    if (this.game.curmove < this.game.transitions.length) {
        return null;
    }

    if (!piece.piece) {
        console.log("Bad piece: "+piece);
        return null;
    }
    if (!piece.cell.pos) {
        console.log("Out of board piece: "+piece);
        return null;
    }
    
    var allowed = undefined;
    if (true) {
        var allAllowed = this.getAllAllowedMoves();
        allowed = allAllowed[piece.id];
        if (allowed == undefined && piece.color == this.game.active) {
            allowed = [];
        }
    } else {
        allowed = this.findAllowedMoves(piece, 0);
    }
    
    return allowed;
};

Draughts.prototype.directions = [
    [ 1, 1],
    [ 1,-1],
    [-1, 1],
    [-1,-1],
];

/* Unicode for pieces */
Draughts.prototype.piece2character = {
    Kb: "\u265A",
    Pb: "\u265F",
    Kw: "\u2654",
    Pw: "\u2659"
};



