package io.github.oliviercailloux.y2018.assisted_board_games.servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import io.github.oliviercailloux.y2018.assisted_board_games.game.ChessMove;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessGameEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessMoveEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessStateEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.service.ChessService;

/***
 * 
 * @author Andréa Lourenço
 *
 */
@Path("chess")
public class ChessServlet {
	private static final Logger LOGGER = Logger.getLogger(ChessServlet.class.getCanonicalName());

	@PersistenceContext
	private EntityManager em;

	@Inject
	private ChessService chessService;

	@GET
	@Path("game")
	@Produces(MediaType.TEXT_PLAIN)
	public String getGame(@QueryParam("idGame") int idGame,
			@DefaultValue("0") @QueryParam("idState") int idState) throws IOException {
		LOGGER.info(
				"Request GET on ChessServlet with game = " + idGame + " and state = " + idState);

		final ChessGameEntity game = chessService.getGame(idGame);
		Board board = playMoves(game.getStates().get(idState).getMoves());
		return board.toString();

	}

	@GET
	@Path("move")
	@Consumes(MediaType.TEXT_PLAIN)
	public ChessMoveEntity getMove(@QueryParam("idGame") int idGame,
			@DefaultValue("0") @QueryParam("idMove") int idMove) {
		LOGGER.info("Request GET on ChessServlet with move = " + idMove + " and game = " + idGame);
		final ChessStateEntity lastState = chessService.getLastState(idGame);

		return lastState.getMoves().get(lastState.getMoves().size() - 1);
	}

	@POST
	@Path("move")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addMove(@QueryParam("idGame") int idGame, @Encoded JsonObject jsonMove) {
		LOGGER.info("Request POST on StateServlet : Adding a move");

		final ChessMoveEntity move = new ChessMoveEntity();

		Move moveBussiness = ChessMove.decode(jsonMove);
		move.setFrom(moveBussiness.getFrom().toString());
		move.setTo(moveBussiness.getTo().toString());

		chessService.persist(move);
		return Response.ok().build();
	}

	private Board playMoves(List<ChessMoveEntity> allMoves) {
		Board board = new Board();

		for (ChessMoveEntity move : allMoves) {
			board.doMove(new Move(Square.valueOf(move.getFrom()), Square.valueOf(move.getTo())));
		}
		return board;
	}

}
