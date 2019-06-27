package io.github.oliviercailloux.assisted_board_games.utils;

import java.util.List;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveException;

import io.github.oliviercailloux.assisted_board_games.model.MoveEntity;

public class GameHelper {

    public static Board playMoves(Board board, List<MoveEntity> moves) throws MoveException {
        for (MoveEntity move : moves) {
            if (!board.doMove(move.asMove(), true)) {
                throw new MoveException();
            }
        }
        return board;
    }

    public static Board playMoves(List<MoveEntity> moves) throws MoveException {
        return playMoves(new Board(), moves);
    }
}
