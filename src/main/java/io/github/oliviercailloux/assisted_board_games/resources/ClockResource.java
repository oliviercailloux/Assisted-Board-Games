package io.github.oliviercailloux.assisted_board_games.resources;

import java.time.Duration;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.service.ChessService;
import io.github.oliviercailloux.assisted_board_games.utils.ClockUtils;

@Path("clock")
@RequestScoped
public class ClockResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClockResource.class);
    @Inject
    ChessService chessService;

    @GET
    @Path("{gameId}/black")
    public Duration getBlackRemainingTime(@PathParam("gameId") int gameId) {
        LOGGER.info("GET clock/{}/black", gameId);
        GameEntity game = chessService.getGame(gameId);
        return ClockUtils.getBlackRemainingDuration(game);
    }

    @GET
    @Path("{gameId}/white")
    public Duration getWhiteRemainingTime(@PathParam("gameId") int gameId) {
        LOGGER.info("GET clock/{}/white", gameId);
        GameEntity game = chessService.getGame(gameId);
        return ClockUtils.getWhiteRemainingDuration(game);
    }
}
