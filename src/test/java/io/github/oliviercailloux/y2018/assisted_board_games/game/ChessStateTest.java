package io.github.oliviercailloux.y2018.assisted_board_games.game;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.json.JsonObject;


import org.apache.commons.io.IOUtils;

import org.junit.jupiter.api.Test;

import com.github.bhlangonijr.chesslib.Board;
import io.github.oliviercailloux.y2018.assisted_board_games.game.ChessState;
import io.github.oliviercailloux.y2018.assisted_board_games.ressources.utils.JsonHelper;

/***
 * @author Thibaud Fremin du Sartel
 * 
 */
class ChessStateTest {
	
	private final String fileTest = "src/main/ressources/junit_utils/initialJsonBoard.txt";

	@Test
	public void testEncodeState() throws IOException, FileNotFoundException {
		JsonHelper jsonHelper = new JsonHelper();
		Board board = new Board();
		JsonObject encodedBoard = ChessState.encodeState(board);
		String result = jsonHelper.asPrettyString(encodedBoard);
		
		String result2 = "\r\n" + 
				"{\r\n" + 
				"    \"0\": \"WHITE_ROOK\",\r\n" + 
				"    \"1\": \"WHITE_KNIGHT\",\r\n" + 
				"    \"2\": \"WHITE_BISHOP\",\r\n" + 
				"    \"3\": \"WHITE_QUEEN\",\r\n" + 
				"    \"4\": \"WHITE_KING\",\r\n" + 
				"    \"5\": \"WHITE_BISHOP\",\r\n" + 
				"    \"6\": \"WHITE_KNIGHT\",\r\n" + 
				"    \"7\": \"WHITE_ROOK\",\r\n" + 
				"    \"8\": \"WHITE_PAWN\",\r\n" + 
				"    \"9\": \"WHITE_PAWN\",\r\n" + 
				"    \"10\": \"WHITE_PAWN\",\r\n" + 
				"    \"11\": \"WHITE_PAWN\",\r\n" + 
				"    \"12\": \"WHITE_PAWN\",\r\n" + 
				"    \"13\": \"WHITE_PAWN\",\r\n" + 
				"    \"14\": \"WHITE_PAWN\",\r\n" + 
				"    \"15\": \"WHITE_PAWN\",\r\n" + 
				"    \"16\": \"NONE\",\r\n" + 
				"    \"17\": \"NONE\",\r\n" + 
				"    \"18\": \"NONE\",\r\n" + 
				"    \"19\": \"NONE\",\r\n" + 
				"    \"20\": \"NONE\",\r\n" + 
				"    \"21\": \"NONE\",\r\n" + 
				"    \"22\": \"NONE\",\r\n" + 
				"    \"23\": \"NONE\",\r\n" + 
				"    \"24\": \"NONE\",\r\n" + 
				"    \"25\": \"NONE\",\r\n" + 
				"    \"26\": \"NONE\",\r\n" + 
				"    \"27\": \"NONE\",\r\n" + 
				"    \"28\": \"NONE\",\r\n" + 
				"    \"29\": \"NONE\",\r\n" + 
				"    \"30\": \"NONE\",\r\n" + 
				"    \"31\": \"NONE\",\r\n" + 
				"    \"32\": \"NONE\",\r\n" + 
				"    \"33\": \"NONE\",\r\n" + 
				"    \"34\": \"NONE\",\r\n" + 
				"    \"35\": \"NONE\",\r\n" + 
				"    \"36\": \"NONE\",\r\n" + 
				"    \"37\": \"NONE\",\r\n" + 
				"    \"38\": \"NONE\",\r\n" + 
				"    \"39\": \"NONE\",\r\n" + 
				"    \"40\": \"NONE\",\r\n" + 
				"    \"41\": \"NONE\",\r\n" + 
				"    \"42\": \"NONE\",\r\n" + 
				"    \"43\": \"NONE\",\r\n" + 
				"    \"44\": \"NONE\",\r\n" + 
				"    \"45\": \"NONE\",\r\n" + 
				"    \"46\": \"NONE\",\r\n" + 
				"    \"47\": \"NONE\",\r\n" + 
				"    \"48\": \"BLACK_PAWN\",\r\n" + 
				"    \"49\": \"BLACK_PAWN\",\r\n" + 
				"    \"50\": \"BLACK_PAWN\",\r\n" + 
				"    \"51\": \"BLACK_PAWN\",\r\n" + 
				"    \"52\": \"BLACK_PAWN\",\r\n" + 
				"    \"53\": \"BLACK_PAWN\",\r\n" + 
				"    \"54\": \"BLACK_PAWN\",\r\n" + 
				"    \"55\": \"BLACK_PAWN\",\r\n" + 
				"    \"56\": \"BLACK_ROOK\",\r\n" + 
				"    \"57\": \"BLACK_KNIGHT\",\r\n" + 
				"    \"58\": \"BLACK_BISHOP\",\r\n" + 
				"    \"59\": \"BLACK_QUEEN\",\r\n" + 
				"    \"60\": \"BLACK_KING\",\r\n" + 
				"    \"61\": \"BLACK_BISHOP\",\r\n" + 
				"    \"62\": \"BLACK_KNIGHT\",\r\n" + 
				"    \"63\": \"BLACK_ROOK\",\r\n" + 
				"    \"64\": \"NONE\",\r\n" + 
				"    \"player\": \"WHITE\"\r\n" + 
				"}";

		FileInputStream fis = new FileInputStream(fileTest);
		String expectedResult = IOUtils.toString(fis, "UTF-8");
		
		System.out.println("expected:\n" + expectedResult);
		System.out.println("result:\n" + result);
		System.out.println("result2:\n" + result2);
		
		assertEquals(expectedResult, result);
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
