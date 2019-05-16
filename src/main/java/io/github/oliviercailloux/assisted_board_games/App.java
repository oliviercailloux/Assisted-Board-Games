package io.github.oliviercailloux.assisted_board_games;

import java.net.URI;

import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.h2.tools.Console;

public class App {

    public static void main(String[] args) throws Exception {
        Console.main();
        Server server = JettyHttpContainerFactory.createServer(URI.create("http://127.0.0.1:8080/"), new AppConfig(),
                false);
        server.start();
        server.join();
    }
}
