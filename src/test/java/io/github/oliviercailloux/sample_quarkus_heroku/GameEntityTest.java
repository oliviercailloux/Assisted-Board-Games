package io.github.oliviercailloux.sample_quarkus_heroku;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.oliviercailloux.abg.GameEntity;
import io.quarkus.test.junit.QuarkusTest;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import org.hamcrest.core.Is;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class GameEntityTest {

  @Test
  public void testEntity() {
    final GameEntity game = new GameEntity();
    assertEquals(Duration.ofSeconds(1800), game.getClockDuration());
    assertEquals(Duration.ofSeconds(10), game.getClockIncrement());
    ArrayList moves = new ArrayList<>(); // avoid NPE in tests
    assertEquals(moves, game.getMoves());
  }
  
//  @Test
//  public Instant testSetStartTime() {
//    final GameEntity game = new GameEntity();
//    assertEquals(startTime, game.getStartTime());
//    return game.startTime;
//    
//  }

}
