package io.github.oliviercailloux.assisted_board_games.checkers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.AssertTrue;

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
		assertEquals(50, Square.given(50).getSquareNumber(), "Actual square number doesn't match expected one");
		
		assertEquals(Square.given(38), Square.given(38));
		assertEquals(Square.given(38).hashCode(), Square.given(38).hashCode());
		
		assertNotEquals(Square.given(43), Square.given(23));
		assertNotEquals(Square.given(43).hashCode(), Square.given(23).hashCode());
	}
	
	@Test
	void testPiece() {
		assertThrows(IllegalArgumentException.class, () -> Piece.given(null, null), "Expected to throw IllegalArgument exception, but it didn't");
		assertThrows(IllegalArgumentException.class, () -> Piece.given(PieceSort.NORMAL_PIECE, null), "Expected to throw IllegalArgument exception, but it didn't");
		assertThrows(IllegalArgumentException.class, () -> Piece.given(null, Side.WHITE), "Expected to throw IllegalArgument exception, but it didn't");
		
		assertEquals(Piece.given(PieceSort.NORMAL_PIECE, Side.WHITE), Piece.given(PieceSort.NORMAL_PIECE, Side.WHITE));
		assertNotEquals(Piece.given(PieceSort.NORMAL_PIECE, Side.BLACK), Piece.given(PieceSort.NORMAL_PIECE, Side.WHITE));
		assertNotEquals(Piece.given(PieceSort.QUEEN, Side.WHITE), Piece.given(PieceSort.NORMAL_PIECE, Side.WHITE));
		
		assertEquals(Piece.given(PieceSort.NORMAL_PIECE, Side.WHITE).hashCode(), Piece.given(PieceSort.NORMAL_PIECE, Side.WHITE).hashCode());
		assertNotEquals(Piece.given(PieceSort.NORMAL_PIECE, Side.BLACK).hashCode(), Piece.given(PieceSort.NORMAL_PIECE, Side.WHITE).hashCode());
		assertNotEquals(Piece.given(PieceSort.QUEEN, Side.WHITE).hashCode(), Piece.given(PieceSort.NORMAL_PIECE, Side.WHITE).hashCode());
		
		assertEquals(Piece.given(PieceSort.NORMAL_PIECE, Side.WHITE), Piece.white());
		assertEquals(Piece.given(PieceSort.NORMAL_PIECE, Side.BLACK), Piece.black());
		assertEquals(Piece.given(PieceSort.QUEEN, Side.WHITE), Piece.whiteQueen());
		assertEquals(Piece.given(PieceSort.QUEEN, Side.BLACK), Piece.blackQueen());
		
		assertEquals(Side.WHITE, Piece.white().getColor());
		assertNotEquals(Side.BLACK, Piece.white().getColor());
		assertEquals(Side.BLACK, Piece.black().getColor());
		assertEquals(PieceSort.NORMAL_PIECE, Piece.black().getSort());
		assertNotEquals(PieceSort.QUEEN, Piece.black().getSort());
		
		assertEquals(Side.WHITE, Piece.whiteQueen().getColor());
		assertEquals(PieceSort.QUEEN, Piece.whiteQueen().getSort());
		assertNotEquals(Side.BLACK, Piece.whiteQueen().getColor());
		assertNotEquals(PieceSort.NORMAL_PIECE, Piece.whiteQueen().getSort());
		
		assertEquals("Piece{color=BLACK, sort=NORMAL_PIECE}", Piece.given(PieceSort.NORMAL_PIECE, Side.BLACK).toString());
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
		
		Map<Square, Piece> map = Maps.newLinkedHashMap();
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
		
		assertThrows(NullPointerException.class, () -> CheckerBoard.given(null), "Expected to throw NullPointerException exception, but it didn't");
		assertThrows(NullPointerException.class, () -> board.getPiece(null), "Expected to throw NullPointerException exception, but it didn't");
	}
	
	@Test
	void testCheckerBoardMove() {
		final CheckerBoard board = CheckerBoard.newInstance();
		assertNotNull(board);
		
		assertThrows(NullPointerException.class, () -> board.move(null, Square.given(23)), "Expected to throw NullPointerException exception, but it didn't");
		assertThrows(NullPointerException.class, () -> board.move(Square.given(34), null), "Expected to throw NullPointerException exception, but it didn't");
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
		assertNotEquals(whiteMoveResult, blackMoveResult);
		
		final Map<Square, Piece> inputMap = Map.of(Square.given(1), Piece.black(), Square.given(2), Piece.black(), 
				Square.given(3), Piece.black(), Square.given(33), Piece.white(), Square.given(34), Piece.white());
		int inputMapSize = inputMap.size();
		
		final CheckerBoard mover = CheckerBoard.given(inputMap);
		int piecesCount = 0;
		int whitePiecesCount = 0;
		int blackPiecesCount = 0;
		
		for (int i = 1; i < 51; i++) {
			final Optional<Piece> opt = mover.getPiece(Square.given(i));
			
			if (opt.isPresent()) {
				piecesCount++;
				
				if (opt.get().getColor() == Side.BLACK)
					blackPiecesCount++;
				else
					whitePiecesCount++;
			}
		}
		
		assertEquals(inputMapSize, piecesCount);
		
		Square from = Square.given(1);
		Square to = Square.given(7);
		Piece originalPiece = mover.getPiece(from).get();
		CheckerBoard moveResult = mover.move(from, to);
		
		assertTrue(moveResult.getPiece(from).isEmpty());
		assertTrue(moveResult.getPiece(to).isPresent());
		assertEquals(originalPiece, moveResult.getPiece(to).get());
		
		from = Square.given(34);
		to = Square.given(29);
		originalPiece = mover.getPiece(from).get();
		moveResult = mover.move(from, to);
		
		assertTrue(moveResult.getPiece(from).isEmpty());
		assertTrue(moveResult.getPiece(to).isPresent());
		assertEquals(originalPiece, moveResult.getPiece(to).get());
		
		piecesCount = 0;
		int newWhitePiecesCount = 0;
		int newBlackPiecesCount = 0;
		
		for (int i = 1; i < 51; i++) {
			final Optional<Piece> opt = mover.getPiece(Square.given(i));
			
			if (opt.isPresent()) {
				piecesCount++;
				
				if (opt.get().getColor() == Side.BLACK)
					newBlackPiecesCount++;
				else
					newWhitePiecesCount++;
			}
		}
		
		assertEquals(inputMapSize, piecesCount);
		assertEquals(whitePiecesCount, newWhitePiecesCount);
		assertEquals(blackPiecesCount, newBlackPiecesCount);
	}
}
