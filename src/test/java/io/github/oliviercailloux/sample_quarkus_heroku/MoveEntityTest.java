package io.github.oliviercailloux.sample_quarkus_heroku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import io.github.oliviercailloux.abg.GameEntity;
import io.github.oliviercailloux.abg.MoveDAO;
import io.github.oliviercailloux.abg.MoveEntity;
import io.quarkus.runtime.ApplicationConfig;
import io.quarkus.test.junit.QuarkusTest;
import java.time.Duration;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbProperty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusTest
public class MoveEntityTest {
  static MoveEntity moveEntity;
  GameEntity game;
  static MoveDAO move;
  Duration duration;
  static Square from;
  static Square to;
  static Piece promotion;
  private static final Logger LOGGER = LoggerFactory.getLogger(MoveEntityTest.class);

  @Test
  public void testCreateMoveEntityNull() {
    // Test
    assertThrows(IllegalArgumentException.class, () -> {
      MoveEntity.createMoveEntity(game, move, duration);
    });
    LOGGER.info("The Illegal Exception has been thrown and detected");
  }

  @Test
  public void testCreateMoveEntityInstance() {
    from = Square.A7;
    to = Square.B3;
    promotion = Piece.BLACK_KING;
    game = new GameEntity();
    move = MoveDAO.createMoveDAO(from, to, promotion);
    duration = Duration.ofSeconds(50);

    moveEntity = MoveEntity.createMoveEntity(game, move, duration);
    assertEquals(Duration.ofSeconds(1800), moveEntity.getGame().getClockDuration());
    assertEquals(Duration.ofSeconds(10), moveEntity.getGame().getClockIncrement());
    assertEquals(Duration.ofSeconds(1800), moveEntity.getGame().getClockDuration());
    assertEquals(Square.A7, moveEntity.getFrom());
    assertEquals(Square.B3, moveEntity.getTo());
    assertEquals(Piece.BLACK_KING, moveEntity.getPromotion());
    LOGGER.info("The Move entity has been successfully instantiated");

  }
}
