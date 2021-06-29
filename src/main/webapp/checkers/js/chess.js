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
Chess = function(options) {

    var gameExtra = {
      body              : '',
      annotations       : [],
      raw_annotations   : [],
    };
    
    this.init(this.defaults, options, gameExtra);
    this.genBoard(8,8);
    
    // If pgn was passed in, parse it (and reset to the initial position)
    if (this.settings.pgn) {
        this.parsePGN(this.settings.pgn);
        this.goStart();
    } else {
        // Load a fresh board position
        this.setupBoard( );
    }
};

// Import Game's functions into Chess's prototype
for (k in Game) {
    Chess.prototype[k] = Game[k];
}

Chess.prototype.defaults = {
    fen : "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
    json_annotations : false
}

Chess.prototype.setupBoard = function() {
    var setup =  this.parseFEN( this.settings.fen );
    if (setup==undefined && this.settings.fen != this.defaults.fen) {
        this.settings.fen = this.defaults.fen;
        this.setupBoard();
        return;
    }
    
    var template = setup[0];
    for (var j in template) {
        var row = template[j];
        for (var k in row) {
            var val = row[k];
            if (val != '-') {
                var pos = [j,k];
                this.createPiece( template[j][k].toString(), pos, undefined );
            }
        }
    }

    this.iflags = setup[1];
    for (i in this.iflags) {
        this.game[i] = this.iflags[i];
    }
};
      
