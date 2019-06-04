package io.github.oliviercailloux.assisted_board_games.game;

import java.net.URI;

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
    private Server server;

    protected URI getBaseUri() {
        return URI.create("http://localhost:8080");
    }

    @BeforeAll
    protected void startServer() throws Exception {
        server = JettyHttpContainerFactory.createServer(getBaseUri(), new AppConfig(), false);
        server.start();
        server.join();
    }

    @AfterAll
    protected void stopServer() throws Exception {
        server.stop();
    }

    @Test
    void testCreateGame() {
    }
}
