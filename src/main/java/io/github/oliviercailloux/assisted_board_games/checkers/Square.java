package io.github.oliviercailloux.assisted_board_games.checkers;

/**
 * <p> 
 * In this class we represent all the features of a square.
 * <p> 
 * It is define by its two coordinates : vertical, representing the y axis 
 * and horizontal, representing the x axis. 
 * <p> 
 * We can also find in this class methods that allow us to know if a square is white or black 
 * or if the square is empty or not. 
 * 
 * @author Dahuiss and Marina
 */

public class Square {
	
	private final int vertical;
	private final int horizontal;
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
	
	/**
	 * This method uses the parity property to show us if the square is white or not.
	 * @return True is the sum of vertical and horizontal are even and false if the sum is odd.
	 */
	public final boolean isWhite() {
		int som = vertical + horizontal;
		if (som % 2 == 1) {
			return false;//Black color
		}
		return true;//White color
	}

	/**
	 * This method permits us to know if the square is empty just by testing if piece is null or not. 
	 * @return True if piece is not null and false else.
	 */
	public final boolean isEmpty() {
		return (this.myPiece == null);
	}
}
