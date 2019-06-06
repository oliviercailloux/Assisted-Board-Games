package io.github.oliviercailloux.assisted_board_games.resources;

import java.time.Duration;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.MoveException;

import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.model.MoveDAO;
import io.github.oliviercailloux.assisted_board_games.model.MoveEntity;
import io.github.oliviercailloux.assisted_board_games.service.ChessService;
import io.github.oliviercailloux.assisted_board_games.service.MoveService;
import io.github.oliviercailloux.assisted_board_games.utils.GameHelper;

@Path("game")
@RequestScoped
public class GameResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameResource.class);
    @Inject
    ChessService chessService;
    @Inject
    MoveService chessMove;

    @POST
    @Path("new")
    @Produces(MediaType.TEXT_PLAIN)
    public String createGame() {
        LOGGER.info("POST\t/game/new");
        GameEntity game = new GameEntity();
        chessService.persist(game);
        return String.valueOf(game.getId());
    }

    @GET
    @Path("get")
    @Produces(MediaType.TEXT_PLAIN)
    public String getGame(@QueryParam("gid") int gameId) throws MoveException {
        LOGGER.info("GET\t/game/get\tgid={}", gameId);
        GameEntity game = chessService.getGame(gameId);
        List<MoveEntity> moves = game.getMoves();
        Board b = GameHelper.playMoves(moves);
        return b.getFen(true);
    }

    @POST
    @Path("{gameId}/move")
    @Consumes(MediaType.APPLICATION_JSON)
    public void addMove(@PathParam("gameId") int gameId, MoveDAO move) {
        LOGGER.info("Request POST on StateServlet : Adding a move");
        GameEntity game = chessService.getGame(gameId);
        final Duration duration = game.getCurrentMoveDuration();
        MoveEntity moveEntity = MoveEntity.createMoveEntity(game, move, duration);
        chessService.persist(moveEntity);
    }

    @GET
    @Path("moves")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMoves(@QueryParam("gid") int gameId) {
        LOGGER.info("GET\t/game/moves\tgid={}", gameId);
        List<MoveEntity> moves = chessService.getGame(gameId).getMoves();
        StringBuilder sb = new StringBuilder();
        for (MoveEntity move : moves) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(move);
        }
        return sb.toString();
    }

    @GET
    @Path("moves/last")
    @Produces(MediaType.TEXT_PLAIN)
    public String getLastMove(@QueryParam("gid") int gameId) {
        LOGGER.info("GET\t/game/moves/last\tgid={}", gameId);
        int moveId = chessService.getLastMoveId(gameId);
        MoveEntity move = chessService.getMove(moveId);
        return move.toString();
    }

    @GET
    @Path("{gameId}/clock/black")
    public Duration getBlackRemainingTime(@PathParam("gameId") int gameId) {
        LOGGER.info("GET game/{}/clock/black", gameId);
        GameEntity game = chessService.getGame(gameId);
        return game.getRemainingTime(Side.BLACK, true);
    }

    @GET
    @Path("{gameId}/clock/white")
    public Duration getWhiteRemainingTime(@PathParam("gameId") int gameId) {
        LOGGER.info("GET game/{}/clock/white", gameId);
        GameEntity game = chessService.getGame(gameId);
        return game.getRemainingTime(Side.WHITE, true);
    }
}
