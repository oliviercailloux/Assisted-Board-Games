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

// Make sure that console.log() is available
if (typeof console == "undefined") { var console = { log: function() {} }; }

jQuery.debug = function(msg) {
    console.log(msg);
    debugdiv = $("#debug");
    if (debugdiv) {
        $("#debug").html(msg.toString());
    }
    return false;
};


// Helper to animate a sprite moving from a container to another
jQuery.moveSprite = function(sprite, dest, atime) {
    if (atime == undefined) {
        atime = 200;
    }

    var parent = sprite.parent();
    var baseoffset = parent.offset();
    var dstoffset = dest.offset();

    var dx = (dstoffset.left - baseoffset.left) +"px";
    var dy = (dstoffset.top - baseoffset.top) +"px";

    var curdx = sprite.css("left");
    var curdy = sprite.css("top");
    
    if ( dx != curdx || dy != curdy ) {
        sprite.animate({ opacity: 1, top : dy, left : dx }, atime);
    } else {
        sprite.animate({ opacity: 1 }, atime);
    }
};

// disable tap-to-zoom on specific elements
// source: http://stackoverflow.com/questions/10614481/disable-double-tap-zoom-option-in-browser-on-touch-devices
(function($) {
  $.fn.nodoubletapzoom = function() {
      $(this).bind('touchstart', function preventZoom(e) {
        var t2 = e.timeStamp
          , t1 = $(this).data('lastTouch') || t2
          , dt = t2 - t1
          , fingers = e.originalEvent.touches.length;
        $(this).data('lastTouch', t2);
        if (!dt || dt > 500 || fingers > 1) return; // not double-tap

        e.preventDefault(); // double tap - prevent the zoom
        // also synthesize click events we just swallowed up
        $(this).trigger('click');//.trigger('click');
      });
  };
})(jQuery);


