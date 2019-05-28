package io.github.oliviercailloux.assisted_board_games.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;

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