Chess.prototype.createPiece = function(tpl, pos, init) {
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

// Update chess-specific information flags
Chess.prototype.runInfoTransition = function(transition) {
    var type  = transition[1];
    var value = transition[2];
    
    switch (type) {
        case 'c':
            this.game.castling[value] = transition[3];
            break;
        case 'p':
            this.game.en_passant = value;
            break;
        case 'h':
            this.game.halfmove_clock = value;
            break;
        case 'q':
            // TODO: store queening info
            //this.game.queening[value] += transition[3];
            break;
        case 'r':
            this.game.removed[value] += transition[3];
            break;
    }
};

Chess.prototype.move2transition = function(move) {
    if (this.game.curmove < this.game.transitions.length) {
        return false;
    }

    var src = move[0];
    var dest = move[1];
    var promote = move[2];
    var piece = this.pieceOn(src[0], src[1]);
    
    // check that the move is valid!
    var selected = undefined;
    var allowed = this.getAllowedMoves(piece);
    if (allowed) {
        for (var i in allowed) {
            pos = allowed[i];
            if (pos[0] == dest[0] && pos[1] == dest[1]) {
                selected = pos;
                break;
            }
        }
    }
    if (!selected) {
        console.log("["+this.game.curmove+"] No allowed move! "+move[0]+"#"+move[1]);
        return false;
    }
    
    var source = piece.cell;
    var FW = [ ['m', piece.id, dest] ]
    var BW = [ ['m', piece.id, source.pos] ];
    var tmove = {forward:FW, backward:BW};
    var a_src = piece.cell.name;
    var dstcell = this.cellAt(pos[0], pos[1]);
    var a_dst = dstcell.name;
    var en_passant = "-";
    var extra = selected.extra;
    var canPromote = false;
    
    var take = false;
    if (dstcell.content != "-") {
        take = true;
        var removed = dstcell.content;
        var rcode = removed.piece.toUpperCase()+removed.color;
        FW.unshift( ["r", removed.id] );
        BW.push( ["a", removed.id, dest] );
        FW.push( ["i", "r", rcode, 1] );
        BW.push( ["i", "r", rcode, -1] );
    }
    
    if (extra) {
        switch (extra[0]) {
            case 'm':
                var xpiece = this._pieces[ extra[1] ];
                var xcell = this.cellAt( extra[2][0],extra[2][1] );
                FW.push( ['m', xpiece.id, xcell.pos] );
                BW.unshift( ['m', xpiece.id, xpiece.cell.pos] );
                break;
            case 'r':
                var pdest = this.cellAt(extra[1][0], extra[1][1]);
                FW.unshift( ["r", pdest.content.id] );
                BW.push( ["a", pdest.content.id, pdest.pos] );
                var rcode = pdest.content.piece.toUpperCase()+pdest.content.color;
                FW.push( ["i", "r", rcode, 1] );
                BW.push( ["i", "r", rcode, -1] );
                break;
        }
    }
    
    if (selected.info) {
        switch (selected.info[0]) {
            case 'p':
                en_passant = selected.info[1];
                break;
            case 'q':
                canPromote = true;
                break;
        }
    }
    
    var piececode = piece.piece.toUpperCase();
    var pgnmove = a_src+"?"+a_dst;
    
    var dr = dest[0] - source.pos[0];
    var dc = dest[1] - source.pos[1];

    tmove.pgn = null;
    if (piececode == "P") {
        
        if (dc != 0 || take) {
            tmove.pgn = a_src[0]+"x"+a_dst;
        } else {
            tmove.pgn = a_dst;
        }
        
        // promoting
        if (canPromote) {
            if (!promote) {
                promote = "Q";
            } else {
                promote = promote.charAt(0);
            }
            
            if (promote != "P") {
                tmove.pgn += "="+promote;

                // transforme the Pawn
                FW.push( ["t", piece.id, promote] );
                BW.unshift( ["t", piece.id, piece.piece] );
                FW.push( ["i", "q", promote+piece.color, 1] );
                BW.push( ["i", "q", promote+piece.color, -1] );
            }
        }

    } else if (piececode == "K") {
        // remove castling flags
        var cf = this.game.castling[piece.color];
        if (cf[0] || cf[1]) {
            FW.push( ["i", "c", piece.color, [0,0]] );
            BW.push( ["i", "c", piece.color, cf] );
        }
    } else if (piececode == "R") {
        // remove castling flags
        var cf = this.game.castling[piece.color];
        var idx = -1;
        var row = (piece.color == "w" ? 7 : 0);
        if (source.pos[0] == row) {
            if (source.pos[1] == 0) {
                idx = 1;
            } else if (source.pos[1] == 7) {
                idx = 0;
            }
        }
        
        if (idx != -1 && cf[idx]) {
            var ncf = [ cf[0], cf[1] ];
            ncf[idx] = 0;
            FW.push( ["i", "c", piece.color, ncf] );
            BW.push( ["i", "c", piece.color, cf] );
        }
    }
    
    // regular moves
    if (tmove.pgn == null) {
        if (take) {
            tmove.pgn = piececode+a_src+"x"+a_dst;
        } else {
            tmove.pgn = piececode+a_src+a_dst;
        }
    }
    
    // update flags
    if (take || piececode == "P") {
        FW.push( ["i", "h", this.game.curmove+1] );
        BW.push( ["i", "h", this.game.halfmove_clock] );
    }
    
    if (en_passant != this.game.en_passant) {
        FW.push( ["i", "p", en_passant] );
        BW.push( ["i", "p", this.game.en_passant] );
    }

    // actually perform the move
    this.game.transitions.push(tmove);
    this.next();
    
    // test if the next player can move
    var canMove = false;
    for (var p in this._pieces) {
        var piece = this._pieces[p];
        var amoves = this.getAllowedMoves(piece);
        if (amoves && amoves.length > 0) {
            canMove = true;
            break;
        }
    }
    
    var check = false;
    if (check) {
        if (!canMove) {
            // TODO: checkmate!
        }
        var checkmate = !canMove;
    } else if (!canMove) {
        // TODO: stalemate!
    } else {
        // TODO: draw by 50 moves
    }
    
    if (this._helper && !move.noforward) {
        var srcpos = piece.cell.pos;
        var dstpos = dest.pos;
        this._helper.queueMove(this, move);
    }

};

Chess.prototype.getAllAllowedMoves = function() {
    if (this.game.curmove < this.game.transitions.length) {
        return {};
    }
    // compute all allowed moves for the current player
    var allAllowed = {}
    var color = this.game.active;
    for (idx in this._pieces) {
        var piece = this._pieces[idx]
        var allowed = getAllowedMoves(piece);
        if (allowed && allowed.length > 0) {
            allAllowed[piece] = allowed;
        }
    }
};

Chess.prototype.getAllowedMoves = function(piece) {
    if (this.game.curmove < this.game.transitions.length) {
        return null;
    }
    if (!piece.piece) {
        console.log("Bad piece: "+piece);
        return null;
    }
    if (!piece.cell.pos) {
        return null;
    }

    var type = piece.piece.toUpperCase();
    var color = piece.color;
    if (color != this.game.active) {
        return null;
    }

    var vboard = this.getVirtualBoard();
    var allowed = [];
    var curr = piece.cell.pos[0];
    var curc = piece.cell.pos[1];
    var move = undefined;
    switch(type) {
        case 'P':
            // special case for pawns
            var direction = (color=="w") ? -1 : 1;
            var tr = curr + direction;
            var tc = curc;
            var next = undefined;
            var info = undefined;
            if ( (direction==1 && curr==6) || (direction==-1 && curr==1) ) {
                next = ["q", "Select a piece", ["Q"+color,"R"+color,"N"+color,"B"+color, "P"+color]];
                info = ["q",];
            }

            if (this.isFree(tr, tc)) {
                move = [tr,tc];
                move.next = next;
                move.info = info;
                this.controlAllowedMove(allowed, vboard, piece, move);
                
                if ( (direction==1 && curr==1) || (direction == -1 && curr==6) ) {
                    tr += direction;
                    if (this.isFree(tr, tc)) {
                        move = [tr,tc];
                        move.info = ["p", [tr-direction,tc]];
                        this.controlAllowedMove(allowed, vboard, piece, move);
                    }
                }
            }
            
            var ep = this.game.en_passant;
            var tr = curr + direction;
            var tc = curc + 1;
            if (this.isEnnemy(tr,tc, color)) {
                move = [tr,tc];
                move.next = next;
                move.info = info;
                this.controlAllowedMove(allowed, vboard, piece, move);
            } else {
                if (ep!="-" && tr==ep[0] && tc==ep[1]) {
                    move = [tr,tc];
                    move.extra = ["r", [curr,tc]];
                    this.controlAllowedMove(allowed, vboard, piece, move);
                }
            }
            var tc = curc - 1;
            if (this.isEnnemy(tr,tc, color)) {
                move = [tr,tc];
                move.next = next;
                move.info = info;
                this.controlAllowedMove(allowed, vboard, piece, move);
            } else {
                if (ep!="-" && tr==ep[0] && tc==ep[1]) {
                    move = [tr,tc];
                    move.extra = ["r", [curr,tc]];
                    this.controlAllowedMove(allowed, vboard, piece, move);
                }
            }
            
            break;
        case 'K':
            // castle move
            var ca = this.game.castling[piece.color];
            var krow = piece.cell.pos[0];
            var kcol = piece.cell.pos[1];
            if ( (krow == 0 || krow == 7) && kcol == 4 && (ca[0] | ca[1]) && !this.isThreatened(krow, kcol)) {
                if (ca[0] && 
                    this.isFree(krow, kcol+1) &&
                    this.isFree(krow, kcol+2) &&
                    !this.isThreatened(krow, kcol+1) &&
                    !this.isThreatened(krow, kcol+2)
                ) {
                    if (this.isPiece(krow, 7, "R", piece.color)) {
                        var rook = this.pieceOn(krow, 7);
                        var rooktarget = [krow, 5];
                        move = [ krow, kcol+2 ];
                        move.extra = ["m", rook.id, rooktarget];
                        this.controlAllowedMove(allowed, vboard, piece, move);
                    }
                }
                if (ca[1] &&
                    this.isFree(krow, kcol-1) &&
                    this.isFree(krow, kcol-2) &&
                    this.isFree(krow, kcol-3) &&
                    !this.isThreatened(krow, kcol-1) &&
                    !this.isThreatened(krow, kcol-2)
                ) {
                    if (this.isPiece(krow, 0, "R", piece.color)) {
                        var rook = this.pieceOn(krow, 0);
                        var rooktarget = [krow, 3];
                        move = [ krow, kcol-2 ];
                        move.extra = ["m", rook.id, rooktarget];
                        this.controlAllowedMove(allowed, vboard, piece, move);
                    }
                }
            }
        case 'Q':
        case 'B':
        case 'N':
        case 'R':
            var vectors = this.pieces[type].vectors;

            // go through vectors, stop when hitting something
            for (var idx in vectors) {
                var v = vectors[idx];
                for (var i=1 ; i<=v.limit ; i++) {
                    var tr = curr + i*v.y;
                    var tc = curc + i*v.x;
                    
                    if (this.isEnnemy(tr, tc, color)) {
                        this.controlAllowedMove(allowed, vboard, piece, [tr,tc]);
                        break;
                    }
                    
                    if ( !this.isFree(tr, tc)) {
                        break;
                    }
                    this.controlAllowedMove(allowed, vboard, piece, [tr,tc]);
                }
            }
            break;
        default:
            return undefined;
    }
    return allowed;
};

Chess.prototype.controlAllowedMove = function(allowed, vboard, piece, move) {
    var crow = piece.cell.pos[0];
    var ccol = piece.cell.pos[1];
    
    var trow = move[0];
    var tcol = move[1];
    
    // play the move on the virtual board
    // FIXME: it does not work for "en passant" border cases.
    var backup = vboard[trow][tcol];
    vboard[trow][tcol] = vboard[crow][ccol];
    vboard[crow][ccol] = "-";
    
    if (!this.isVirtualThreatened(vboard, piece.color)) {
        allowed.push(move);
    }
    
    // cancel the move
    vboard[crow][ccol] = vboard[trow][tcol];
    vboard[trow][tcol] = backup;
};

// Test if a place is threatened
Chess.prototype.isVirtualThreatened = function(vboard, curcolor) {
    if (curcolor == undefined) {
        curcolor = this.game.active;
    }
    var color = (curcolor == "b") ? "w" : "b";

    // find the king!
    var row,col;
    for (var j in vboard) {
        var vrow = vboard[j];
        var found = false;
        for (k in vrow) {
            if (vrow[k][0]=="K" && vrow[k][1] == curcolor) {
                row = j;
                col = k;
                found = true;
                break;
            }
        }
        if (found) {
            break;
        }
    }
    
    if (row==undefined) {
        console.log("could not find the king!!" + vboard);
        return true;
    }

    // Pawn threat
    var direction = (color=="w") ? -1 : 1;
    var srow = row - direction;
    var scol = col - 1;
    if (this.isVirtualPiece(vboard, srow, col-1, "P", color) ||
        this.isVirtualPiece(vboard, srow, col+1, "P", color)) {
        return true;
    }

    for (var type in this.pieces) {
        var vectors = this.pieces[type].vectors;
        // go through vectors, stop when hitting something
        for (var idx in vectors) {
            var v = vectors[idx];
            for (var i=1 ; i<=v.limit ; i++) {
                var tr = row - i*v.y;
                var tc = col - i*v.x;
                
                if (this.isVirtualPiece(vboard, tr, tc, type, color)) {
                    return true;
                }
                
                if ( !this.isVirtualFree(vboard, tr, tc)) {
                    break;
                }
            }
        }
    }
    return false;
};


// Test if a place is threatened
Chess.prototype.isThreatened = function(row, col, curcolor) {
    if (curcolor == undefined) {
        curcolor = this.game.active;
    }
    var color = (curcolor == "b") ? "w" : "b";

    // Pawn threat
    var direction = (color=="w") ? -1 : 1;
    var srow = row - direction;
    var scol = col - 1;
    if (this.isPiece(srow, col-1, "P", color) ||
        this.isPiece(srow, col+1, "P", color)) {
        return true;
    }


    for (var type in this.pieces) {
        var vectors = this.pieces[type].vectors;
        // go through vectors, stop when hitting something
        for (var idx in vectors) {
            var v = vectors[idx];
            for (var i=1 ; i<=v.limit ; i++) {
                var tr = row - i*v.y;
                var tc = col - i*v.x;
                
                if (this.isPiece(tr, tc, type, color)) {
                    return true;
                }
                
                if ( !this.isFree(tr, tc)) {
                    break;
                }
            }
        }
    }
    return false;
};

Chess.prototype.parseFEN = function(fen) {
    // rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2
    var pattern = /\s*([rnbqkpRNBQKP12345678]+\/){7}([rnbqkpRNBQKP12345678]+)\s[bw-]\s(([kqKQ]{1,4})|(-))\s(([a-h][1-8])|(-))\s\d+\s\d+\s*/;
    if ( !pattern.test(fen) ) {
        return undefined;
    }

    var parts     = fen.replace(/^\s*/, "").replace(/\s*$/, "").split(/\/|\s/);

    var new_board     = [];
    for (var j = 0;j < 8; j++) {
      new_board[j] = [];
      var row = parts[j].replace(/\d/g, this.replaceNumberWithDashes);
      for (var k=0;k<8;k++) {
        new_board[j][k] = row.substr(k, 1);
      }
    }

    var info = {};
    info.active = parts[8].toLowerCase();
    info.en_passant = parts[10];
    info.halfmove_clock = parseInt(parts[11]);
    info.move_clock = (parseInt(parts[12])-1)*2;
    if (info.active == "b") {
        info.move_clock++;
    }

    var castling = parts[9];
    info.castling = {"w": [0,0], "b":[0,0]};
    if (castling != "-") {
        for (var i in castling) {
            var ctype = castling[i];
            var color = "w";
            if (ctype.toUpperCase() == ctype) {
                color = "b";
            }
            ctype = ctype.toUpperCase();
            if (ctype == "K") {
                info.castling[color][0] = 1;
            } else {
                info.castling[color][1] = 1;
            }
        }
    }
    
    info.removed = {
        Rw:0, Nw:0, Bw:0, Qw:0, Pw:0,
        Rb:0, Nb:0, Bb:0, Qb:0, Pb:0,
    };

    return [new_board, info];
};

Chess.prototype.parsePGN = function(pgn) {
    // Do a little clean up on the string
    var thegame = this;
    pgn = pgn.trim().replace(/\n|\r/g, ' ').replace(/\s+/g, ' ');
    // Recognize escaped closing curly brackets as part of the comment
    // This allows us to have json encoded comments
    pgn = pgn.replace(/\{((\\})|([^}]))+}/g, function() {
        thegame.pluckAnnotation.apply(thegame, arguments);
    });

    var headers = ['Event','Site','Date','Round','White','Black','Result', 'FEN'];
    for (var i=0; i < headers.length; i++) {
        var re      = new RegExp(headers[i] + ' "([^"]*)"]');
        var result  = re.exec(pgn);
        this.game.header[headers[i]] = (result == null) ? "" : result[1];
    }

    // apply the FEN headers
    var fen = this.game.header['FEN'];
    if (fen) {
        this.settings.fen = fen;
    }
    this.setupBoard();


    // Find the body
    this.game.body = /(1\. ?([RNBQK]?[a-h]?[1-8]?x?[a-h][1-8]).*)/m.exec(pgn)[1];

    // Remove numbers, remove result
    this.game.body = this.game.body.replace(new RegExp("1-0|1/2-1/2|0-1"), '');
    this.game.body = this.game.body.replace(/(\s?)\d+\.\s*\.*/g, '$1');

    var moves = this.game.body.trim().split(/\s+/);

    // This must be a separate variable from i, since annotations don't
    // count as moves.
    var move_number = 0;
    for (var i in moves) {
        this.addMove(moves[i]);
    }
};

