package io.github.oliviercailloux.sample_quarkus_heroku;

import io.github.oliviercailloux.abg.GameEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class HelpResourceTest {
    static GameEntity game;
    @Test
    public void testSuggestMove() {
        game = new GameEntity();
        
    }
}
