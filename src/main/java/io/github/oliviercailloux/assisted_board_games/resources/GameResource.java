package io.github.oliviercailloux.assisted_board_games.resources;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveException;

import io.github.oliviercailloux.assisted_board_games.game.ChessMove;
import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.model.MoveEntity;
import io.github.oliviercailloux.assisted_board_games.service.ChessService;
import io.github.oliviercailloux.assisted_board_games.utils.GameHelper;

@Path("game")
@RequestScoped
public class GameResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameResource.class);
    @Inject
    ChessService chessService;

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
    @Path("move/json")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMove(@QueryParam("gid") int gameId, JsonObject jsonMove) {
        LOGGER.info("GET\t/game/move/json\tgid={}, data={}", gameId, jsonMove);
        final MoveEntity move = MoveEntity.fromMove(ChessMove.decode(jsonMove));
        GameEntity game = chessService.getGame(gameId);
        game.addMove(move);
        chessService.persist(move);
        return Response.ok().build();
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
}
