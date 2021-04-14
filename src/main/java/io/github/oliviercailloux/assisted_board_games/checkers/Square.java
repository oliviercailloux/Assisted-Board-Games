package io.github.oliviercailloux.assisted_board_games.checkers;

/**
 * @author Dahuiss and Marina
 *
 */

public class Square {
	/**
	 *
	 * @param vertical represents the vertical coordinate of Checkers Square.
	 * @param horizontal  represents the horizontal coordinate of  Checkers Square.
	 * @param color state if the Square is black or white.
	 */
	private int vertical;
	private int horizontal;
	private Piece piece;
	private boolean color;

	
	public Square(int vertical, int horizontal) {
		
		this.vertical = vertical;
		this.horizontal = horizontal;
		this.color = false;

	}
	
	public int getVertical() {
		return vertical;
	}

	public int getHorizontal() {
		return horizontal;
	}

	public void setWhite() {
		this.color = true;
	}

	public boolean isWhite() {

		return this.color;

	}

	public boolean isBlack() {
		return !this.color;
	}

	public boolean hasPiece() {
		try {

			piece.getTypePiece();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String toString() {
		
		try {

			this.getHorizontal();
			return this.piece.toString();
		} catch (Exception e) {
			if (this.isWhite())
				return "░";
			else
				return " ";
		}
	}

}
