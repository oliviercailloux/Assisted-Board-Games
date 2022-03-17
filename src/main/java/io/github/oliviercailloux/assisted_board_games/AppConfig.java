package io.github.oliviercailloux.assisted_board_games;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * 
 * @author theophile
 *
 */
public class AppConfig extends ResourceConfig {

  public AppConfig() {
    packages("io.github.oliviercailloux.assisted_board_games.resources");
  }
}
