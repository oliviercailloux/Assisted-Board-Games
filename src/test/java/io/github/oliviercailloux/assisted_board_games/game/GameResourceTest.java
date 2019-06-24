package io.github.oliviercailloux.assisted_board_games.game;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;

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
import com.google.common.collect.ImmutableList;

import io.github.oliviercailloux.assisted_board_games.AppConfig;
import io.github.oliviercailloux.assisted_board_games.model.GameDAO;
import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.model.MoveDAO;

class GameResourceTest {

    public static final String PETROV_DEFENSE_FEN_STRING = "rnbqkb1r/pppp1ppp/5n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3";
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
        assertDoesNotThrow(() -> response.readEntity(Integer.class));
    }

    @Test
    void testImportGame() {
        final WebTarget importGame = target.path("api/v1/game/import");
        final GameDAO gameDAO = GameDAO.createGameDAO(Instant.EPOCH, Duration.ofMinutes(10), Duration.ZERO,
                GameEntity.STARTING_FEN_POSITION,
                ImmutableList.of(
                        MoveDAO.createMoveDAO(Square.E2, Square.E4, Piece.NONE),
                        MoveDAO.createMoveDAO(Square.E7, Square.E5, Piece.NONE),
                        MoveDAO.createMoveDAO(Square.G1, Square.F3, Piece.NONE),
                        MoveDAO.createMoveDAO(Square.G8, Square.F6, Piece.NONE)));
        final Response response = importGame
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.json(gameDAO));
        response.bufferEntity();
        assertDoesNotThrow(() -> response.readEntity(Integer.class));
        final String gameId = response.readEntity(String.class);
        final WebTarget getGame = target.path("api/v1/game").path(gameId);
        final Response getGameResponse = getGame
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertEquals(PETROV_DEFENSE_FEN_STRING, getGameResponse.readEntity(String.class));
    }

    @Test
    void testImportFen() {
        final WebTarget importFen = target.path("api/v1/game/import/fen");
        final Response response = importFen
                .request(MediaType.TEXT_PLAIN)
                .post(Entity.text(PETROV_DEFENSE_FEN_STRING));
        response.bufferEntity();
        assertDoesNotThrow(() -> response.readEntity(Integer.class));
        final String gameId = response.readEntity(String.class);
        final WebTarget getGame = target.path("api/v1/game").path(gameId);
        final Response getGameResponse = getGame
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertEquals(PETROV_DEFENSE_FEN_STRING, getGameResponse.readEntity(String.class));
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