Chess.prototype.addMove = function(move) {
    var move_number = this.game.curmove;
    if ( /annotation-\d+/.test(move) ) {
        this.game.annotations[move_number] = this.game.raw_annotations.shift();
        return;
    }
    this.game.moves[move_number] = move;
    var player = (move_number % 2 == 0) ? 'w' : 'b';

    if ( this.patterns.explicit_move.test(move) ) {
        var m = this.patterns.explicit_move.exec(move);
        var piece = m[1];
        var src = this.algebraic2Coord( m[2] );
        var dst = this.algebraic2Coord( m[3] );

        this.queueMove(src, dst);
        
    // If the move was to castle
    } else if ( this.patterns.castle_queenside.test(move) ) {
        var rank = (player == 'w') ? 7 : 0;
        this.queueMove( [rank,4], [rank, 2]);

    } else if ( this.patterns.castle_kingside.test(move) ) {
        var rank = (player == 'w') ? 7 : 0;
        this.queueMove( [rank,4], [rank, 6]);

    // If the move was a piece
    } else if ( this.patterns.piece_move.test(move) ) {
        var m = this.patterns.piece_move.exec(move);

        var piece = m[1];
        var src_file = m[2];
        var src_rank = m[3];
        var dest = this.algebraic2Coord(m[4]);

        var src_col = undefined;
        var src_row = undefined;
        if (src_file != "") {
            src_col = src_file.charCodeAt(0) - ('a').charCodeAt(0);
        }
        if (src_rank != "") {
            src_row = 8-src_rank;
        }
        var src = this.findMoveSource(piece, src_row, src_col, dest, player);
        this.queueMove( src, dest);

    // If the move was a pawn
    } else {
        var dst_file = null;
        var dst_rank = null;

        // Queening
        var promote = null;
        if ( this.patterns.pawn_queen.test(move) ) {
            var m = this.patterns.pawn_queen.exec(move);
            promote = m[1];
        }
      
        // Pawn capture
        if ( this.patterns.pawn_capture.test(move) ) {
            var m   = this.patterns.pawn_capture.exec(move);
//alert(m[2]+m[3] +"::"+ m[1]+m[3]);            
            var dst = this.algebraic2Coord( m[2]+m[3] );
            var src = this.algebraic2Coord( m[1] + (parseInt(m[3])+( (player == 'w') ? -1 : 1 ) ) );

            this.queueMove( src, dst, promote);
        } else 
        if ( this.patterns.pawn_move.test(move) ) {
            var m    = this.patterns.pawn_move.exec(move);
            dst_file = m[1];
            dst_rank = m[2];
            var dst  = this.algebraic2Coord( dst_file + dst_rank );
            var src = this.findMoveSource("P", undefined, undefined, dst, player);
            this.queueMove(src, dst, promote);
        }
    }
};


