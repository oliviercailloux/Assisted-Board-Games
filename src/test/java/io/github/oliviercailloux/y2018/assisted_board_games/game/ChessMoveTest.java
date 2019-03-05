package io.github.oliviercailloux.y2018.assisted_board_games.game;

import org.junit.jupiter.api.*;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.json.*;
import io.github.oliviercailloux.y2018.assisted_board_games.game.ChessMove;

/**
 * 
 * @author Megan Brassard
 *
 */
public class ChessMoveTest {
	Board a;
	Board b;
	Move m;

	@BeforeEach
	public void setUp() {
		a = new Board();
		b = new Board();
		m = null;
	}

	@Test
	public void testEncode() {
		m = new Move(Square.A1, Square.A2, Piece.BLACK_ROOK);
		assertEquals("{\"Square From\":\"A1\",\"Square To\":\"A2\",\"Piece promotion\":\"BLACK_ROOK\"}",
				ChessMove.encode(m).toString());
	}

	@Test
	public void testDecode() {
		JsonObject j = Json.createObjectBuilder().add("Square From", "B7").add("Square To", "B6")
				.add("Piece promotion", "WHITE_QUEEN").build();
		m = new Move(Square.B7, Square.B6, Piece.WHITE_QUEEN);
		assertEquals(ChessMove.decode(j), m);

	}

	@Test
	public void testGetMove() throws MoveException{
		m=ChessMove.getMove(a, b);
		assertEquals(m, new Move(Square.NONE, Square.NONE, Piece.NONE));
	}

	@Test
	public void testGetMove1() throws MoveException {
		b.setPiece(Piece.WHITE_PAWN, Square.C3);
		b.unsetPiece(Piece.WHITE_PAWN, Square.C2);
		m=ChessMove.getMove(a, b);
		assertEquals(m, new Move(Square.C2, Square.C3, Piece.NONE));
	}

	@Test
	public void testDoMove() throws MoveException {
		b = ChessMove.doMove(a, new Move(Square.C2, Square.C3, Piece.NONE));
		assertEquals(b.getPiece(Square.C2), Piece.NONE);
		assertEquals(b.getPiece(Square.C3), Piece.WHITE_PAWN);

	}
}
