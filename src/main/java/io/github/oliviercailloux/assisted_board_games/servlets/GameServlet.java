package io.github.oliviercailloux.assisted_board_games.servlets;

import java.util.List;
import java.util.logging.Logger;

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

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;

import io.github.oliviercailloux.assisted_board_games.game.ChessMove;
import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.model.MoveEntity;
import io.github.oliviercailloux.assisted_board_games.service.ChessService;
import io.github.oliviercailloux.assisted_board_games.utils.GameHelper;

@Path("/game")
@RequestScoped
public class GameServlet {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(GameServlet.class.getCanonicalName());
    @Inject
    ChessService chessS;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String createGame() {
        LOGGER.info("Request GET on GameServlet : Adding a new game");
        GameEntity game = new GameEntity();
        chessS.persist(game);
        return String.valueOf(chessS.getLastGameId());
    }

    @GET
    @Path("getGame")
    @Produces(MediaType.TEXT_PLAIN)
    public String getGame(@QueryParam("game") int idGame) throws MoveException {
        LOGGER.info("Request GET on GameServlet : Returning game :" + idGame);
        GameEntity game = chessS.getGame(idGame);
        List<MoveEntity> moves = game.getMoves();
        Board b = GameHelper.playMoves(moves);
        return b.toString();
    }

    @GET
    @Path("addMove")
    @Produces(MediaType.TEXT_PLAIN)
    public String addMove(@QueryParam("game") int idGame, @QueryParam("from") String from, @QueryParam("to") String to)
            throws MoveException {
        LOGGER.info("Request POST on GameServlet : Adding a move to game :" + idGame + " with from = " + from
                + " with to = " + to);
        GameEntity game = chessS.getGame(idGame);
        MoveEntity move = new MoveEntity(from, to);
        game.addMove(move);
        move.setGame(game);
        List<MoveEntity> moves = game.getMoves();
        Board b = GameHelper.playMoves(moves);
        chessS.persist(move);
        return b.toString();
    }

    @POST
    @Path("addMove/json")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMove(@QueryParam("idGame") int idGame, JsonObject jsonMove) {
        LOGGER.info("Request POST on StateServlet : Adding a move");
        final MoveEntity move = new MoveEntity();
        Move moveBussiness = ChessMove.decode(jsonMove);
        move.setFrom(moveBussiness.getFrom());
        move.setTo(moveBussiness.getTo());
        move.setPromotion(moveBussiness.getPromotion());
        GameEntity game = chessS.getGame(idGame);
        game.addMove(move);
        move.setGame(game);
        chessS.persist(move);
        return Response.ok().build();
    }

    @GET
    @Path("getMoves")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMoves(@QueryParam("game") int idGame) {
        LOGGER.info("Request GET on GameServlet : Returning moves for game :" + idGame);
        List<MoveEntity> moves = chessS.getGame(idGame).getMoves();
        String movesStr = "";
        for (MoveEntity move : moves) {
            movesStr += move.toString();
        }
        return movesStr;
    }

    @GET
    @Path("getLastMove")
    @Produces(MediaType.TEXT_PLAIN)
    public String getLastMove(@QueryParam("game") int idGame) {
        LOGGER.info("Request GET on GameServlet : Returning last move : for game :" + idGame);
        int idMove = chessS.getLastMoveId(idGame);
        MoveEntity move = chessS.getMove(idMove);
        return move.toString();
    }
}
