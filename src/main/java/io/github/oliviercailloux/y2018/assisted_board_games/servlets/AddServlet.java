package io.github.oliviercailloux.y2018.assisted_board_games.servlets;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;

import io.github.oliviercailloux.y2018.assisted_board_games.game.ChessMove;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessGameEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessMoveEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.model.ChessStateEntity;
import io.github.oliviercailloux.y2018.assisted_board_games.service.ChessService;


@Path("/add")
public class AddServlet {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(AddServlet.class.getCanonicalName());

	@PersistenceContext
	private EntityManager em;
	@Inject
	private ChessService chessS;
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response addState(@QueryParam("game") int idGame,@QueryParam("state") String state) throws MoveException {
		LOGGER.info("Request POST on AddServlet with game = " + idGame + " and state = " + state);
		ChessGameEntity game =chessS.getGame(idGame);
		if(game.equals(null)) {
			if (state.equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")){
				
				ChessGameEntity newGame = new ChessGameEntity();
				ChessStateEntity newState = new ChessStateEntity();
				newState.setGame(newGame);
				newGame.addState(newState);
				chessS.persist(newState);
				chessS.persist(newGame);
				return Response.ok().entity("game : "+newGame.getId()+ " state : "+ newState.getId_state()).build();
				
			}return Response.status(Response.Status.BAD_REQUEST).build();
		}else {
				Board a = new Board();
				Board b = new Board();
				b.loadFromFen(state);
				ChessStateEntity lastState = chessS.getLastState(idGame);
				List<ChessMoveEntity> moves =lastState.getMoves();
				for (ChessMoveEntity m : moves) {
					a.doMove(new Move(Square.valueOf(m.getFrom()),Square.valueOf(m.getTo())));
				}
				Move m = ChessMove.getMove(a, b);
				if (m.equals(null)) {
					return Response.status(Response.Status.BAD_REQUEST).build();
					}else {
						ChessMoveEntity mEntity = new ChessMoveEntity(m.getFrom().toString(),m.getTo().toString());
						ChessStateEntity newState = new ChessStateEntity();
						mEntity.setState(newState);
						newState.setMoves(moves);
						newState.addMove(mEntity);
						game.addState(newState);
						chessS.persist(newState);
						chessS.persist(mEntity);
						return Response.ok().entity("game : " +idGame + " state : "+newState.getId_state()).build();	
			}
		}
		
	}

}
