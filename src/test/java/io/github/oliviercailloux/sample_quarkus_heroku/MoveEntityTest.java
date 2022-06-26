package io.github.oliviercailloux.sample_quarkus_heroku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import io.github.oliviercailloux.abg.GameEntity;
import io.github.oliviercailloux.abg.MoveDAO;
import io.github.oliviercailloux.abg.MoveEntity;
import io.quarkus.runtime.ApplicationConfig;
import io.quarkus.test.junit.QuarkusTest;
import java.time.Duration;
import org.hamcrest.core.Is;
import org.hamcrest.text.MatchesPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MoveEntityTest {
  static MoveEntity moveEntity;
  static MoveEntity anotherMoveEntity;
  static MoveEntity anotherMoveEntityagain;

  GameEntity game;
  MoveDAO move;
  Duration duration;
  Square from;
  Square to;
  Piece promotion;

  @BeforeAll
  public static void instanciate() {
    moveEntity = new MoveEntity();
  }

  @Test
  public void testMoveEntity() {
    assertEquals(Square.NONE, moveEntity.getFrom());
    assertEquals(Square.NONE, moveEntity.getTo());
    assertEquals(Piece.NONE, moveEntity.getPromotion());
  }

  @Test
  public void testMoveEntity2() {
    game = new GameEntity();
    anotherMoveEntity = new MoveEntity(game, Square.A7, Square.B3, promotion, duration);
    assertNotNull(anotherMoveEntity.getGame());
    assertEquals(Duration.ZERO, anotherMoveEntity.getDuration());
    assertEquals(Piece.NONE, anotherMoveEntity.getPromotion());

    anotherMoveEntityagain =
        new MoveEntity(game, Square.A7, Square.B3, Piece.BLACK_KING, Duration.ofSeconds(50));
    assertNotNull(anotherMoveEntity.getGame());
    assertEquals(Duration.ofSeconds(50), anotherMoveEntityagain.getDuration());
    assertEquals(Piece.BLACK_KING, anotherMoveEntityagain.getPromotion());
  }

  @Test
  public void testCreateMoveEntity() {
    MoveEntity anotherMove = MoveEntity.createMoveEntity(game, move, duration);
  }
}
