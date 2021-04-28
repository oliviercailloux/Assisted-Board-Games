package io.github.oliviercailloux.assisted_board_games.checkers;

public enum Color {
	
	Black, 
	White;
	
	public boolean equalsSide (Color other) {
		return equals(other);
	}
	
	public static Color fromValue(String v) {
		return valueOf(v);
	}
	
	public String value( ) {
		return name();
	}
}
