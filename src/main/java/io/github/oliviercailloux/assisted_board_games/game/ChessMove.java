package io.github.oliviercailloux.assisted_board_games.game;

import javax.json.Json;
import javax.json.JsonObject;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;

/**
 * 
 * @author Megan Brassard
 * @author Theophile Dano
 *
 */
public class ChessMove {

    public static JsonObject encode(Move move) {
        return Json.createObjectBuilder()
                .add("from", move.getFrom().toString())
                .add("to", move.getTo().toString())
                .add("promotion", move.getPromotion().toString())
                .build();
    }

    public static Move decode(JsonObject json) {
        String from = json.getString("from");
        String to = json.getString("to");
        String promotion = json.getString("promotion");
        return new Move(Square.valueOf(from), Square.valueOf(to), Piece.valueOf(promotion));
    }

    public static Move getMove(Board fromPosition, Board toPosition) throws MoveException {
        Piece[] fromPieces = fromPosition.boardToArray();
        Piece[] toPieces = toPosition.boardToArray();
        Square from = Square.NONE;
        Square to = Square.NONE;
        Piece promotion = Piece.NONE;
        for (int i = 0; i < fromPieces.length; i++) {
            if (fromPieces[i] != toPieces[i]) {
                if (Piece.NONE == toPieces[i]) {
                    from = Square.values()[i];
                }
                if (Piece.NONE == fromPieces[i]) {
                    to = Square.values()[i];
                }
            }
        }
        if (fromPosition.getPiece(from) != toPosition.getPiece(to)) {
            promotion = toPosition.getPiece(to);
        }
        Move move = new Move(from, to, promotion);
        if (Square.NONE == from) {
            return move;
        }
        if (fromPosition.isMoveLegal(move, false)) {
            return move;
        }
        throw new MoveException("Move is not legal");
    }

    public static Board doMove(Board board, Move move) throws MoveException {
        Board boardCopy = board.clone();
        if (boardCopy.doMove(move)) {
            return boardCopy;
        }
        return null;
    }
}
