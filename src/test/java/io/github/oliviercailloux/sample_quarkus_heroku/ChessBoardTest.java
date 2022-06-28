package io.github.oliviercailloux.sample_quarkus_heroku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.MoveException;
import io.github.oliviercailloux.abg.ChessBoard;
import io.github.oliviercailloux.abg.GameEntity;
import io.github.oliviercailloux.abg.MoveDAO;
import io.github.oliviercailloux.abg.MoveEntity;
import io.quarkus.test.junit.QuarkusTest;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ChessBoardTest {
  static ChessBoard chessboard;
  Board board;
  static Square from;
  static Square to;
  static Piece promotion;
  static MoveDAO move;
  static GameEntity game;
  static MoveEntity moveEntity;
  static Duration duration;

  @Test
  public void testCreateChessBoard() { // Test de la méthode createChessBoard()
    chessboard = ChessBoard.createChessBoard();
    assertEquals(GameEntity.STARTING_FEN_POSITION, chessboard.getFen());
    assertEquals(Side.WHITE, chessboard.getSideToMove());
  }

  @Test
  public void testCreateChessBoardFromFen() { // Test de la méthode createChessBoard(String fen)
    chessboard =
        ChessBoard.createChessBoard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
    assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1",
        chessboard.getFen());
    assertEquals(Side.BLACK, chessboard.getSideToMove());
  }
}
