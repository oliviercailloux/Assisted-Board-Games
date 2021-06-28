package io.github.oliviercailloux.assisted_board_games.checkers;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.hash.HashCode;
import com.google.common.base.MoreObjects;
import java.util.Objects;

import io.github.oliviercailloux.assisted_board_games.two_players.Side;

public class Piece {
	private final PieceSort sort;
	private final Side color;

	private Piece(PieceSort sort, Side color) {
		checkArgument(sort != null);
		checkArgument(color != null);

		this.sort = sort;
		this.color = color;
	}

	/**
	 * @param pieceSort : represents sort of piece
	 * @param color : represent a color of the side
	 * @return creates a piece by using given square and color
	 */
	public static Piece given(PieceSort pieceSort, Side color) {
		return new Piece(pieceSort, color);
	}

	/**
	 * @return new instance of a White piece
	 */
	public static Piece white() {
		return new Piece(PieceSort.NORMAL_PIECE, Side.WHITE);
	}

	/**
	 * @return new instance of a Black piece
	 */
	public static Piece black() {
		return new Piece(PieceSort.NORMAL_PIECE, Side.BLACK);
	}

	/**
	 * @return new instance of a White Queen piece
	 */
	public static Piece whiteQueen() {
		return new Piece(PieceSort.QUEEN, Side.WHITE);
	}

	/**
	 * @return new instance of a Black Queen piece
	 */
	public static Piece blackQueen() {
		return new Piece(PieceSort.QUEEN, Side.BLACK);
	}

	/**
	 * @return piece's color
	 */
	public Side getColor() {
		return color;
	}

	/**
	 * @return piece's sort
	 */
	public PieceSort getSort() {
		return sort;
	}

	/**
	 * @returns true iff the given object is also an instance of a Piece and has the same color and sort.
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Piece)) {
			return false;
		}

		final Piece other = (Piece) obj;
		return sort.equals(other.getSort()) && color.equals(other.getColor());
	}

	@Override
	public int hashCode() {
		return Objects.hash(sort, color);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("color", color).add("sort", sort).toString();
	}
}
