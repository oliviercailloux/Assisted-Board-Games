package io.github.oliviercailloux.y2018.assisted_board_games.game;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

import com.github.bhlangonijr.chesslib.Board;
import io.github.oliviercailloux.y2018.assisted_board_games.game.ChessState;
import io.github.oliviercailloux.y2018.assisted_board_games.utils.JsonHelper;

/***
 * @author Thibaud Fremin du Sartel
 * 
 */
class ChessStateTest {

	private final String fileTest = "initialJsonBoard.txt";

	@Test
	public void testEncodeState() throws IOException, FileNotFoundException {
		JsonHelper jsonHelper = new JsonHelper();
		Board board = new Board();
		JsonObject encodedBoard = ChessState.encodeState(board);
		String result = jsonHelper.asPrettyString(encodedBoard);

		URL resourceUrl = ChessStateTest.class.getResource(fileTest);
		BufferedReader in;
		in = new BufferedReader(new InputStreamReader(resourceUrl.openStream()));
		String expectedResult = "";
		String inputLine;
		boolean firstLine = true;

		while ((inputLine = in.readLine()) != null) {
			if (!firstLine) {
				expectedResult += "\n";
			} else {
				firstLine = false;
			}
			expectedResult += inputLine;
		}

		assertEquals(expectedResult, result);
	}

	@Test
	public void testDecodeFromJson() {
		String boardString = "rnbqkbnr\n" 
							+ "pppppppp\n" 
							+ "        \n"
							+ "        \n" 
							+ "        \n" 
							+ "        \n"
							+ "PPPPPPPP\n" 
							+ "RNBQKBNR\n"
							+ "Side: WHITE";
		Board firstBoard = new Board();
		JsonObject obj = ChessState.encodeState(firstBoard);

		Board secondBoard = ChessState.decodeFromJson(obj);
		String boardTest = secondBoard.toString();

		assertEquals(boardString, boardTest);

	}

}
