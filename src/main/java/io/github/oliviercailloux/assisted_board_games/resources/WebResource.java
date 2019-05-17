package io.github.oliviercailloux.assisted_board_games.resources;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * 
 * @author mimim
 *
 */
@Path("web")
public class WebResource {

    @GET
    @Path("{path}")
    public Response getResource(@PathParam("path") String path) {
        File file = new File("./src/main/resources/webapp", path);
        if (!file.exists()) {
            return Response.status(404).build();
        }
        return Response.ok().entity(file).build();
    }

    @GET
    @Path("{path}/{file}")
    public Response getResource(@PathParam("path") String path, @PathParam("file") String file) {
        File res = new File("./src/main/resources/webapp/" + path, file);
        if (!res.exists()) {
            return Response.status(404).build();
        }
        return Response.ok().entity(res).build();
    }
}
