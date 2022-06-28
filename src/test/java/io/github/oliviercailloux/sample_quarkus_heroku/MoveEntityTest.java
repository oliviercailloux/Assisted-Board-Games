package io.github.oliviercailloux.sample_quarkus_heroku;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import io.github.oliviercailloux.abg.GameEntity;
import io.github.oliviercailloux.abg.MoveDAO;
import io.github.oliviercailloux.abg.MoveEntity;
import io.quarkus.runtime.ApplicationConfig;
import io.quarkus.test.junit.QuarkusTest;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MoveEntityTest {
  static MoveEntity moveEntity;

  GameEntity game;
  MoveDAO move;
  Duration duration;

  Square from;
  Square to;
  Piece promotion;

  @Test
  // Test for the method MoveEntity(GameEntity gameEntity, Square from, Square to, Piece promotion,
  // Duration duration)
  public void testMoveEntityMethod() {
    game = new GameEntity();
    // Test de la méthode en laissant les paramètres promotion et duration à NULL
    moveEntity = new MoveEntity(game, Square.A7, Square.B3, promotion, duration);
    assertNotNull(moveEntity.getGame());
    assertEquals(Duration.ZERO, moveEntity.getDuration());
    assertEquals(Piece.NONE, moveEntity.getPromotion());

    // Test de la méthode en instanciant les paramètres promotion et duration)
    moveEntity =
        new MoveEntity(game, Square.A7, Square.B3, Piece.BLACK_KING, Duration.ofSeconds(50));
    assertNotNull(moveEntity.getGame());
    assertEquals(Duration.ofSeconds(50), moveEntity.getDuration());
    assertEquals(Piece.BLACK_KING, moveEntity.getPromotion());
  }

  @Test
  public void testCreateMoveEntity() {
    // Test
    assertThrows(IllegalArgumentException.class, () -> {
      MoveEntity.createMoveEntity(game, move, duration);
    });
    move = new MoveDAO(Square.A7, Square.B3, Piece.BLACK_KING);
    moveEntity =
        new MoveEntity(game, Square.A7, Square.B3, Piece.BLACK_KING, Duration.ofSeconds(50));
  }
}
