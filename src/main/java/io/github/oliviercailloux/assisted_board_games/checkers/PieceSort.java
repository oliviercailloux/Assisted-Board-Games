package io.github.oliviercailloux.assisted_board_games.checkers;

public enum PieceSort {
	
	NormalPiece,
	Queen;
	
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
