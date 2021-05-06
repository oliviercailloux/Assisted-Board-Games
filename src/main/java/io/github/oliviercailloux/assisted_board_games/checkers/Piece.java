package io.github.oliviercailloux.assisted_board_games.checkers;

/**
 * This class has 2 features which are color and promoted.
 * <p>
 * Indeed, color permits us to know the color of the Piece. We add the "final"
 * type because the color of the Piece don't has to be changed.
 * <p>
 * promoted attribute permits us to know if the piece becomes a Queen or not.
 * That's why we choosed the boolean parameter to express it.
 * <p>
 * The color() method returns a String representing the color of the piece and
 * the isPromoted() method returns the promoted attributes that we explained
 * before.
 * 
 */
public class Piece {

	private final Square point;
	private final Side color;

	Piece(Square point, Side couleur) {
		this.color = couleur;
		this.point = point;
	}

	/**
	 * @param p       : an instance of Square which is considered as a point
	 * @param couleur : represent a color of the side
	 * @return create a piece by using a square and his color
	 */
	public final Piece givenPoint(Square p, Side couleur) {
		return new Piece(p, couleur);
	}

	/**
	 * @return an instance of a square which is considered as a point
	 */
	public Square getSquare() {
		return point;
	}

	/**
	 * @return the color of the side.
	 */
	public Side getColor() {
		return color;
	}

}
