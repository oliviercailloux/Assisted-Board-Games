package io.github.oliviercailloux.assisted_board_games.game;

import java.net.URI;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.assisted_board_games.resources.GameResource;

class GameResourceTest extends JerseyTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameResourceTest.class);

    @Override
    protected Application configure() {
        return new ResourceConfig(GameResource.class);
    }

    @Override
    protected URI getBaseUri() {
        return URI.create("http://localhost:8080");
    }

    @Test
    public void testCreateGame() {
        Response response = target()
                        .path("/game/new")
                        .request(MediaType.TEXT_PLAIN_TYPE)
                        .post(Entity.text(""));
        LOGGER.info("gameID={}", response.getEntity());
    }
}
