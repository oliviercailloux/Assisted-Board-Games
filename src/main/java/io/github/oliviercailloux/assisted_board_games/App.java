package io.github.oliviercailloux.assisted_board_games;

import java.net.URI;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.h2.tools.Console;

public class App {

  private static final URI SERVER_URI = URI.create("http://127.0.0.1:8080");

  public static void main(String[] args) throws Exception {
    Console.main();
    Server server = JettyHttpContainerFactory.createServer(SERVER_URI, new AppConfig(), false);
    final Handler serverHandler = server.getHandler();
    /**
     * https://www.eclipse.org/jetty/documentation/current/embedding-jetty.html#_using_handlers
     */
    final ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed(true);
    resourceHandler.setWelcomeFiles(new String[] {"index.html"});
    resourceHandler.setResourceBase("src/main/webapp");
    final HandlerList handlers = new HandlerList();
    handlers.addHandler(resourceHandler);
    handlers.addHandler(serverHandler);
    server.setHandler(handlers);
    server.start();
    server.join();
  }
}
