package io.github.oliviercailloux.assisted_board_games.resources;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveException;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;

import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.model.MoveEntity;
import io.github.oliviercailloux.assisted_board_games.service.ChessService;
import io.github.oliviercailloux.assisted_board_games.utils.GameHelper;

/***
 * 
 * @author Megan Brassard
 *
 */
@Path("/help")
@RequestScoped
public class HelpResource {

    private static final Logger LOGGER = Logger.getLogger(HelpResource.class.getCanonicalName());
    @Inject
    ChessService chessService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String suggestMove(@QueryParam("gid") int gameId) throws MoveGeneratorException, MoveException {
        LOGGER.info("Request GET on HelpServlet with state :" + gameId);
        GameEntity game = chessService.getGame(gameId);
        List<MoveEntity> moves = game.getMoves();
        Board board = GameHelper.playMoves(moves);
        // Generate possible moves
        final MoveList moveList = MoveGenerator.generateLegalMoves(board);
        MoveList mated = new MoveList();
        MoveList staleMate = new MoveList();
        for (Move move : moveList) {
            board.doMove(move);
            if (board.isMated()) {
                mated.add(move);
                moveList.remove(move);
            }
            if (board.isStaleMate()) {
                staleMate.add(move);
                moveList.remove(move);
            }
            board.undoMove();
        }
        return "Move to mate opponent : " + mated.toString() + "\n"
                + "Move leading to stalemate : " + staleMate.toString() + "\n"
                + "Other legal move : " + moveList.toString();
    }
}
