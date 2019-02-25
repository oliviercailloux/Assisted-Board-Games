package io.github.oliviercailloux.y2018.assisted_board_games.game;

import static org.junit.jupiter.api.Assertions.*;

import javax.json.JsonObject;

import org.junit.jupiter.api.Test;

import com.github.bhlangonijr.chesslib.Board;

import io.github.oliviercailloux.y2018.assisted_board_games.game.ChessState;

/***
 * @author Thibaud Fremin du Sartel
 * 
 */
class ChessStateTest {

	@Test
	public void testEncodeState() {
		Board board = new Board();
		JsonObject obj = ChessState.encodeState(board);
		
		String result = "{\"0\":\"WHITE_ROOK\",\"1\":\"WHITE_KNIGHT\",\"2\":\"WHITE_BISHOP\",\"3\":\"WHITE_QUEEN\",\"4\":\"WHITE_KING\",\"5\":\"WHITE_BISHOP\",\"6\":\"WHITE_KNIGHT\",\"7\":\"WHITE_ROOK\",\"8\":\"WHITE_PAWN\",\"9\":\"WHITE_PAWN\",\"10\":\"WHITE_PAWN\",\"11\":\"WHITE_PAWN\",\"12\":\"WHITE_PAWN\",\"13\":\"WHITE_PAWN\",\"14\":\"WHITE_PAWN\",\"15\":\"WHITE_PAWN\",\"16\":\"NONE\",\"17\":\"NONE\",\"18\":\"NONE\",\"19\":\"NONE\",\"20\":\"NONE\",\"21\":\"NONE\",\"22\":\"NONE\",\"23\":\"NONE\",\"24\":\"NONE\",\"25\":\"NONE\",\"26\":\"NONE\",\"27\":\"NONE\",\"28\":\"NONE\",\"29\":\"NONE\",\"30\":\"NONE\",\"31\":\"NONE\",\"32\":\"NONE\",\"33\":\"NONE\",\"34\":\"NONE\",\"35\":\"NONE\",\"36\":\"NONE\",\"37\":\"NONE\",\"38\":\"NONE\",\"39\":\"NONE\",\"40\":\"NONE\",\"41\":\"NONE\",\"42\":\"NONE\",\"43\":\"NONE\",\"44\":\"NONE\",\"45\":\"NONE\",\"46\":\"NONE\",\"47\":\"NONE\",\"48\":\"BLACK_PAWN\",\"49\":\"BLACK_PAWN\",\"50\":\"BLACK_PAWN\",\"51\":\"BLACK_PAWN\",\"52\":\"BLACK_PAWN\",\"53\":\"BLACK_PAWN\",\"54\":\"BLACK_PAWN\",\"55\":\"BLACK_PAWN\",\"56\":\"BLACK_ROOK\",\"57\":\"BLACK_KNIGHT\",\"58\":\"BLACK_BISHOP\",\"59\":\"BLACK_QUEEN\",\"60\":\"BLACK_KING\",\"61\":\"BLACK_BISHOP\",\"62\":\"BLACK_KNIGHT\",\"63\":\"BLACK_ROOK\",\"64\":\"NONE\",\"player\":\"WHITE\"}";
		
		assertEquals(result, obj.toString());
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