Chess.prototype.findMoveSource = function(piece, src_row, src_col, dest, player) {
    if ( src_row && src_col ) {
        return [src_row, src_col];
    }
    
    for (var p in this._pieces) {
        var curpiece = this._pieces[p];
        if (curpiece.color != player || curpiece.piece.toUpperCase() != piece) {
            continue;
        }
        var curpos = curpiece.cell.pos;
        if (src_row != undefined && curpos[0] != src_row) {
            continue;
        }
        if (src_col != undefined && curpos[1] != src_col) {
            continue;
        }
        
        var allowed = this.getAllowedMoves(curpiece);
        if (!allowed) {
            continue;
        }
        
        for (var i in allowed) {
            var pos = allowed[i];
            if (pos[0] == dest[0] && pos[1] == dest[1]) {
                return curpos;
            }
        }
    }
    
    console.log("could not find a source!");
    return undefined;
};


Chess.prototype.replaceNumberWithDashes = function(str) {
    var num_spaces = parseInt(str);
    var new_str = '';
    for (var i = 0; i < num_spaces; i++) { new_str += '-'; }
    return new_str;
};

Chess.prototype.pluckAnnotation = function(str) {
    this.game.raw_annotations = this.game.raw_annotations || [];
    var ann_num = this.game.raw_annotations.length;
    var annot   = str.substring(1,str.length-1); // Remove curly brackets
    annot       = annot.replace(/\\\{/g, '{');
    annot       = annot.replace(/\\\}/g, '}');

    if (this.settings.json_annotations) {
      eval("annot = " + annot);
    }

    this.game.raw_annotations.push(annot);
    return "annotation-" + ann_num;
};


