package io.github.oliviercailloux.assisted_board_games.checkers;

/**
 * This class has 2 features which are color and promoted.
 * <p>
 * Indeed, color permits us to know the color of the Piece. We add the "final" type because
 * the color of the Piece don't has to be changed.
 * <p>
 * promoted attribute permits us to know if the piece becomes a Queen or not. That's why we choosed the boolean parameter to express it.
 * <p> 
 * The color() method returns a String representing the color of the piece and the isPromoted() method returns the promoted attributes that we explained before.
 * 
 * @author Dahuiss & Marina
 */
public class Piece {
	
	private final String color = null;
	private boolean promoted = false;
	/**
	 * @return color 
	 */
	public String color() {
		return color;
	}
	/**
	 * @return promoted
	 */
	public boolean isPromoted() {
		return promoted;
	}
}
