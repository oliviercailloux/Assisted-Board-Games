package io.github.oliviercailloux.assisted_board_games.checkers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jboss.weld.util.collections.ImmutableMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Maps;

import io.github.oliviercailloux.assisted_board_games.checkers.CheckerBoard;
import io.github.oliviercailloux.assisted_board_games.checkers.Piece;
import io.github.oliviercailloux.assisted_board_games.checkers.PieceSort;
import io.github.oliviercailloux.assisted_board_games.checkers.Square;
import io.github.oliviercailloux.assisted_board_games.two_players.Side;

class CheckersTests {
	@Test
	void testSquare() {
		assertThrows(IllegalArgumentException.class, () -> Square.given(-15), "Expected to throw IllegalArgument exception, but it didn't");
		assertThrows(IllegalArgumentException.class, () -> Square.given(0), "Expected to throw IllegalArgument exception, but it didn't");
		assertThrows(IllegalArgumentException.class, () -> Square.given(51), "Expected to throw IllegalArgument exception, but it didn't");
		assertEquals(1, Square.given(1).getSquareNumber(), "Actual square number doesn't match expected one");
		assertEquals(24, Square.given(24).getSquareNumber(), "Actual square number doesn't match expected one");
		assertEquals(50, Square.given(50).getSquareNumber(), "Actual square number doesn't match expected one");
		
		assertEquals("Square{square number=37}", Square.given(37).toString());
		assertEquals(Square.given(38), Square.given(38));
		assertEquals(Square.given(38).hashCode(), Square.given(38).hashCode());
		
		assertNotEquals(Square.given(43), Square.given(23));
		assertNotEquals(Square.given(43).hashCode(), Square.given(23).hashCode());
	}
	
	@Test
	void testPiece() {
		assertThrows(IllegalArgumentException.class, () -> Piece.given(null, null), "Expected to throw IllegalArgument exception, but it didn't");
		assertThrows(IllegalArgumentException.class, () -> Piece.given(PieceSort.NormalPiece, null), "Expected to throw IllegalArgument exception, but it didn't");
		assertThrows(IllegalArgumentException.class, () -> Piece.given(null, Side.White), "Expected to throw IllegalArgument exception, but it didn't");
		
		assertEquals(Piece.given(PieceSort.NormalPiece, Side.White), Piece.given(PieceSort.NormalPiece, Side.White));
		assertNotEquals(Piece.given(PieceSort.NormalPiece, Side.Black), Piece.given(PieceSort.NormalPiece, Side.White));
		assertNotEquals(Piece.given(PieceSort.Queen, Side.White), Piece.given(PieceSort.NormalPiece, Side.White));
		
		assertEquals(Piece.given(PieceSort.NormalPiece, Side.White).hashCode(), Piece.given(PieceSort.NormalPiece, Side.White).hashCode());
		assertNotEquals(Piece.given(PieceSort.NormalPiece, Side.Black).hashCode(), Piece.given(PieceSort.NormalPiece, Side.White).hashCode());
		assertNotEquals(Piece.given(PieceSort.Queen, Side.White).hashCode(), Piece.given(PieceSort.NormalPiece, Side.White).hashCode());
		
		assertEquals(Piece.given(PieceSort.NormalPiece, Side.White), Piece.white());
		assertEquals(Piece.given(PieceSort.NormalPiece, Side.Black), Piece.black());
		assertEquals(Piece.given(PieceSort.Queen, Side.White), Piece.whiteQueen());
		assertEquals(Piece.given(PieceSort.Queen, Side.Black), Piece.blackQueen());
		
		assertEquals(Side.White, Piece.white().getColor());
		assertNotEquals(Side.Black, Piece.white().getColor());
		assertEquals(Side.Black, Piece.black().getColor());
		assertEquals(PieceSort.NormalPiece, Piece.black().getSort());
		assertNotEquals(PieceSort.Queen, Piece.black().getSort());
		
		assertEquals(Side.White, Piece.whiteQueen().getColor());
		assertEquals(PieceSort.Queen, Piece.whiteQueen().getSort());
		assertNotEquals(Side.Black, Piece.whiteQueen().getColor());
		assertNotEquals(PieceSort.NormalPiece, Piece.whiteQueen().getSort());
		assertEquals(Side.Black, Piece.blackQueen().getColor());
		assertEquals(PieceSort.Queen, Piece.blackQueen().getSort());
		
		assertEquals("Piece{color=Black, sort=NormalPiece}", Piece.given(PieceSort.NormalPiece, Side.Black).toString());
		assertNotEquals("Piece{color=Black, sort=NormalPiece}", Piece.blackQueen().toString());
	}
	
