package io.github.oliviercailloux.y2018.assisted_board_games.utils;

import java.util.List;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;

import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessMoveEntity;

public class GameHelper {
	public static Board playMoves(List<ChessMoveEntity> allMoves) throws MoveException {
		Board board = new Board();

		for (ChessMoveEntity move : allMoves) {
			if (!(board.doMove(
					new Move(Square.valueOf(move.getFrom()), Square.valueOf(move.getTo())),
					true))) {
				throw new MoveException();
			}
		}
		return board;
	}
}
