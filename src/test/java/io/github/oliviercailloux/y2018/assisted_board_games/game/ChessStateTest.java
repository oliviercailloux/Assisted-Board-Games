package io.github.oliviercailloux.y2018.assisted_board_games.game;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.json.JsonObject;


import org.apache.commons.io.IOUtils;
//import org.eclipse.persistence.jpa.jpql.tools.model.EclipseLinkConditionalStateObjectBuilder;
import org.junit.jupiter.api.Test;

import com.github.bhlangonijr.chesslib.Board;
import io.github.oliviercailloux.y2018.assisted_board_games.game.ChessState;
//import io.github.oliviercailloux.y2018.assisted_board_games.ressources.utils.JsonHelper;

/***
 * @author Thibaud Fremin du Sartel
 * 
 */
class ChessStateTest {
	
	private final String fileTest = "src/main/ressources/junit_utils/initialJsonBoard.txt";

	@Test
	public void testEncodeState() throws IOException, FileNotFoundException {
//		JsonHelper jsonHelper = new JsonHelper();
		Board board = new Board();
		JsonObject encodedBoard = ChessState.encodeState(board);
//		String result = jsonHelper.asPrettyString(encodedBoard);

		FileInputStream fis = new FileInputStream(fileTest);
		String expectedResult = IOUtils.toString(fis, "UTF-8");
		
//		System.out.println("expected:\n" + expectedResult);
//		System.out.println("result:\n" + result);
//		System.out.println("result2:\n" + result2);
		
		assertEquals(expectedResult, encodedBoard.toString());
	}

	@Test
	public void testDecodeFromJson() {
		String boardString = "rnbqkbnr\n" + 
				"pppppppp\n" + 
				"        \n" + 
				"        \n" + 
				"        \n" + 
				"        \n" + 
				"PPPPPPPP\n" + 
				"RNBQKBNR\n" + 
				"Side: WHITE";
		Board firstBoard = new Board();
		JsonObject obj = ChessState.encodeState(firstBoard);

		Board secondBoard = ChessState.decodeFromJson(obj);
		String boardTest = secondBoard.toString();

		assertEquals(boardString, boardTest);

	}
	

}