	@Test
	void testCheckerBoard() {
		final CheckerBoard board = CheckerBoard.newInstance();
		assertNotNull(board);
		
		for (int i = 1; i <= 50; i++) {
			if (i > 20 && i < 31) {
				assertEquals(Optional.empty(), board.getPiece(Square.given(i)));
			} else if (i > 30 && i <= 50) {
				assertEquals(Piece.white(), board.getPiece(Square.given(i)).get());
			} else {
				assertEquals(Piece.black(), board.getPiece(Square.given(i)).get());
			}
		}
		
		Map<Square, Piece> map = Maps.newHashMap();
		map.put(Square.given(1), Piece.whiteQueen());
		map.put(Square.given(7), Piece.white());
		map.put(Square.given(19), Piece.black());
		map.put(Square.given(26), Piece.white());
		map.put(Square.given(39), Piece.black());
		map.put(Square.given(48), Piece.blackQueen());
		map = ImmutableMap.copyOf(map);
		final CheckerBoard inputBoard = CheckerBoard.given(map);
		assertNotNull(inputBoard);
		
		assertEquals(Piece.whiteQueen(), inputBoard.getPiece(Square.given(1)).get());
		assertEquals(Optional.empty(), inputBoard.getPiece(Square.given(2)));
		assertNotEquals(Optional.empty(), inputBoard.getPiece(Square.given(7)));
		assertEquals(Piece.white(), inputBoard.getPiece(Square.given(7)).get());
		assertEquals(Piece.black(), inputBoard.getPiece(Square.given(19)).get());
		assertEquals(Piece.white(), inputBoard.getPiece(Square.given(26)).get());
		assertEquals(Piece.black(), inputBoard.getPiece(Square.given(39)).get());
		assertEquals(Piece.blackQueen(), inputBoard.getPiece(Square.given(48)).get());
		
		assertThrows(IllegalArgumentException.class, () -> CheckerBoard.given(null), "Expected to throw IllegalArgument exception, but it didn't");
		assertThrows(IllegalArgumentException.class, () -> board.getPiece(null), "Expected to throw IllegalArgument exception, but it didn't");
	}
	
	@Test
	void testCheckerBoardMove() {
		final CheckerBoard board = CheckerBoard.newInstance();
		assertNotNull(board);
		
		assertThrows(IllegalArgumentException.class, () -> board.move(null, Square.given(23)), "Expected to throw IllegalArgumentException exception, but it didn't");
		assertThrows(IllegalArgumentException.class, () -> board.move(Square.given(34), null), "Expected to throw IllegalArgumentException exception, but it didn't");
		assertThrows(IllegalArgumentException.class, () -> board.move(null, null), "Expected to throw IllegalArgumentException exception, but it didn't");
		assertThrows(IllegalStateException.class, () -> board.move(Square.given(1), Square.given(5)), "Expected to throw IllegalStateException exception, but it didn't");
		assertThrows(IllegalStateException.class, () -> board.move(Square.given(21), Square.given(26)), "Expected to throw IllegalStateException exception, but it didn't");
		assertThrows(IllegalStateException.class, () -> board.move(Square.given(27), Square.given(32)), "Expected to throw IllegalStateException exception, but it didn't");
		assertThrows(IllegalStateException.class, () -> board.move(Square.given(1), Square.given(7)), "Expected to throw IllegalStateException exception, but it didn't");
		
		Square s1 = Square.given(5);
		Square s2 = Square.given(5);
		assertThrows(IllegalArgumentException.class, () -> board.move(s1, s1), "Expected to throw IllegalArgumentException exception, but it didn't");
		assertThrows(IllegalArgumentException.class, () -> board.move(s1, s2), "Expected to throw IllegalArgumentException exception, but it didn't");
		
		final CheckerBoard blackMoveResult = board.move(Square.given(16), Square.given(21));
		assertNotNull(blackMoveResult);
		assertNotEquals(board, blackMoveResult);
		
		final CheckerBoard whiteMoveResult = board.move(Square.given(31), Square.given(27));
		assertNotNull(whiteMoveResult);
		assertNotEquals(whiteMoveResult, blackMoveResult);
		assertNotEquals(board, whiteMoveResult);
	}
}
