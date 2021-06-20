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
	
	private CheckerBoard() {
		LOGGER.info("Checkerboard constructor invocation");
		this.board = Maps.newHashMap();
		
		for (int i = 1; i <= 50; i++) {
			if (i > 0 && i < 21) {
				board.put(Square.given(i), Piece.black());
			} else if (i > 30 && i <= 50) {
				board.put(Square.given(i), Piece.white());
			}
		}
		
		LOGGER.info("Default checkerboard representation:\n" + this.toString());
		
		this.board = ImmutableMap.copyOf(board);
	}
	
	private CheckerBoard(Map<Square, Piece> board) {
		LOGGER.info("Checkerboard constructor invocation using inputBoard");
		checkArgument(board != null);
		this.board = ImmutableMap.copyOf(board);
	}
	
	/**
	 * Returns a default representation of a Checkerboard with pieces set in their
	 * default position at the start of the game
	 * @return new instance of Checkerboard
	 */
	public static CheckerBoard newInstance() {
		return new CheckerBoard();
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
		checkArgument(square != null);
		return Optional.ofNullable(board.get(square));
	}
	
	/**
	 * Permits to move a piece from one square to another
	 * @param from - square which contains the piece
	 * @param to - square to which the piece should be moved
	 * @return a new instance of CheckerBoard with made move
	 */
	public CheckerBoard move(Square from, Square to) {
		// TODO: Implement logic to decide if the move 'from' square to 'to' square is legal and can be done, if not don't let it happen
		
		LOGGER.info("Moving a piece from: " + from + " to: " + to);
		checkArgument(from != null && to != null);
		checkArgument(!from.equals(to));
		
		if (board.get(from) == null)
			throw new IllegalStateException("There is no piece on square: " + from);
		
		if (board.get(to) != null)
			throw new IllegalStateException("Square: " + to + " is occupied");
		
		Piece piece = board.get(from);
		
		final Map<Square, Piece> newBoard = Maps.newHashMap(board);
		newBoard.remove(from);
		newBoard.put(to, piece);
		
		return new CheckerBoard(ImmutableMap.copyOf(newBoard));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		board.forEach((key, value) -> sb.append(key + ": " + value + "\n"));
		return sb.toString();
	}
}
