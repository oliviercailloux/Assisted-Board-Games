package io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ChessState {
	private int idState;
	private IChessPiece[][] _board;

	public ChessState(int id, IChessPiece[][] board) {
		idState = id;
		_board = board;
	}

	@SuppressWarnings("unchecked")
	public JSONObject encodeJson() {
		JSONObject jsonState = new JSONObject();
		JSONArray jsonBoard = new JSONArray();
		
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				JSONObject obj = new JSONObject();
				obj.put("caseX", _board[i][j].getX());
				obj.put("caseY", _board[i][j].getY());
				obj.put("type", _board[i][j].getType());
				obj.put("color", _board[i][j].color());
				jsonBoard.add(obj);
			}
		}

		
		jsonState.put("id", idState);
		jsonState.put("board", jsonBoard);
		
		return jsonState;
		
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