/* The Board itself: group of generic methods */
Board = {
    
// Init: draw the empty board
init : function(parent, viewOptions) {

    this.settings = {
        className:  "board",
        spritePath: "theme/%s.svg",
        boardThemes: ["classic", "green", "blue", "red", "brown"],
        controls: true,
        flipped: false,
        play: {w:true, b:true},
    }
    
    if (this.defaults) {
        for (k in this.defaults) {
            this.settings[k] = this.defaults[k];
        }
    }

    if (viewOptions) {
        for (k in viewOptions) {
            this.settings[k] = viewOptions[k];
        }
    }

    var theboard = this;
    this.parent = parent;
    parent.empty();
    var wrapper = $('<div class="'+this.settings.className+'">')
    var curtheme = this.settings.boardThemes[0];
    if (this.settings.boardThemes.length > 1) {
        wrapper.append("<br>Board:");
        var combo = $("<select>");
        for (var t in this.settings.boardThemes) {
            var curt = this.settings.boardThemes[t];
            var option = $("<option value='"+curt+"'>"+curt+"</option>");
            combo.append(option);
        }
        combo.change(theboard, function(e) { e.data.setBoardTheme($(this).val());});
        wrapper.append(combo);
    }

    this._boardElement = $('<div class="board_'+curtheme+'">');

    var fliplink = $("<button type='button'>Flip</button>");
    fliplink.click( function() {theboard.flipBoard(); return false;} );
    wrapper.append(" ");
    wrapper.append( fliplink );

    this._modal = $("<div class='jqmWindow'>");
    wrapper.append(this._modal);
    this._modal.jqm({modal:true});

    wrapper.append(this._boardElement);
    $(parent).append(wrapper);

    var gameboard = this.game.boardData();

    var n = gameboard.length;

    this.createSurfaces(gameboard);
    this.createSprites(this.game._pieces);
    this.writeBoardTable();

    // space for game-specific info
    var block = $("<div class='info'>");
    this.writeBoardInfo(block);
    this._boardElement.append(block);

    // control buttons
    if (this.settings.controls) {
        this.controls = [];
        var game = this.game;
        block = $("<div class='controls'>");
        
        var imgurl = this.settings.spritePath.replace("%s", "start")
        var link = $("<img src='"+imgurl+"' class='bcontrol'>");
        var cb = function() {  game.goStart(); return false; };
        link.click(cb);
        this.controls.start = link;

        imgurl = this.settings.spritePath.replace("%s", "prev")
        link = $("<img src='"+imgurl+"' class='bcontrol'>");
        link.click(function() {  game.prev(); return false;});
        this.controls.prev = link;

        imgurl = this.settings.spritePath.replace("%s", "next")
        link = $("<img src='"+imgurl+"' class='bcontrol'>");
        link.click( function() {game.next(); return false;} );
        this.controls.next = link;

        imgurl = this.settings.spritePath.replace("%s", "end")
        link = $("<img src='"+imgurl+"' class='bcontrol'>");
        link.click( function() {game.goEnd(); return false;} );
        this.controls.end = link;

        this.controls.state = $("<span>");

        block.nodoubletapzoom();
        this.controls.next.nodoubletapzoom();
        this.controls.prev.nodoubletapzoom();
        
        block.append( this.controls.end );
        block.append( this.controls.next );
        block.append( this.controls.prev );
        block.append( this.controls.start );
        block.append( this.controls.state );
        
        this._boardElement.append(block);
    } else {
        this.controls = undefined;
    }

    this.fillBoard();
    this.game._view = this;
},

writeBoardInfo : function() {
},

setBoardTheme: function(theme) {
    this._boardElement.removeClass();
    this._boardElement.addClass("board_"+theme);
    },

    flipBoard : function() {
    this.settings.flipped = !this.settings.flipped;
    this.writeBoardTable();
    // reset all positions to get a less-weird animation
    this.fillBoard(0);
},

createSurfaces : function(theboard) {
    var n = theboard.length;
    var view = this;
    for ( var j=0; j<n ; j++) {
        var row = theboard[j];
        for ( var k=0; k<n ; k++) {
            var cell = row[k];
            var surface = $("<div class='place_"+cell.color+"'>");
            var surfacecontent = $("<div class='placelink'>");
            surface.append(surfacecontent);
            cell.surface = surface;
            surfacecontent.click(cell, function(e) { view.boardClicked(e); } );
        }
    }
},
  
createSprites : function(pieces) {
    for (var i in pieces) {
        this.createSprite( pieces[i] );
    }
},

createSprite : function(piece) {
    var code = piece.piece.toUpperCase()+piece.color;
    var character = this.game.piece2character[code];
    //var img = "theme/"+code+".svg";
    var img = this.settings.spritePath.replace("%s",code);
    var sprite = $("<img src='"+img+"' class='piece' alt='"+character+"'>");
    sprite.css("top", 0);
    sprite.css("left", 0);
    piece.sprite = sprite;
    piece.cell.surface.append(piece.sprite);
    if (piece.init == "-") {
        sprite.css("opacity", 0);
    }
},
      
writeBoardTable: function() {
    if (this._tableBoard == undefined) {
        this._tableBoard = $("<table>");
        this._boardElement.append(this._tableBoard);
    } else {
        this._tableBoard.empty();
    }
    var theboard = this;
    var gameboard = this.game.boardData();
    var n = gameboard.length;
    for ( var j=0; j<n ; j++) {
        row = $("<tr>");
        for ( var k=0; k<n ; k++) {
            cell = $("<td>");
            var place = gameboard[j][k];
            var surface = place.surface;
            cell.append(surface);
            if (this.settings.flipped) {
                row.prepend(cell);
            } else {
                row.append(cell);
            }
        }
        if (this.settings.flipped) {
            this._tableBoard.prepend(row)
        } else {
            this._tableBoard.append(row)
        }
    }
},

fillBoard : function(atime) {
    if (atime == undefined) {
        atime = 200;
    }

    this._selected = undefined;
    this.parent.find(".selected").removeClass("selected");
    var theboard = this.game.boardData();

    for (idx in this.game._pieces) {
        piece = this.game._pieces[idx];
        if (piece.transformed) {
            var code = piece.piece.toUpperCase()+piece.color;
            var character = this.game.piece2character[code];
            var src = this.settings.spritePath.replace("%s", code);
            piece.sprite.attr("src", src);
            piece.sprite.attr("alt", character);
            piece.transformed = undefined;
        }
        if (piece.cell == 'r') {
            piece.sprite.animate({ opacity : 0 }, atime);
        } else if (piece.cell == 'h') {
            piece.sprite.animate({ opacity : 0 }, atime);
        } else {
            $.moveSprite(piece.sprite, piece.cell.surface, atime);
        }
    }

    if (this.controls) {
        var totalMoves = this.game.getMoveCount();
        var curMove = this.game.getCurMoveNumber();
        
        var acl = "active";
        if (curMove > 0) {
            this.controls.start.addClass(acl);
            this.controls.prev.addClass(acl);
        } else {
            this.controls.start.removeClass(acl);
            this.controls.prev.removeClass(acl);
        }

        if (curMove < totalMoves) {
            this.controls.state.empty();
            this.controls.state.append("Move "+curMove+" of "+totalMoves);
            this.controls.next.addClass(acl);
            this.controls.end.addClass(acl);
        } else {
            this.controls.state.empty();
            this.controls.next.removeClass(acl);
            this.controls.end.removeClass(acl);
        }
        
    }

    this.fillBoardInfo();
},

fillBoardInfo : function() {
},


boardClicked : function(e) {
    var cell = e.data;

    if ( !this.settings.play[this.game.game.active] ) {
        return;
    }
    
    // reset selected class
    this.parent.find(".selected").removeClass("selected");
    
    // start or continue a move
    if (this._selected) {
        this.selectNext(cell);
    } else {
        this.initMove(cell);
    }
},

selectNext: function(cell) {

    var sel_cell = this._selected[0];
    var allowed = this._selected[1];
    var piece = sel_cell.content;

    for (var idx in allowed) {
        var curmove = allowed[idx];
        if (curmove[0] == cell.pos[0] && curmove[1] == cell.pos[1]) {
            this._selected[2].push(curmove);
            var next = curmove.next;
            if (next) {
                if (next[0] == "q") {
                    this._selected[1] = next;
                } else {
                    this._selected[1] = next;
                    this.previewMove(piece, curmove);
                    // TODO: keep selecting
                }
            } else {
                this._selected[1] = true;
            }
            this.continueMove();
            return;
        }
    }

    this.initMove(cell);

},

initMove : function(cell) {
    // mark the parent cell as selected
    if (cell.content != "-") {
        var piece = cell.content;
        var allowed = this.game.getAllowedMoves(piece);
        if (allowed == undefined) {
            return;
        }
        this._selected = [cell, allowed, [cell.pos]];
        cell.surface.addClass("selected");
        this.continueMove();
    }
},

continueMove : function() {
    if (!this._selected) {
        return;
    }
    
    var allowed = this._selected[1];
    if (!allowed) {
        return;
    }
    
    if (allowed == true) {
        this.game.queueMove(this._selected[2]);
        this._selected = undefined;
        return;
    }
    
    if (allowed[0] == "q") {
        this.askPlayer(allowed[1], allowed[2]);
        return;
    }

    var cell = this._selected[0];
    var gameboard = this.game.boardData();
    for (var idx=0 ; idx<allowed.length ; idx++) {
        var pos = allowed[idx];
//console.log("CM: "+pos);
        var targetcell = gameboard[pos[0]][pos[1]];
        targetcell.surface.addClass("selected");
    }
},

askPlayer: function(title, options) {
    var div = this._modal;
    this._modal.empty();
    var theboard = this;
    div.append( $("<h1>"+title+"</h1>") )
    for (var idx in options) {
        var o = options[idx];
        var imgpath = this.settings.spritePath.replace("%s", o);
        var img = $("<img src='"+imgpath+"'>");
        div.append(img)
        img.click(o, function(e) {theboard.playerSelected(e.data)} );
    }
    div.append("<p>")
    var cancel = $("<button>cancel</button>");
    cancel.click(function() {theboard.cancelMove()});
    div.append( cancel );
    
    
    div.jqmShow();
},

cancelMove : function() {
    this._modal.jqmHide();
    this._modal.empty();
    this._selected = undefined;
},

playerSelected : function(o) {
    this._selected[2].push(o);
    this._selected[1] = true;
    this._modal.jqmHide();
    this._modal.empty();
    this.continueMove();
},

previewMove : function(piece, move) {
    var cell = this.game.cellAt(move[0], move[1]);
    var atime = 200;
    $.moveSprite(piece.sprite, cell.surface, atime);
    if (move.extra) {
        if (move.extra[0] == "r") {
            var removed = this.game.pieceOn( move.extra[1][0], move.extra[1][1] );
            removed.sprite.css( "opacity", .2 );
        }
    }
},

} // END of Board


