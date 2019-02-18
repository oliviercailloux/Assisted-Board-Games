package io.github.oliviercailloux.y2018.assisted_board_games.game;

import javax.json.*;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;

/***
 * 
 * @author Thibaud Fremin du Sartel
 *
 */
public class ChessState {
	
	public static JsonObject encodeState(Board board) {
		Piece pieces[] = board.boardToArray();
		JsonObjectBuilder obj = Json.createObjectBuilder();
		
		for (int i=0; i<pieces.length; i++) {
			Piece p = pieces[i];

			
			obj.add(Integer.toString(i), p.value());
			
		}
		obj.add("player", board.getSideToMove().value());
		
		return obj.build();
	}
	
	public static Board decodeFromJson(JsonObject json) {
		Board board = new Board();
		Piece pieces[] = new Piece[64];
		String player = json.getString("player");
		String fenString = "";
		int count = 0;
		
		// write the first part of the FEN String, containing the places of the pieces on the board
		for(int i=0; i<pieces.length; i++) {
			if(i != 0 && i%8 == 0) {
				fenString += "/";
			}
			String currentPiece = json.getString(Integer.toString(i));
			
			switch (currentPiece) {
			case "WHITE_ROOK":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "r";
				break;
				
			case "BLACK_ROOK":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "R";
				break;
				
			case "WHITE_KNIGHT":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "n";
				break;
				
			case "BLACK_KNIGHT":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "N";
				break;
				
			case "BLACK_BISHOP":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "B";
				break;
				
			case "WHITE_BISHOP":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "b";
				break;
				
			case "WHITE_QUEEN":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "q";
				break;
				
			case "BLACK_QUEEN":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "Q";
				break;
				
			case "WHITE_KING":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "k";
				break;
				
			case "BLACK_KING":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "K";
				break;
				
			case "WHITE_PAWN":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "p";
				break;
				
			case "BLACK_PAWN":
				if(count != 0){
					fenString += Integer.toString(count);
					count = 0;
				}
				fenString += "P";
				break;
				
			case "NONE":
				count ++;
				break;

			default:
				break;
			} 
			
			if((i+1)%8 == 0) {
				// last square of the line : add count's value to fenString if not = 0 
				// 							 set count to zero
				if(count != 0) {
					fenString += Integer.toString(count);
					count = 0;
				}
			}
				
			 
		}
		
		//add the player who has to play next
		if(player.equals("WHITE")) {
			fenString += " w";
		}else {
			fenString += " b";
		}
		
		//add the rest set to "-"
		fenString += " - - 0 1";
		
		board.loadFromFen(fenString);
		
		return board;
	}
	

}
