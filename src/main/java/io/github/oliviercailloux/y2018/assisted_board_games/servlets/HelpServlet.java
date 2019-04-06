package io.github.oliviercailloux.y2018.assisted_board_games.servlets;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveConversionException;
import com.github.bhlangonijr.chesslib.move.MoveException;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;

import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessGameEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessMoveEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.service.ChessService;
import io.github.oliviercailloux.y2018.assisted_board_games.utils.GameHelper;

/***
 * 
 * @author Megan Brassard
 *
 */
@Path("/help")
public class HelpServlet {

	private static final Logger LOGGER = Logger.getLogger(HelpServlet.class.getCanonicalName());

	@Inject
	private ChessService chessS;
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String suggestMove(@QueryParam("game") int idGame)
			throws MoveGeneratorException, MoveConversionException, MoveException {
		
		LOGGER.info("Request GET on HelpServlet with state :" + idGame);
		System.out.println("suggesssst");
		ChessGameEntity game= chessS.getGame(idGame);
		List<ChessMoveEntity> moves=game.getMoves();
		Board b= GameHelper.playMoves(moves);
		// Generate possible moves
		final MoveList l = MoveGenerator.generateLegalMoves(b);
		return l.toSan();
	}
}
