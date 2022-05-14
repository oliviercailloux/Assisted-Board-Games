package io.github.oliviercailloux.sample_quarkus_heroku.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
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

@Path("/items")
@RequestScoped
public class ItemResource {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemResource.class);

	@Context
	UriInfo uriInfo;

	@Inject
	ItemService itemS;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public List<Integer> getItems() {
		LOGGER.info("Running GET.");
		final List<MoveEntity> allItems = itemS.getAll();
		LOGGER.info("Returning {} items.", allItems.size());
		return allItems.stream().map(mv -> mv.getId()).collect(Collectors.toList());
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public Response postItem() {
		LOGGER.info("Running POST.");
		final MoveEntity item = new MoveEntity();
		/** Ideally we’d use the client zone here. */
		//final ZonedDateTime zonedTimestamp = ZonedDateTime.now(ZoneId.systemDefault());
		//item.setName("MyItem dated " + zonedTimestamp);

		itemS.persist(item);

		LOGGER.info("Redirecting.");
		return Response.seeOther(uriInfo.getAbsolutePath()).build();
	}
}
