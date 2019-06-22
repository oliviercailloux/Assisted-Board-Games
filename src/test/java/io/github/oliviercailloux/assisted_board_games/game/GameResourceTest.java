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

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;

import io.github.oliviercailloux.assisted_board_games.AppConfig;
import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.model.MoveDAO;

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
        final WebTarget createGame = target.path("api/v1/game/new");
        final Response response = createGame
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.text(""));
        assertFalse(response.readEntity(String.class).isEmpty());
    }

    @Test
    void testGetGame() {
        final WebTarget createGame = target.path("api/v1/game/new");
        final Response createGameResponse = createGame
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.text(""));
        final String gameId = createGameResponse.readEntity(String.class);
        final WebTarget getGame = target.path("api/v1/game").path(gameId);
        final Response response = getGame
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertEquals(GameEntity.STARTING_FEN_POSITION, response.readEntity(String.class));
    }

    @Test
    void testAddMove() {
        final WebTarget createGame = target.path("api/v1/game/new");
        final Response createGameResponse = createGame
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.text(""));
        final String gameId = createGameResponse.readEntity(String.class);
        final WebTarget addMove = target.path("api/v1/game")
                .path(gameId)
                .path("move");
        final MoveDAO move = MoveDAO.createMoveDAO(Square.E2, Square.E4, Piece.NONE);
        final Response response = addMove
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.json(move));
        // Since the function returns void we check the status code of the request
        assertEquals(204, response.getStatus());
    }

    @Test
    void testGetMoves() {
        final WebTarget createGame = target.path("api/v1/game/new");
        final Response createGameResponse = createGame
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.text(""));
        final String gameId = createGameResponse.readEntity(String.class);
        final WebTarget getMoves = target.path("api/v1/game")
                .path(gameId)
                .path("/moves");
        final Response response = getMoves
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertEquals("", response.readEntity(String.class));
    }

    @Test
    void testGetLastMove() {
        final WebTarget createGame = target.path("api/v1/game/new");
        final Response createGameResponse = createGame
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.text(""));
        final String gameId = createGameResponse.readEntity(String.class);
        final WebTarget getLastMove = target.path("api/v1/game")
                .path(gameId)
                .path("moves");
        final Response response = getLastMove
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertEquals("", response.readEntity(String.class));
    }
}
