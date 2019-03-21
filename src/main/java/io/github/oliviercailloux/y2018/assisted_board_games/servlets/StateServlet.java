package io.github.oliviercailloux.y2018.assisted_board_games.servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
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
import io.github.oliviercailloux.y2018.assisted_board_games.service.ChessService;


/***
 * 
 * @author Andréa Lourenço
 *
 */
@Path("game")
public class StateServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(StateServlet.class.getCanonicalName());
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private ChessService chessService;

	@Inject
	private ChessMove chessMove;
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getGame (@QueryParam("idGame") int idGame, @DefaultValue("0") @QueryParam("idState") int idState) throws IOException {
		LOGGER.info("Request GET on StateServlet with game = " + idGame + " and state = " + idState);

		final ChessGameEntity game = chessService.getGame(idGame);		
		Board board = playMoves(game.getStates().get(idState).getMoves());		
		return board.toString();

	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addGame(@Encoded String req) {
		LOGGER.info("Request POST on StateServlet : Adding a comment");
		// Créer un jeu et son identifiant à partir d’une liste d’états -
		final ChessGameEntity chessGame = new ChessGameEntity();
		
		
		
		return Response.ok().build();		
	}
	
	
	
	private Board playMoves(List<ChessMoveEntity> allMoves) {
		Board board = new Board();
		
		for(ChessMoveEntity move : allMoves) {
			board.doMove(new Move(Square.valueOf(move.getFrom()), Square.valueOf(move.getTo())));	
		}
		return board;
	}
	
}
