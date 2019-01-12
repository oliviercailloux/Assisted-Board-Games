package io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.ChessGame;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.ChessMove;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.ChessState;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.chess.IChessPiece;
import junit.framework.TestCase;

/**Megan Brassard feature "ChessMoveTXT **/
public class ChessMoveTest extends TestCase{
	@Test
	public void testGetMove() {
		ChessGame game = new ChessGame(true);
		ChessState state1 = new ChessState(1, game.get_board());
		ChessState state2 = new ChessState(2, game.get_board());
		assertEquals(null, ChessMove.getMove(state1, state2));
	}
	
	@Test
	public void testGetMove2() {
		ChessGame game = new ChessGame(true);
		ChessState state1 = new ChessState(1, game.get_board());
		IChessPiece[][] board = new IChessPiece[8][8]; 
		for (int i = 0;i < 8 ;i++) {
     		 for (int j = 0; j< 8; j++) {
     			 if(state1.get_board()[i][j]!=null)
     			 board[i][j]=state1.get_board()[i][j].copyPiece();
     		 }
     	 }
		IChessPiece tomove = board[0][0];
		board[0][0]= null;
		board[5][5]=tomove;
		tomove.setX(5);
		tomove.setY(5);
		ChessState state2 = new ChessState(2, board);
		ChessMove move=ChessMove.getMove(state1, state2);
		
		assertEquals(0, move.getOrigX());
		assertEquals(0, move.getOrigY());
		assertEquals(5, move.getDestX());
		assertEquals(5, move.getDestY());
	}
	
	
	@Test
	public void testEncode() {
		ChessMove move = new ChessMove ( 0, 3, 5, 6);
		JSONObject jo= move.encode();
	    
	    assertEquals(jo.get("OrigX"), 0);
	    assertEquals(jo.get("OrigY"), 3); 
	    assertEquals(jo.get("DestX"), 5); 
	    assertEquals(jo.get("DestY"), 6) ;
	   //(jsonText,"{\"OrigX\": 0,\"OrigY\":3,\"DestX\":5, \"DestY\" : 6}" );
					/**"{\"OrigX\": 0,\"OrigY\":3,\"DestX\":5, \"DestY\" : 6}"
					) );**/
	      
	    
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testDecode () { 
		ChessMove move = new ChessMove ( 0, 3, 5, 6);
		JSONObject jo = new JSONObject();
		jo.put("OrigX", 0);
		jo.put("OrigY", 3);
    	jo.put("DestX", 5);
    	jo.put("DestY", 6);	
    	
    	assertEquals( ChessMove.decode(jo), move);
	}
}
