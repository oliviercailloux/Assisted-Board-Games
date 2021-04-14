package io.github.oliviercailloux.assisted_board_games.checkers;

/**
 * @author Dahuiss & Marina
 *
 */
public class Piece {
	
	private Square square;
	private String typePiece;
	private boolean selected;

	/**
	 * @return typePiece states if it's a ordinary piece or draught 
	 */
	public String getTypePiece() {
		return typePiece;
	}

	public void setTypePiece(String typePiece) {
		this.typePiece = typePiece;
	}

	public String toString() {
		if (this.selected) {
			return "@";
		} else {
			if (this.typePiece.equals("piece")) {
				if (this.square.isWhite())
					return "white";
				else
					return "black";
			} else {
				if (this.typePiece.equals("draught")) { 
					if (this.square.isWhite())
						return "White";
					else
						return "Black";
				} else
					return "Other";
			}
		}
	}

}
