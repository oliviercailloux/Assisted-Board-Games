package io.github.oliviercailloux.assisted_board_games.checkers;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Permits to create a checker board.
 * <p>
 * This interface show how we will implement our checker board.
 */

public interface CheckerBoard {
	/**
	 * This class requires to have as attribute 2 Map, the first which contains, the
	 * square coordinates and a second one which contains the piece's coordinates
	 */
	Map<Point, Square> pointToSquare = new HashMap<>();
	Map<Point, Piece> pointToPiece = new HashMap<>();

	/**
	 * A Board is a set of Squares. The Board numbers its Squares starting in
	 * upper-left: (1,1) (2,1) ... (x,y) (1,2) ... ... (x,y)
	 * <p>
	 * This method, consist on displaying the checkers board using its coordinates.
	 */

	public void printBoard();

	/**
	 * Change the type of a piece when it becomes a "Queen", for example if the
	 * piece is represented by an empty round, when it becomes a "Queen" this piece
	 * will be represented by a full round.
	 *
	 * @param piece : it's the piece which will become "Queen".
	 */

	public void isQueen(Piece piece);

	/**
	 * This method create all the points top down and left right
	 * 
	 * @return points top row first, for example : (1,1), (2,1), (3,1) ... (sizeX,
	 *         1) (1,2) (2,2) (3,2) (sizeX, 2) .... (sizeX,sizeY)
	 */

	public List<Point> generatePoints();

	/**
	 * @param point : is the coordinate of a square.
	 * @return returns the square's coordinates if the point is in the map which
	 *         contains the coordinates of squares.
	 */
	public Square getSquare(Point point);

	/**
	 * @param point : is the coordinate of a piece.
	 * @return returns the piece's coordinates if the point is in the map which
	 *         contains piece's coordinates
	 */

	public Piece getPiece(Point point);

	/**
	 * The objective of this method is to put the piece in the map representing
	 * pieces with his coordinates.
	 *
	 * @param point : represents the coordinates of the piece.
	 * @param piece : is the piece to add.
	 */
	void putPointToPiece(Point point, Piece piece);

	/**
	 * The objective of this method is to delete a piece, do not display it anymore,
	 * if and only if it is eaten by another one.
	 *
	 * @param point : represents the coordinates of the piece which is eaten.
	 */
	public void removePointToPiece(Point point);

	/**
	 * The objective of this method is to put the square in the map representing
	 * squares with his coordinates.
	 *
	 * @param point  : represents the coordinates of the piece.
	 * @param square : is the square to add.
	 */
	void putPointToSquare(Point point, Square square);

	/**
	 * Move a piece from one position to another.
	 * 
	 * @param from : the initial position
	 * @param to   : arrival position.
	 */
	void movePiece(Point from, Point to);

	/**
	 * Show on the board if the movement is possible or not?
	 * 
	 * @param from : the initial position
	 * @param to   : arrival position.
	 * @return true is it's a valid move and false else.
	 */
	public boolean isValidToMove(Point from, Point to);

	/**
	 * LoadPieces is the method which load all the pieces in the CheckerBoard.
	 * 
	 * @param pieces : a list of all the checker pieces.
	 */
	void loadPieces(List<Piece> pieces);

}
