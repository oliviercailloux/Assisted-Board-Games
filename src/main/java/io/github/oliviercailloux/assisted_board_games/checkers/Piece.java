package io.github.oliviercailloux.assisted_board_games.checkers;

/**
 * @author Dahuiss & Marina
 *
 */
public class Piece {
	private final String color = null;
	private final boolean promoted = false;

	public String color() {
		return color;
	}

	public boolean isPromoted() {
		return promoted;
	}
}
