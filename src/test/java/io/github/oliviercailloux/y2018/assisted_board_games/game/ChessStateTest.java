package io.github.oliviercailloux.y2018.assisted_board_games.game;
import static org.junit.jupiter.api.Assertions.*;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

import com.github.bhlangonijr.chesslib.Board;

import io.github.oliviercailloux.y2018.assisted_board_games.game.ChessState;

/***
 *  @author Thibaud Fremin du Sartel
 *  
 */
class ChessStateTest {

	@Test
	public void testEncodeState () {
		Board board = new Board();
		JsonObject obj = ChessState.encodeState(board);
		
		assertEquals("WHITE", obj.getString("player"));
		assertEquals("WHITE_ROOK", obj.getString("0"));
		assertEquals("BLACK_KING", obj.getString("60"));
	}
	
	@Test
	public void testDecodeFromJson() {
		Board firstBoard = new Board();
		String boardTest1 = firstBoard.toString();
		
		JsonObject obj = ChessState.encodeState(firstBoard);
		
		Board secondBoard = ChessState.decodeFromJson(obj);
		String boardTest2 = secondBoard.toString();
		
		assertEquals(boardTest1, boardTest2);
		
	}
	

}
