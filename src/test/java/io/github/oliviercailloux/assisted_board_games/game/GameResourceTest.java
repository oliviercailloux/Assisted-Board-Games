package io.github.oliviercailloux.assisted_board_games.game;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
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
import com.google.common.io.CharStreams;

import io.github.oliviercailloux.assisted_board_games.AppConfig;
import io.github.oliviercailloux.assisted_board_games.model.GameDAO;
import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.model.MoveDAO;

class GameResourceTest {

    public static final String PETROV_DEFENSE_FEN_STRING = "rnbqkb1r/pppp1ppp/5n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 2 3";
    public static final String RUY_LOPEZ_FEN_STRING = "r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3";
    private static final Entity<String> EMPTY_ENTITY = Entity.text("");
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
                .post(EMPTY_ENTITY);
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
    void testImportPgn() throws IOException {
        final WebTarget importPgn = target.path("api/v1/game/import/pgn");
        final String pgnString;
        try (InputStream inputStream = GameResourceTest.class.getResourceAsStream("fischer_spassky.pgn")) {
            try (Reader reader = new InputStreamReader(inputStream)) {
                pgnString = CharStreams.toString(reader);
            }
        }
        assertNotNull(pgnString);
        final Response response = importPgn
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.text(pgnString));
        response.bufferEntity();
        assertDoesNotThrow(() -> response.readEntity(new GenericType<List<String>>() {
        }));
        final List<String> gameIds = response.readEntity(new GenericType<List<String>>() {
        });
        final String gameId = gameIds.get(0);
        final WebTarget getGame = target.path("api/v1/game").path(gameId);
        final Response getGameResponse = getGame
                .request(MediaType.TEXT_PLAIN)
                .get();
        final String endPosition = "8/8/4R1p1/2k3p1/1p4P1/1P1b1P2/3K1n2/8 b - - 2 43";
        assertEquals(endPosition, getGameResponse.readEntity(String.class));
    }

    @Test
    void testCreateVariation() throws IOException {
        final WebTarget importPgn = target.path("api/v1/game/import/pgn");
        final String pgnString;
        try (InputStream inputStream = GameResourceTest.class.getResourceAsStream("fischer_spassky.pgn")) {
            try (Reader reader = new InputStreamReader(inputStream)) {
                pgnString = CharStreams.toString(reader);
            }
        }
        assertNotNull(pgnString);
        final Response response = importPgn
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.text(pgnString));
        response.bufferEntity();
        assertDoesNotThrow(() -> response.readEntity(new GenericType<List<String>>() {
        }));
        final List<String> gameIds = response.readEntity(new GenericType<List<String>>() {
        });
        final String gameId = gameIds.get(0);
        final WebTarget createVariation = target.path("api/v1/game")
                .path(gameId)
                .path("variation");
        final Response variationResponse = createVariation
                .queryParam("move", 3)
                .queryParam("side", 1)
                .request(MediaType.TEXT_PLAIN)
                .post(EMPTY_ENTITY);
        variationResponse.bufferEntity();
        assertDoesNotThrow(() -> variationResponse.readEntity(Integer.class));
        final String variationId = variationResponse.readEntity(String.class);
        final WebTarget getVariation = target.path("api/v1/game").path(variationId);
        final Response getVariationResponse = getVariation
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertEquals(RUY_LOPEZ_FEN_STRING, getVariationResponse.readEntity(String.class));
    }

    @Test
    void testGetGame() {
        final WebTarget createGame = target.path("api/v1/game/new");
        final Response createGameResponse = createGame
                .request(MediaType.TEXT_PLAIN)
                .post(EMPTY_ENTITY);
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
                .post(EMPTY_ENTITY);
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
                .post(EMPTY_ENTITY);
        final String gameId = createGameResponse.readEntity(String.class);
        final WebTarget getMoves = target.path("api/v1/game")
                .path(gameId)
                .path("moves");
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
                .post(EMPTY_ENTITY);
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
