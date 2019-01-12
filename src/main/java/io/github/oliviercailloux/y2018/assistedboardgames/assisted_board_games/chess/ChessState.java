package io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.pieces.Bishop;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.pieces.King;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.pieces.Knight;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.pieces.Pawn;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.pieces.Queen;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.pieces.Rook;

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

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				JSONObject obj = new JSONObject();
				if (_board[i][j] != null) {
					obj.put("tabX", i);
					obj.put("tabY", j);
					obj.put("caseX", _board[i][j].getX());
					obj.put("caseY", _board[i][j].getY());
					obj.put("type", _board[i][j].getType());
					obj.put("color", _board[i][j].color());
					obj.put("value", _board[i][j].getValue());
				} else {
					obj.put("tabX", i);
					obj.put("tabY", j);
					obj.put("caseX", null);
					obj.put("caseY", null);
					obj.put("type", null);
					obj.put("color", null);
					obj.put("value", null);
				}
				jsonBoard.add(obj);
			}
		}

		jsonState.put("id", idState);
		jsonState.put("board", jsonBoard);

		return jsonState;

	}

	public static ChessState decodeStateJson(JSONObject obj) {
		int idState = (int) obj.get("id");
		IChessPiece[][] board = new IChessPiece[8][8];
		JSONArray jsonBoard = (JSONArray) obj.get("board");

		for (Object square : jsonBoard) {
			JSONObject piece = (JSONObject) square;
			if (piece.get("type") != null) {
				switch ((int) piece.get("type")) {
				case 5:
					board[(int) piece.get("tabX")][(int) piece.get("tabY")] = new King((int) piece.get("caseX"),
							(int) piece.get("caseY"), (boolean) piece.get("color"));
					break;
				case 1:
					board[(int) piece.get("tabX")][(int) piece.get("tabY")] = new Knight((int) piece.get("caseX"),
							(int) piece.get("caseY"), (boolean) piece.get("color"));
					break;
				case 0:
					board[(int) piece.get("tabX")][(int) piece.get("tabY")] = new Pawn((int) piece.get("caseX"),
							(int) piece.get("caseY"), (boolean) piece.get("color"));
					break;
				case 4:
					board[(int) piece.get("tabX")][(int) piece.get("tabY")] = new Queen((int) piece.get("caseX"),
							(int) piece.get("caseY"), (boolean) piece.get("color"));
					break;
				case 3:
					board[(int) piece.get("tabX")][(int) piece.get("tabY")] = new Rook((int) piece.get("caseX"),
							(int) piece.get("caseY"), (boolean) piece.get("color"));
					break;
				case 2:
					board[(int) piece.get("tabX")][(int) piece.get("tabY")] = new Bishop((int) piece.get("caseX"),
							(int) piece.get("caseY"), (boolean) piece.get("color"));
					break;
				default:
					break;
				}
			}
		}

		return new ChessState(idState, board);

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
	/**Megan Brassard feature "ChessMoveTXT **/
	public ChessState doMove(ChessMove move) {
		if (move == null) return null; 
      	 IChessPiece movingPiece =get_board()[move.getOrigX()][move.getOrigY()];
      	 if (movingPiece == null) {
           return this;
      	 }
      	 IChessPiece [][]copy= new IChessPiece [8][8] ;
      	 for (int i = 0;i < 8 ;i++) {
      		 for (int j = 0; j< 8; j++) {
      			 if( _board[i][j]!= null)
      			 copy[i][j]=_board[i][j].copyPiece();
      		 }
      	 }
      	 IChessPiece tomove =copy[move.getOrigX()][move.getOrigY()]; 
      	 copy[move.getOrigX()][move.getOrigY()]= null;
      	 copy[move.getDestX()][move.getDestY()]= tomove;
      	 tomove.setX(move.getDestX());
      	 tomove.setY(move.getDestY());
      	 
      	 return  new ChessState( -1, copy);
		
	}

}
