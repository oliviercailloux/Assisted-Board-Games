package io.github.oliviercailloux.assisted_board_games.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;

import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.model.MoveEntity;

/**
 * 
 * @author Megan Brassard
 * @author Theophile Dano
 * @author Thais Piganeau
 *
 */
@RequestScoped
public class MoveService {

    @Inject
    ChessService chessService;

    public JsonObject encode(MoveEntity move) {
        return Json.createObjectBuilder()
                        .add("from", move.getFrom().toString())
                        .add("to", move.getTo().toString())
                        .add("promotion", move.getPromotion().toString())
                        .build();
    }

    public MoveEntity decode(JsonObject json) {
        Square from = Square.valueOf(json.getString("from"));
        Square to = Square.valueOf(json.getString("to"));
        Piece promotion = Piece.valueOf(json.getString("promotion"));
        int gameId = json.getInt("gid");
        GameEntity game = chessService.getGame(gameId);
        return game.makeMove(from, to, promotion);
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
