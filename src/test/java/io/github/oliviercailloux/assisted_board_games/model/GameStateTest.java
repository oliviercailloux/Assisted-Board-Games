package io.github.oliviercailloux.assisted_board_games.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.bhlangonijr.chesslib.Side;
import io.github.oliviercailloux.assisted_board_games.model.state.GameState;
import io.github.oliviercailloux.assisted_board_games.model.state.PlayerState;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Test;

/**
 * 
 * @author theophile
 *
 */
class GameStateTest {

  private static final Duration DEFAULT_CLOCK_DURATION = Duration.ofMinutes(30);

  @Test
  void testGameStateCreation() {
    GameEntity gameEntity = new GameEntity();
    gameEntity.setStartTime(Instant.now());
    gameEntity.setClockDuration(DEFAULT_CLOCK_DURATION);
    GameState gameState = gameEntity.getGameState();
    PlayerState white = gameState.getPlayerState(Side.WHITE);
    assertEquals(gameEntity.getClockDuration(), white.getRemainingTime());
    PlayerState black = gameState.getPlayerState(Side.BLACK);
    assertEquals(gameEntity.getClockDuration(), black.getRemainingTime());
  }
}
