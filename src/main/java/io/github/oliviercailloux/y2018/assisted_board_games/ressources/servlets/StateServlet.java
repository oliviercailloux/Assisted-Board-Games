package io.github.oliviercailloux.y2018.assisted_board_games.ressources.servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessStateEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessGameEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.ressources.utils.ServletHelper;

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
				
				final ServletOutputStream out = new ServletHelper().configureAndGetOutputStream(response);

				final EntityManagerFactory emFactory = Persistence
						.createEntityManagerFactory("Assited-Boards-Servlets");
				final EntityManager em = emFactory.createEntityManager();

				final EntityTransaction transaction = em.getTransaction();
				transaction.begin();
				
				@SuppressWarnings("unchecked")
				final TypedQuery<ChessGameEntity> query = (TypedQuery<ChessGameEntity>) em.createQuery("SELECT g FROM ChessGameEntity g WHERE id=" + request.getParameter("game"));		
				
				final ChessGameEntity game = (ChessGameEntity) query.getResultList();
				
				transaction.commit();
				
				if (request.getParameter("state") == null) {
					out.println(game.getStates().get(game.getStates().size()-1).getJsonState()); 
				} else {
					try {
						boolean flag = false;
						List<ChessStateEntity> allStates = game.getStates(); 
						
						for (ChessStateEntity state : allStates) {
							if(state.getId() == Integer.parseInt(request.getParameter("state"))) {
								flag = true;
								out.println(state.getJsonState());
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
