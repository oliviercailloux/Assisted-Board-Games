package io.github.oliviercailloux.assisted_board_games.model;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.github.bhlangonijr.chesslib.Board;

/**
 * 
 * @author elbaylot
 *
 */
@Entity
@Table(name = "boards")
public class ChessBoard {

    @Id
    @GeneratedValue
    int id;
    String fen;

    ChessBoard() {
        this.fen = GameEntity.STARTING_FEN_POSITION;
    }

    private ChessBoard(Board board) {
        checkNotNull(board);
        this.fen = board.getFen(true);
    }

    public static ChessBoard createChessBoard() {
        return new ChessBoard();
    }

    public static ChessBoard createChessBoard(Board board) {
        return new ChessBoard(board);
    }

    public int getId() {
        return id;
    }

    public String getFen() {
        return fen;
    }

    public Board asBoard() {
        final Board board = new Board();
        board.loadFromFen(fen);
        return board;
    }
}
