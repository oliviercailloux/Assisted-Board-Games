package io.github.oliviercailloux.assisted_board_games.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.assisted_board_games.AppConfig;

class GameResourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameResourceTest.class);
    private static Server server;
    private static Client client;
    private static WebTarget target;

    protected static URI getBaseUri() {
        return URI.create("http://localhost:8080");
    }

    @BeforeAll
    protected static void init() throws Exception {
        final URI baseUri = getBaseUri();
        server = JettyHttpContainerFactory.createServer(baseUri, new AppConfig(), true);
        client = ClientBuilder.newClient();
        target = client.target(baseUri);
    }

    @AfterAll
    protected static void clean() throws Exception {
        client.close();
        server.stop();
    }

    @Test
    void testCreateGame() {
        final WebTarget createGame = target.path("game/new");
        final Response response = createGame
                        .request(MediaType.TEXT_PLAIN)
                        .post(Entity.text(""));
        assertFalse(response.readEntity(String.class).isEmpty());
    }

    @Test
    void testGetGame() {
        final WebTarget createGame = target.path("game/new");
        final Response createGameResponse = createGame
                        .request(MediaType.TEXT_PLAIN)
                        .post(Entity.text(""));
        final int gameId = createGameResponse.readEntity(Integer.class);
        final WebTarget getGame = target.path("game/get");
        final Response response = getGame
                        .queryParam("gid", gameId)
                        .request(MediaType.TEXT_PLAIN)
                        .get();
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", response.readEntity(String.class));
    }

    void testAddMove() {
    }
}
