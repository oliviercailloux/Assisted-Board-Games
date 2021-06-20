package io.github.oliviercailloux.assisted_board_games.checkers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.checkerframework.checker.nullness.Opt;
import org.hibernate.annotations.Check;
import org.jboss.weld.util.collections.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.bhlangonijr.chesslib.BoardEvent;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import io.github.oliviercailloux.assisted_board_games.two_players.Side;

/**
 * Permits to create checkerboard object.
 * <p>
 * The Board is a composed of 100 squares (10 x 10 size checkerboard) and black or white pieces.
 * The Board squares are represented by a number (1, 2, 3, ... 50)
 */

public class CheckerBoard {
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckerBoard.class);
	
	private Map<Square, Piece> board;
	
	private CheckerBoard(Map<Square, Piece> board) {
		LOGGER.info("Checkerboard constructor invocation using an input board");
		checkNotNull(board);
		this.board = ImmutableMap.copyOf(board);
		LOGGER.info("Checkerboard representation: {}", this.toString());
	}
	
	
	/**
	 * Returns a default representation of a Checkerboard with pieces set in their
	 * default position at the start of the game
	 * @return new instance of Checkerboard
	 */
	public static CheckerBoard newInstance() {
		Map<Square, Piece> board = Maps.newLinkedHashMap();
		
		for (int i = 1; i <= 50; i++) {
			if (i > 0 && i < 21) {
				board.put(Square.given(i), Piece.black());
			} else if (i > 30 && i <= 50) {
				board.put(Square.given(i), Piece.white());
			}
		}
		
		return new CheckerBoard(ImmutableMap.copyOf(board));
	}
	
	
	/**
	 * Returns a representation of a Checkerboard with provided pieces
	 * and their position on a Checkerboard
	 * @return new instance of Checkerboard
	 */
	public static CheckerBoard given(Map<Square, Piece> inputBoard) {
		return new CheckerBoard(inputBoard);
	}
	
	
	/**
	 * @param square : associate position of the piece on a current board.
	 * @return The piece at the given position.
	 */
	public Optional<Piece> getPiece(Square square) {
		LOGGER.info("Get associated piece for square {}", square);
		checkNotNull(square);
		return Optional.ofNullable(board.get(square));
	}
	
	
	/**
	 * Permits to move a piece from one square to another
	 * @param from - square which contains the piece
	 * @param to - square to which the piece should be moved
	 * @return a new instance of CheckerBoard with made move
	 */
	public CheckerBoard move(Square from, Square to) {
		LOGGER.info("Moving a piece from: {} to: {}", from, to);
		checkNotNull(from);
		checkNotNull(to);
		checkArgument(!from.equals(to));
		
		checkState(board.get(from) != null);
		checkState(board.get(to) == null);
		
		Piece piece = board.get(from);
		
		final Map<Square, Piece> newBoard = Maps.newLinkedHashMap(board);
		newBoard.remove(from);
		newBoard.put(to, piece);
		
		return new CheckerBoard(newBoard);
	}
	
	@Override
	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		board.forEach((square, piece) -> sb.append(MoreObjects.toStringHelper(this).add("Square", square).add("Piece", piece).toString()));
//		return sb.toString();
		
		return MoreObjects.toStringHelper(this).add("Board", board).toString();
	}
}