/* Patterns used for parsing */
Chess.prototype.patterns = {
    castle_kingside     : /^O-O/,
    castle_queenside    : /^O-O-O/,

    explicit_move       : /^([BKNQR]?)([a-h][1-8])x?([a-h][1-8])/,

    piece_move          : /^([BKNQR])([a-h]?)([1-8]?)x?([a-h][1-8])/,

    pawn_move           : /^([a-h])([1-8])/,
    pawn_capture        : /^([a-h])[1-8]?x([a-h])([1-8])/,

    pawn_queen          : /=([BNQR])/,
};

/* Unicode for pieces */
Chess.prototype.piece2character = {
    Kb: "\u265A",
    Qb: "\u265B",
    Rb: "\u265C",
    Bb: "\u265D",
    Nb: "\u265E",
    Pb: "\u265F",
    Kw: "\u2654",
    Qw: "\u2655",
    Rw: "\u2656",
    Bw: "\u2657",
    Nw: "\u2658",
    Pw: "\u2659"
};

/* Definitions of pieces */
Chess.prototype.pieces = {
    R : {
      vectors : [
        { x :  0, y :  1, limit : 8 },
        { x :  1, y :  0, limit : 8 },
        { x :  0, y : -1, limit : 8 },
        { x : -1, y :  0, limit : 8 }
      ]
    },
    N : {
      vectors : [
        { x :  1, y :  2, limit : 1 },
        { x :  2, y :  1, limit : 1 },
        { x :  2, y : -1, limit : 1 },
        { x :  1, y : -2, limit : 1 },
        { x : -1, y : -2, limit : 1 },
        { x : -2, y : -1, limit : 1 },
        { x : -2, y :  1, limit : 1 },
        { x : -1, y :  2, limit : 1 }
      ]
    },
    B : {
      vectors : [
        { x :  1, y :  1, limit : 8 },
        { x :  1, y : -1, limit : 8 },
        { x : -1, y : -1, limit : 8 },
        { x : -1, y :  1, limit : 8 }
      ]
    },
    Q : {
      vectors : [
        { x :  0, y :  1, limit : 8 },
        { x :  1, y :  0, limit : 8 },
        { x :  0, y : -1, limit : 8 },
        { x : -1, y :  0, limit : 8 },

        { x :  1, y :  1, limit : 8 },
        { x :  1, y : -1, limit : 8 },
        { x : -1, y : -1, limit : 8 },
        { x : -1, y :  1, limit : 8 }
      ]
    },
    K : {
      vectors : [
        { x :  0, y :  1, limit : 1 },
        { x :  1, y :  0, limit : 1 },
        { x :  0, y : -1, limit : 1 },
        { x : -1, y :  0, limit : 1 },

        { x :  1, y :  1, limit : 1 },
        { x :  1, y : -1, limit : 1 },
        { x : -1, y : -1, limit : 1 },
        { x : -1, y :  1, limit : 1 }
      ]
    }
}

