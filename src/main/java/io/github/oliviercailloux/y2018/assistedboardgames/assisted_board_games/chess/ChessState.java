package io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess;

import org.json.simple.JSONObject;

public class ChessState {
	private int idState;
	private IChessPiece[][] _board;
	
	public ChessState (int id, IChessPiece[][] board) {
		idState = id;
		_board = board;
	}

	public int getIdState() {
		return idState;
	}

	public void setIdState(int idState) {
		this.idState = idState;
	}

	public IChessPiece[][] get_board() {
		return _board;
	}

	public void set_board(IChessPiece[][] _board) {
		this._board = _board;
	}
	
	
	
	
}
