package io.github.oliviercailloux.y2018.assisted_board_games.ressources.servlets;

import java.io.IOException;
import java.util.List;
import java.util.OptionalInt;
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

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.google.common.primitives.Ints;

import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessStateEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessGameEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessMoveEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.ressources.utils.ServletHelper;

@WebServlet("/state")
public class StateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(StateServlet.class.getCanonicalName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOGGER.info("Request GET on StateServlet with game = " + request.getParameter("game") + " and state = " + request.getParameter("state"));

		final OptionalInt gameParam = tryParse("game", request);
		final OptionalInt stateParam = tryParse("state", request);

		if (!gameParam.isPresent()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Exécution impossible, paramètre manquant.");
			return;
		} else {

			final ServletOutputStream out = new ServletHelper().configureAndGetOutputStream(response);

			final EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("Assited-Boards-Servlets");
			final EntityManager em = emFactory.createEntityManager();

			final EntityTransaction transaction = em.getTransaction();
			transaction.begin();

			@SuppressWarnings("unchecked")
			final TypedQuery<ChessGameEntity> query = (TypedQuery<ChessGameEntity>) em.createQuery("SELECT g FROM ChessGameEntity g WHERE id=" + gameParam);

			final ChessGameEntity game = (ChessGameEntity) query.getResultList();

			transaction.commit();

			if (!stateParam.isPresent()) {
				ChessStateEntity lastState = game.getStates().get(game.getStates().size()-1);
				out.println(playMoves(lastState.getMoves()).toString());
				
			} else {
				ChessStateEntity state = game.getStates().get(stateParam.getAsInt());
				out.println(playMoves(state.getMoves()).toString());
			}
			em.close();
		}

	}

	/***
	 * Code From
	 * https://github.com/oliviercailloux/JavaEE-Servlets/blob/additioner/src/main/java/io/github/oliviercailloux/javaee_servlets/servlets/AdditionerServlet.java
	 * 
	 * @Author : Olivier Cailloux
	 */
	private OptionalInt tryParse(String parameterName, HttpServletRequest request) {
		final String parameter = request.getParameter(parameterName);
		final Integer parsed = parameter == null ? null : Ints.tryParse(parameter);

		final OptionalInt param;
		if (parsed == null) {
			param = OptionalInt.empty();
			LOGGER.info("No valid " + parameterName + " in request.");
		} else {
			param = OptionalInt.of(parsed);
		}

		return param;
	}
	
	private Board playMoves(List<ChessMoveEntity> allMoves) {
		Board board = new Board();
		
		for(ChessMoveEntity move : allMoves) {
			board.doMove(new Move(Square.valueOf(move.getFrom()), Square.valueOf(move.getTo())));	
		}
		return board;
	}

}
