package io.github.oliviercailloux.y2018.assisted_board_games.ressources.servlets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

/***
 * 
 * @author Andréa Lourenço
 *
 */
@RunWith(Arquillian.class)
public class StateServletTest {
	

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(StateServletTest.class.getCanonicalName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		final WebArchive war = ShrinkWrap.create(WebArchive.class, "mychessgame.war").addPackages(true, StateServletTest.class.getPackage());
		return war;
	}

	@ArquillianResource
	private URL baseURL;

	@Test
	public void testStateServlet() throws Exception {
		final Client client = ClientBuilder.newClient();
		final WebTarget target = client.target(baseURL.toString()).path("/state").queryParam("game", 2);
		LOGGER.info(target.getUri().toString());
		final String result = target.request(MediaType.APPLICATION_JSON).get(String.class);
		
		assertEquals("chessGame", result);
		client.close();
	}

}
