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

@Path("/game")
@RequestScoped
public class GameResourceTest {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(GameResourceTest.class);

	@Context
	UriInfo uriInfo;

	@Inject
	GameServiceTest gameS;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public List<Integer> getMoves() {
		LOGGER.info("Running GET.");
		final List<GameEntity> allGames = gameS.getAll();
		LOGGER.info("Returning {} items.", allGames.size());
		return allGames.stream().map(mv -> mv.getId()).collect(Collectors.toList());
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response postMove() {
		LOGGER.info("Running POST.");
		final GameEntity game = new GameEntity();

		gameS.persist(game);

		LOGGER.info("Redirecting.");
		return Response.seeOther(uriInfo.getAbsolutePath()).build();
	}
}
