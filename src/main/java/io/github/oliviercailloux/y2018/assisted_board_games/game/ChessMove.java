package io.github.oliviercailloux.y2018.assisted_board_games.game;

import javax.json.*;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;

/**
 * 
 * @author Megan Brassard
 *
 */
public class ChessMove {
	// private Move move;

	/**
	 * public ChessMoveTXT ( String from, String To, Piece promotion) { move= new
	 * Move( Square.fromValue(from), Square.fromValue(To), promotion);
	 * 
	 * } public Square getFrom() { return move.getFrom(); } public Square getTo() {
	 * return move.getTo(); } public Piece getPromotion() { return
	 * move.getPromotion(); }
	 * 
	 * public Board doMove() { return null; } public static Move getMove(Board
	 * board1, Board board2) { return null; }
	 **/

	public static JsonObject encode(Move move) {
		JsonObject object = Json.createObjectBuilder().add("Square From", move.getFrom().value())
				.add("Square To", move.getTo().value()).add("Piece promotion", move.getPromotion().name()).build();

		return object;

	}

	public static Move decode(JsonObject json) {
		String sFrom = json.getString("Square From");
		String sTo = json.getString("Square To");
		String piece = json.getString("Piece promotion");
		return new Move(Square.fromValue(sFrom), Square.fromValue(sTo), Piece.fromValue(piece));

	}

	public static Move getMove(Board board1, Board board2) throws MoveException {
		Piece[] pieces1 = board1.boardToArray();
		Piece[] pieces2 = board2.boardToArray();
		Square from = Square.NONE;
		Square to = Square.NONE;
		Piece promotion = Piece.NONE;
		for (int i = 0; i < pieces1.length; i++) {
			if (!(pieces1[i].equals(pieces2[i]))) {
				if (pieces2[i].equals(Piece.NONE))
					from = Square.values()[i];
				if (pieces1[i].equals(Piece.NONE))
					to = Square.values()[i];
			}
		}
		if (!board1.getPiece(from).equals(board2.getPiece(to))) {
			promotion = board2.getPiece(to);
		}
		Move m = new Move(from, to, promotion);
		System.out.println(m);
		if (from == Square.NONE)
			return m;
		if (board1.isMoveLegal(m, false))
			return m;
		throw new MoveException("Move is not legal");

	}

	public static Board doMove(Board b, Move move) throws MoveException {
		Board bis = b.clone();
		if (bis.doMove(move))
			return bis;
		return null;
	}

}
