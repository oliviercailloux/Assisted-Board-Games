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
 * @author Dahuiss and Marina
 */

public class Square {

	private final Point point;

	Square(Point point) {
		this.point = point;
	}

	/**
	 * A Board is a set of Squares. The Board numbers its Squares starting in
	 * upper-left: (1,1) (2,1) ... (x,y) (1,2) ... ... (x,y)
	 * <p>
	 * This method, consist on displaying the checkers board using its coordinates.
	 */

	public static final Square givenCoordonate(Point point) {
		return new Square(point);
	}

	/**
	 * @return the point
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * This method uses the parity property to show us if the square is white or
	 * not.
	 * 
	 * @return True is the sum of vertical and horizontal are even and false if the
	 *         sum is odd.
	 */
	public final boolean isWhite() {
		return (this.point.getRow() + this.point.getColumn()) % 2 == 1;
	}

}
