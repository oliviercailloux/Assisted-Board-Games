package io.github.oliviercailloux.assisted_board_games.checkers;

/**
 * @author Dahuiss and Marina
 *
 */

public class Square {
	
	private final int vertical;
	private final int horizontal;
	private boolean white;
	private Piece myPiece;
	Square(int verticale, int horizontal){
		this.horizontal = horizontal;
		this.vertical = verticale;
	}
	public static final Square givenCoordonate(int vertical, int horizontal) {
		return new Square(vertical, horizontal);
	}
	/**
	 * @return the vertical
	 */
	public int getVertical() {
		return vertical;
	}
	/**
	 * @return the horizontal
	 */
	public int getHorizontal() {
		return horizontal;
	}
	
	public boolean isWhite() {
		int som = vertical + horizontal;
		if (som % 2 == 1) {
			return false;//Black color
		}
		return true;//White color
	}
	public void setWhite() {
		this.white= true;
	}
	public boolean getWhite() {
		return this.white;
	}
	public boolean isEmpty() {
		return (this.myPiece == null);
	}
}
