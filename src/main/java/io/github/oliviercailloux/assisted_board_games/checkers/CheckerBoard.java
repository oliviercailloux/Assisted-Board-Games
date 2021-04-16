package io.github.oliviercailloux.assisted_board_games.checkers;

public class CheckerBoard {

	private static final int TAILLE = 10;
	private Square[][] squares;
	private boolean whiteBoard;
	public CheckerBoard() {
		this.squares = new Square[TAILLE][TAILLE];
		for ( int y = 0; y<squares.length; y ++) {
			for ( int x = 0; x<this.squares.length; x++) {
				whiteBoard = ((x+y)%2 == 0);
				if (whiteBoard) {
					this.squares[y][x] = new Square(y,x);
					this.squares[y][x].setWhite();
					this.whiteBoard = false;
					
				}else {
					this.squares[y][x] = new Square(y,x);
					this.whiteBoard = true;
				}
			}
		}
	}
	
	
	
}
