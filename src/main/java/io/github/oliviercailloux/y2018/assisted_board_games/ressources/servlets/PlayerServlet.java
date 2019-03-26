package io.github.oliviercailloux.y2018.assisted_board_games.ressources.servlets;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

@Path("board")
public class PlayerServlet{

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(StateServlet.class.getCanonicalName());

	@GET
    protected void getBoard(@Context final HttpServletRequest request, @Context final HttpServletResponse response) throws Exception {
		LOGGER.info("Request GET on PlayerServlet ");
		Board board = new Board();
		String boardString = board.toString();
		String cleanBoard = boardString.replaceAll("\n", "<br/>");

        request.setAttribute("board", cleanBoard);
        request.getRequestDispatcher("/PlayerView.jsp").forward(request, response);
    }
	
	@POST
	protected void play(@Context final HttpServletRequest request) {
		String startSquare = request.getParameter("start");
		String arrivalSquare = request.getParameter("end");
		
		Square start = Square.fromValue(startSquare);
		Square end = Square.fromValue(arrivalSquare);
		Board board = new Board();
		board.doMove(new Move(start, end));
	}
	
}
