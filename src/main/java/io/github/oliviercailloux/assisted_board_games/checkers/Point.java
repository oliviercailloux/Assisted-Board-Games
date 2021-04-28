package io.github.oliviercailloux.assisted_board_games.checkers;

public class Point {

	private  int row;
	private int column;
	
	Point(int x, int y){
		this.row =x;
		this.column=y;
	}
	
	public static final Point given(int x, int y) {
		return new Point(x, y);
	}
	
	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	
}
