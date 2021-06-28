/*
 * webboard - jQuery-based board view for chess and checkers
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


/* The Chess Board */
ChessBoard = function(parent, gameOptions, viewOptions) {
    this.game = new Chess(gameOptions);
    this.init(parent, viewOptions);
};

// Import Board's functions into ChessBoard's prototype
for (k in Board) {
    ChessBoard.prototype[k] = Board[k];
}

ChessBoard.prototype.defaults = {
    className:  "chess_board",
    spritePath: "theme/%s.svg",
};

ChessBoard.prototype.takenOrder   = ["Q", "R","R", "B","B", "N","N", "P"];
ChessBoard.prototype.promoteOrder = ["Q","R","B","N"];

// create the space for game information panel
ChessBoard.prototype.writeBoardInfo = function(parent) {
    this.taken_images = {};
    var colors = ["w", "b"];
    for (var c in colors) {
        var color = colors[c];
        var taken = $("<div class='taken'>");
        var prev = undefined;
        for (var p in this.takenOrder) {
            var l = this.takenOrder[p];
            var code = l+color;
            var img_source = this.settings.spritePath.replace("%s",code);
            var img = $("<img src='"+img_source+"' >");
            img.hide();
            if (l == prev) {
                code += "2";
            } else {
                prev = l;
            }
            this.taken_images[code] = img;
            taken.append(img);
        }
        var counter = $("<span>");
        this.taken_images["P"+color+"c"] = counter;
        taken.append(counter);
        parent.append(taken);
    }
};

// update the game information panel
ChessBoard.prototype.fillBoardInfo = function() {

    var colors = ["w", "b"];
    for (var c in colors) {
        var color = colors[c];
        var prev = undefined;
        for (var p in this.takenOrder) {
            var l = this.takenOrder[p];
            var code = l+color;
            if (l == prev) {
                continue;
            }
            var count   = this.game.game.removed[code];
            var img     = this.taken_images[code];
            var img2    = this.taken_images[code+"2"];
            var counter = this.taken_images[code+"c"];
            
            img.removeClass("overCounted");
            if (img2) {
                img2.removeClass("overCounted");
            }
            if (counter) {
                counter.empty();
            }
            
            if (count < 1) {
                img.hide();
                if (img2) {
                    img2.hide();
                }
            } else if (count == 1) {
                img.show();
                if (img2) {
                    img2.hide();
                }
            } else if (count == 2) {
                img.show();
                if (img2) {
                    img2.show();
                } else if (counter) {
                    counter.html("x2");
                } else {
                    img.addClass("overCounted");
                }
            } else if (count > 2) {
                img.css("opacity", 1);
                if (img2) {
                    img2.show();
                    img2.addClass("overCounted");
                } else if (counter) {
                    counter.html("x"+count);
                } else {
                    img.addClass("overCounted");
                }
            }
            prev = l;
        }
    }
    
    var div = $("#state");
    var msg = "";
    if (div && this.game.game.castling) {
        var g = this.game.game;
        msg = g.active+
            "  castling ["+g.castling.w+"|"+g.castling.b+"]" +
            "  PEP(" + g.en_passant+")" +
            "  " + g.move_clock +"[" + g.halfmove_clock + "]";

        msg += "DEL: ["+this.getRemovedInfo("w") +" | "+ this.getRemovedInfo("b")+"]";
        
        div.html(msg.toString());
    }
};


ChessBoard.prototype.getRemovedInfo = function(color) {
    var order = ["Q","R","B","N","P"];
    var msg = "";
    for (var idx in order) {
        var p = order[idx];
        var count = this.game.game.removed[p+color]
        
        if (!count) {
            //msg += " !"+p;
        } else if (count == 1) {
            msg += p+" "
        } else if (count == 2) {
            msg += p+p+" "
        } else if (count > 2) {
            msg += count+p+" "
        }
    }
    return msg;
};


