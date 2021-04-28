package io.github.oliviercailloux.assisted_board_games.checkers;

import java.util.List;

public class CheckerBoard implements CheckerBoardInterface {
	
	private String color;
	@Override
	public void Promotion(Piece piece) {
		
		
	}

	@Override
	public List<Point> generatePoints() {
		
		return null;
	}

	@Override
	public Square getSquare(Point point) {
		return Square.givenCoordonate(point);
	}

	@Override
	public Piece getPiece(Point point) {
		return Piece.givenPoint(point, Color.fromValue(color));
	}

	@Override
	public void putPointToPiece(Point point, Piece piece) {
	
		
	}

	@Override
	public void removePointToPiece(Point point) {
	
		
	}

	@Override
	public void putPointToSquare(Point point, Square square) {
	
		
	}

	@Override
	public void movePiece(Point from, Point to) {
		
		
	}

	@Override
	public boolean isValidToMove(Point from, Point to) {
		
		return false;
	}

	@Override
	public void loadPieces(List<Piece> pieces) {
		
		
	}

	@Override
	public boolean isEmpty() {
		
		return false;
	}
	
}
