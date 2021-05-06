package io.github.oliviercailloux.assisted_board_games.checkers;

public enum Side {

	Black, White;

	public boolean equalsSide(Side other) {
		return equals(other);
	}

	public static Side fromValue(String v) {
		return valueOf(v);
	}

	public String value() {
		return name();
	}

}
