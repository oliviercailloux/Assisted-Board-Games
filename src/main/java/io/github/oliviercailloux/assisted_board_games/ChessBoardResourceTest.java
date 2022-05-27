package io.github.oliviercailloux.assisted_board_games;

import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/chessboard")
@RequestScoped
public class ChessBoardResourceTest {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ChessBoardResourceTest.class);

	@Context
	UriInfo uriInfo;

	@Inject
	ChessBoardServiceTest chessS;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public List<Integer> getMoves() {
		LOGGER.info("Running GET.");
		final List<ChessBoard> allChess = chessS.getAll();
		LOGGER.info("Returning {} items.", allChess.size());
		return allChess.stream().map(mv -> mv.getId()).collect(Collectors.toList());
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response postMove() {
		LOGGER.info("Running POST.");
		final ChessBoard chess = new ChessBoard();

		chessS.persist(chess);

		LOGGER.info("Redirecting.");
		return Response.seeOther(uriInfo.getAbsolutePath()).build();
	}
}
