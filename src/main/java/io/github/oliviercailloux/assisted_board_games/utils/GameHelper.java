package io.github.oliviercailloux.assisted_board_games.utils;

import java.util.List;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveException;

import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.model.MoveEntity;

public class GameHelper {

    public static Board playMoves(String fromPosition, List<MoveEntity> moves) throws MoveException {
        Board board = new Board();
        board.loadFromFen(fromPosition);
        for (MoveEntity move : moves) {
            if (!board.doMove(move.asMove(), true)) {
                throw new MoveException();
            }
        }
        return board;
    }

    public static Board playMoves(List<MoveEntity> moves) throws MoveException {
        return playMoves(GameEntity.STARTING_FEN_POSITION, moves);
    }
}
