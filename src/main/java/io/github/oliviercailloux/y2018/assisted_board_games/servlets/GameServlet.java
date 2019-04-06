package io.github.oliviercailloux.y2018.assisted_board_games.servlets;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.FormParam;
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


import io.github.oliviercailloux.y2018.assisted_board_games.game.ChessMove;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessGameEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessMoveEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.service.ChessService;
import io.github.oliviercailloux.y2018.assisted_board_games.utils.GameHelper;

@Path("/game")
public class GameServlet {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(GameServlet.class.getCanonicalName());

	@PersistenceContext
	private EntityManager em;

	@Inject
	private ChessService chessS;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String createGame() {
		LOGGER.info("Request GET on GameServlet : Adding a new game");
		ChessGameEntity game = new ChessGameEntity();
		chessS.persist(game);

		return String.valueOf(chessS.getLastGameId());
	}

	@GET
	@Path("getGame")
	@Produces(MediaType.TEXT_PLAIN)
	public String getGame(@QueryParam("game") int idGame) throws MoveException {
		LOGGER.info("Request GET on GameServlet : Returning game :" + idGame);
		ChessGameEntity game = chessS.getGame(idGame);
		List<ChessMoveEntity> moves = game.getMoves();
		Board b = GameHelper.playMoves(moves);
		return b.toString();

	}

	@POST
	@Path("addMove")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String addMove(@QueryParam("game") int idGame, @FormParam("from") String from,
			@FormParam("to") String to) throws MoveException {
		LOGGER.info("Request POST on GameServlet : Adding a move to game :" + idGame
				+ " with from = " + from + " with to = " + to);
		ChessGameEntity game = chessS.getGame(idGame);
		ChessMoveEntity move = new ChessMoveEntity(from, to);
		game.addMove(move);
		move.setGame(game);

		List<ChessMoveEntity> moves = game.getMoves();
		Board b = GameHelper.playMoves(moves);
		chessS.persist(move);
		return b.toString();
	}

	@POST
	@Path("addMove/json")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addMove(@QueryParam("idGame") int idGame, @Encoded JsonObject jsonMove) {
		LOGGER.info("Request POST on StateServlet : Adding a move");

		final ChessMoveEntity move = new ChessMoveEntity();

		Move moveBussiness = ChessMove.decode(jsonMove);
		move.setFrom(moveBussiness.getFrom().toString());
		move.setTo(moveBussiness.getTo().toString());
		
		ChessGameEntity game = chessS.getGame(idGame);
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
		List<ChessMoveEntity> moves = chessS.getGame(idGame).getMoves();
		String movesStr = "";
		for (ChessMoveEntity move : moves) {
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
		ChessMoveEntity move = chessS.getMove(idMove);
		return move.toString();
	}
}
