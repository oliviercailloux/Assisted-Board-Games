package io.github.oliviercailloux.assisted_board_games.checkers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Permits to create a checker board.
 * <p>
 * This class shows our point of view of a checker board. A Board is a set of
 * Squares. The Board numbers its Squares starting in upper-left: (1,1) (2,1)
 * ... (x,y) (1,2) ... ... (x,y)
 * 
 * @author Yasmine & Dahuiss
 */

public class CheckerBoard {

	private String color;

	private final int sizeX;

	private final int sizeY;

	private final Map<Square, Square> pointToSquare; // We consider the key of the map as a point so it is also a Square

	private final Map<Square, Piece> pointToPiece;

	public CheckerBoard(int x, int y) {

		this.sizeX = x;

		this.sizeY = y;

		this.pointToSquare = new HashMap<>();

		this.pointToPiece = new HashMap<>();

	}

	public static final CheckerBoard givenSize(int x, int y) {

		return new CheckerBoard(x, y);

	}

	/**
	 * This method create all the points top down and left right
	 * 
	 * @return points top row first, for example : (1,1), (2,1), (3,1) ... (sizeX,
	 *         1) (1,2) (2,2) (3,2) (sizeX, 2) .... (sizeX,sizeY)
	 */

	public List<Square> generateSquares() {

		final List<Square> pointsBoard = new ArrayList<>();

		for (int y = 1; y <= getSizeY(); y++) {
			for (int x = 1; x <= getSizeX(); x++) {
				pointsBoard.add(Square.givenCoordonate(y, x));
			}
		}

		return pointsBoard;
	}

	/**
	 * @param point : an instance of a square which is considered as a point.
	 * @return The square's coordinates if the point is in the map which contains
	 *         the coordinates of squares.
	 */

	public Square getSquare(Square point) {

		if (pointToSquare.containsKey(point)) {
			return pointToSquare.get(point);
		}
		System.out.println(Square.SquareState.NOT_VALID_COORDINATES);
		return null;
	}

	/**
	 * @param point : an instance of a square which is considered as a point.
	 *              <p>
	 *              Is usefull to check the validity of the coordonate of a square.
	 */
	public void checkPoint(Square point) {
		final Square square = getSquare(point);
		if (square.equals(Square.SquareState.NOT_VALID_COORDINATES)) {
			throw new RuntimeException("Invalid square number" + point);
		}

	}

	/**
	 * @param point : an instance of a square which is considered as a point.
	 * @param piece : an instance of a piece.
	 *              <p>
	 *              This method is used to put a piece at a given position which is
	 *              here a point.
	 */
	public void place(Piece piece, Square point) {
		checkPoint(point);
		putPointToPiece(point, piece);
	}

	/**
	 * @param point : is the position of the piece.
	 * @return The piece at the given position.
	 */

	Piece getPiece(Square point) {
		checkPoint(point);
		final Piece piece = pointToPiece.get(point);
		return piece.givenPoint(point, Side.fromValue(color));
	}

	/**
	 * The objective of this method is to put the piece in the map representing
	 * pieces with its coordinates.
	 * <p>
	 * It will permit us to know where the piece is located. ( Key = Square, Value =
	 * its coordinates a Piece).
	 * 
	 * @param point : represents the coordonates of the piece.
	 * @param piece : is the piece to add.
	 */

	void putPointToPiece(Square point, Piece piece) {
		this.pointToPiece.put(point, piece);
	}

	/**
	 * The objective of this method is to delete a piece, do not have it anymore in
	 * the board, if and only if it is eaten by another one.
	 *
	 * @param point : represents the coordinates of the piece which is eaten.
	 */

	void removePointToPiece(Square point) {
		this.pointToPiece.remove(point);
	}

	/**
	 * The objective of this method is to put the square in the map representing
	 * squares with its coordinates.
	 * <p>
	 * It will permit us to know where the square is located. ( Key = Square, Value
	 * = its coordinates a Point).
	 * 
	 * @param point  : represents the coordinates of the piece.
	 * @param square : is the square to add.
	 */

	public void putPointToSquare(Square point, Square square) {

		this.pointToSquare.put(point, square);
	}

	public boolean isAvailableTargetForMove(Square point) {

		final boolean move;

		if (Square.SquareState.IN_PLAY.equals(getSquare(point))) {

			if (null == getPiece(point)) {

				move = true;

			} else
				move = false;

		} else
			move = false;

		System.out.println("isAvalaible(" + ") move=" + move);

		return move;

	}

	/**
	 * Move a piece from one position to another.
	 * 
	 * @param from : the initial position
	 * @param to   : arrival position.
	 */

	public void movePiece(Square from, Square to) {

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

	public boolean isValidToMove(Square from, Square to) {

		if (getPiece(from) != null) {

			if (isAvailableTargetForMove(to)) {

				return true;

			}

		}
		return false;
	}

	/**
	 * LoadPieces is the method which loads all the pieces in the CheckerBoard.
	 * 
	 * @param pieces : a list of all the checker pieces.
	 */

	public void loadPieces(List<Piece> pieces) {

		final List<Piece> listPiece = new ArrayList<>(pieces);

		for (Square point : generateSquares()) {

			final Square val = getSquare(point);

			if (Square.SquareState.IN_PLAY.equals(val)) {

				final Piece piece = listPiece.remove(0);

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
