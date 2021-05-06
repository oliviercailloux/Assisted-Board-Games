package io.github.oliviercailloux.assisted_board_games.checkers;

/**
 * <p>
 * In this class we represent all the features of a square.
 * <p>
 * It is define by its two coordinates : vertical, representing the y axis and
 * horizontal, representing the x axis.
 * <p>
 * We can also find in this class methods that allow us to know if a square is
 * white or black or if the square is empty or not.
 * 
 * @author Dahuiss and Marina and Yasmine
 */

public class Square {

	private final int row;
	private final int column;

	Square(int x, int y) {
		this.row = x;
		this.column = y;
	}

	/**
	 * <p>
	 * A Board is a set of Squares. The Board numbers its Squares starting in
	 * upper-left: (1,1) (2,1) ... (x,y) (1,2) ... ... (x,y)
	 * 
	 * @param row    the vertical coordonate
	 * @param column the horizontal coordonate
	 * @return Create the checkers squares using its coordinates.
	 */

	public static Square givenCoordonate(int row, int column) {
		return new Square(row, column);
	}

	/**
	 * This method uses the parity property to show us if the square is white or
	 * not.
	 * 
	 * @return True is the sum of vertical and horizontal are even and false if the
	 *         sum is odd.
	 */
	public final boolean isWhite() {
		return (this.getRow() + this.getColumn()) % 2 == 1;
	}

	/**
	 * @return the row the vertical coordonate
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the column the horizontal coordonate
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * This enum allow us to know the state of the square
	 */
	public static enum SquareState {
		NOT_VALID_COORDINATES, NOT_IN_PLAY, IN_PLAY;

		public boolean equalsType(SquareState other) {
			return equals(other);
		}
	}

}
