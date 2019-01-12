package io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.ressources.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.ressources.exception.MissingArgumentException;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.ressources.utils.QueryHelper;
import io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.ressources.utils.ServletHelper;

@WebServlet("/state")
public class StateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(StateServlet.class.getCanonicalName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LOGGER.info("Request GET on StateServlet with game = " + request.getParameter("game") + " and state = "
				+ request.getParameter("state"));

		try {
			if (request.getParameter("game") == null) {
				throw new IllegalArgumentException();
			} else {
				String stateToSend = "le json à récupérer";
				
				final ServletOutputStream out = new ServletHelper().configureAndGetOutputStream(response);

				final EntityManagerFactory emFactory = Persistence
						.createEntityManagerFactory("Assited-Boards-Servlets");
				final EntityManager em = emFactory.createEntityManager();

				final QueryHelper queryHelper = new QueryHelper();
				queryHelper.setEmFactory(emFactory);

				final EntityTransaction transaction = em.getTransaction();
				transaction.begin();
				
				final TypedQuery<ChessGameEntity> query = em.createQuery(
						"SELECT g FROM ChessGameEntity g WHERE id=" + request.getParameter("game"),
						ChessGameEntity.class);		
				
				final ChessGameEntity game = query.getResult();
				
				transaction.commit();
				
				if (request.getParameter("state") == null) {
					// recupérer le dernier etat du game
					out.println(game.getStates().getLast().getStateJson());

				} else {
					// récupérer letat state de la partie game
					try {
						boolean flag = false;
						List<ChessStateEntity> allStates = game.getStates(); 
						for (ChessStateEntity state : allStates) {
							if(state.getId() == request.getParameter("state")) {
								flag = true;
								out.println(state.getStateJson());
							}
						}
						if(!flag) {
							throw new IllegalArgumentException();
						}

					} catch(IllegalArgumentException e) {
						LOGGER.info("Request GET on StateServlet ends with 400 BAD REQUEST: invalid argument");
						response.setStatus(400);
						response.getWriter().append("Missing argument");
					}
				}
								
				em.close();
			}

		} catch (IllegalArgumentException e) {
			LOGGER.info("Request GET on StateServlet ends with 400 : missing argument exception ");
			response.setStatus(400);
			response.getWriter().append("Missing argument");
		}

	}

}
