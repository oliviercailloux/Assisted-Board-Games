package io.github.oliviercailloux.assisted_board_games.checkers;

public enum PieceSort {

	/**
	 * Change the type of a piece when it becomes a "Queen", for example if the
	 * piece is represented by an empty round, when it becomes a "Queen" this piece
	 * will be represented by a full round.
	 */

	NormalPiece, Queen;

	public boolean equalsType(PieceSort other) {
		return equals(other);
	}

	public static PieceSort fromValue(String v) {
		return valueOf(v);
	}

	public String value() {
		return name();
	}

}
