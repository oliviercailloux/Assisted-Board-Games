package io.github.oliviercailloux.sample_quarkus_heroku;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.bhlangonijr.chesslib.Side;
import io.github.oliviercailloux.abg.ChessBoard;
import io.github.oliviercailloux.abg.GameEntity;
import io.github.oliviercailloux.abg.model.state.GameState;
import io.quarkus.test.junit.QuarkusTest;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import org.hamcrest.core.Is;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusTest
public class GameEntityTest {

  static GameEntity game;
  static GameState gameState;
  static ChessBoard chessBoard;
  final Instant fixedInstant =
      Instant.now(Clock.fixed(Instant.parse("2022-06-22T10:00:00Z"), ZoneOffset.UTC));
  private static final Logger LOGGER = LoggerFactory.getLogger(GameEntityTest.class);

  @Test
  public void testEntity() { // Test du constructeur de GameEntity
    game = new GameEntity();
    assertEquals(Duration.ofSeconds(1800), game.getClockDuration());
    assertEquals(Duration.ofSeconds(10), game.getClockIncrement());
    ArrayList moves = new ArrayList<>(); // avoid NPE in tests
    assertEquals(moves, game.getMoves());
    LOGGER.info("The game entity has been successfully instantiated by default");
  }

  @Test
  public void testGameEntity() { // Test de la méthode createNewGameWithChess();
    game = GameEntity.cerateNewGameWithChess();
    assertEquals(Duration.ofSeconds(1800), game.getClockDuration());
    assertEquals(Duration.ofSeconds(10), game.getClockIncrement());
    LOGGER.info("The game entity has been successfully instantiated with the parameters");
  }
}
