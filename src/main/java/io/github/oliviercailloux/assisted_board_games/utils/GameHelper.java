package io.github.oliviercailloux.assisted_board_games.utils;

import java.util.List;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveException;

import io.github.oliviercailloux.assisted_board_games.model.MoveEntity;

public class GameHelper {

    public static Board playMoves(List<MoveEntity> moves) throws MoveException {
        Board board = new Board();
        for (MoveEntity move : moves) {
            if (!board.doMove(MoveEntity.asMove(move), true)) {
                throw new MoveException();
            }
        }
        return board;
    }
}
