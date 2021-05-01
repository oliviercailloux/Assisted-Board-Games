package io.github.oliviercailloux.assisted_board_games.checkers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Permits to create a checker board.
 * <p>
 * This class shows our point of view of a checker board.
 * <p>
 * This class requires to have as attribute 2 Map, the first which contains, the
 * square coordinates and a second one which contains the piece's coordinates
 * 
 *  @author Yasmine & Dahuiss
 */


public class CheckerBoard {
	
	private String color;
	
	private final int sizeX;
	
	private final int sizeY;
	
	private Map<Point, Square> pointToSquare;
	
	private Map<Point, Piece> pointToPiece;
	
	public CheckerBoard(int x, int y) {
		
		this.sizeX = x;
		
		this.sizeY = y;
		
		this.pointToSquare = new HashMap<>();
		
		this.pointToPiece = new HashMap<>();
		
	}
	
	public static final CheckerBoard givenSize(int x, int y){
		
		return new CheckerBoard(x, y);
		
	}
	
	/**
	 * This method create all the points top down and left right
	 * 
	 * @return points top row first, for example : (1,1), (2,1), (3,1) ... (sizeX,
	 *         1) (1,2) (2,2) (3,2) (sizeX, 2) .... (sizeX,sizeY)
	 */

	public List<Point> generatePoints() {
		List<Point> pointsBoard = new ArrayList<>();
		for (int y = 1; y<= getSizeY(); y++) {
			for(int x = 1; x<= getSizeX(); x++) {
				pointsBoard.add(Point.given(x, y));
			}
		}
		return pointsBoard;
	}
	
	/**
	 * This method can allows us to know principally the State of a Square
	 * 
	 */ 
	
	public static enum Square {
		
		NOT_VALID_COORDINATES, NOT_IN_PLAY, IN_PLAY;
		
		public boolean equalsType(Square other) {
			return equals(other);
		}
		
	}
	
	/**
	 * @param point : is the coordinate of a square.
	 * @return returns the square's coordinates if the point is in the map which
	 *         contains the coordinates of squares.
	 */
	
	public Square getSquare(Point point) {
		
		if (pointToSquare.containsKey(point)) {
			return pointToSquare.get(point);
		}
		return Square.NOT_VALID_COORDINATES;
	}

	/** This method is usefull to check the point's validity**/
	
	public void checkPoint (Point point) {
		Square square = getSquare(point);
		if (square.equalsType(Square.NOT_VALID_COORDINATES)) {
			throw new RuntimeException("Invalid coordinates"+point);
		}
	}
	
	public void place(Piece piece,Point point) {
		checkPoint(point);
		putPointToPiece(point, piece);
	}
	
	/**
	 * @param point : is the coordinate of a piece.
	 * @return returns the piece's coordinates if the point is in the map which
	 *         contains piece's coordinates
	 */
	
	Piece getPiece(Point point) {
		checkPoint(point);
		Piece piece = pointToPiece.get(point);
		return piece.givenPoint(point, Color.fromValue(color));
	}

	/**
	 * The objective of this method is to put the piece in the map representing
	 * pieces with its coordinates.
	 *<p>
	 * It will permit us to know where the piece is located. ( Key = Piece, Value = its coordinates a Point).
	 * @param point : represents the coordinates of the piece.
	 * @param piece : is the piece to add.
	 */
	
	void putPointToPiece(Point point, Piece piece) {
		this.pointToPiece.put(point, piece);
	}

	/**
	 * The objective of this method is to delete a piece, do not display it anymore,
	 * if and only if it is eaten by another one.
	 *
	 * @param point : represents the coordinates of the piece which is eaten.
	 */
	
	void removePointToPiece(Point point) {
		this.pointToPiece.remove(point);
	}

	/**
	 * The objective of this method is to put the square in the map representing
	 * squares with its coordinates.
	 *<p>
	 * It will permit us to know where the square is located. ( Key = Square, Value = its coordinates a Point).
	 * @param point  : represents the coordinates of the piece.
	 * @param square : is the square to add.
	 */
	
	public void putPointToSquare(Point point, Square square) {
	
		this.pointToSquare.put(point, square);
	}
	
	public boolean isAvailableTargetForMove(Point point) {
		
		final boolean move;
		
		if (Square.IN_PLAY.equals(getSquare(point))) {
			if (null == getPiece(point)) {
				move = true;
			}else move = false;
		}else move = false;
		
		System.out.println("isAvalaible(" + ") move=" + move); // Mettre une exception.
		
		return move;
		
	}
	/**
	 * Move a piece from one position to another.
	 * 
	 * @param from : the initial position
	 * @param to   : arrival position.
	 */
	
	public void movePiece(Point from, Point to) {
		
		final Piece piece = getPiece(from);
		
		if (piece != null) {
			
			if (isAvailableTargetForMove(to)) {
				
				this.removePointToPiece(from);
				
				this.putPointToPiece(to, piece);
				
			}
		}
	}

	/**
	 * Show on the board if the movement is possible or not.
	 * 
	 * @param from : the initial position
	 * @param to   : arrival position.
	 * @return true if it's a valid move and false else.
	 */
	
	public boolean isValidToMove(Point from, Point to) {
		if (getPiece(from) != null) {
			if (isAvailableTargetForMove(to)) {
				return true;
			}
		}return false;
	}

	/**
	 * LoadPieces is the method which loads all the pieces in the CheckerBoard.
	 * 
	 * @param pieces : a list of all the checker pieces.
	 */
	
	public void loadPieces(List<Piece> pieces) {
		
		List<Piece> listPiece = new ArrayList<>(pieces);
		
		for (Point point : generatePoints()) {
			
			Square val = getSquare(point);
			
			if (Square.IN_PLAY.equals(val)) {
				
				Piece piece = listPiece.remove(0);
				
				this.place(piece, point);
			}
		}
		if (listPiece.size() != 0) {
			throw new RuntimeException("The size of the piece must, size =" + listPiece.size());
		}
	}

	/**
	 * @return the value of x.
	 */
	public int getSizeX() {
		return sizeX;
	}
	
	/**
	 * @return the value of y.
	 */
	public int getSizeY() {
		return sizeY;
	}
	
}
