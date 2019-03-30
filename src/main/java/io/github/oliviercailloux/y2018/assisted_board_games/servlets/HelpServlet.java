package io.github.oliviercailloux.y2018.assisted_board_games.servlets;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveConversionException;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;

/***
 * 
 * @author Megan Brassard
 *
 */
@Path("help")
public class HelpServlet {

	private static final Logger LOGGER = Logger.getLogger(HelpServlet.class.getCanonicalName());

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String suggestMove(@QueryParam("state") String s)
			throws MoveGeneratorException, MoveConversionException {
		LOGGER.info("Request GET on HelpServlet with state :" + s);
		Board b = new Board();
		b.loadFromFen(s);
		// Generate possible moves
		final MoveList l = MoveGenerator.generateLegalMoves(b);
		return l.toSan();
	}
}
