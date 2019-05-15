package io.github.oliviercailloux.assisted_board_games.servlets;

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
import com.github.bhlangonijr.chesslib.move.MoveConversionException;
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
public class HelpServlet {

    private static final Logger LOGGER = Logger.getLogger(HelpServlet.class.getCanonicalName());
    @Inject
    ChessService chessS;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String suggestMove(@QueryParam("game") int idGame)
            throws MoveGeneratorException, MoveConversionException, MoveException {
        LOGGER.info("Request GET on HelpServlet with state :" + idGame);
        GameEntity game = chessS.getGame(idGame);
        List<MoveEntity> moves = game.getMoves();
        Board b = GameHelper.playMoves(moves);
        // Generate possible moves
        final MoveList l = MoveGenerator.generateLegalMoves(b);
        MoveList mated = new MoveList();
        MoveList staleMate = new MoveList();
        for (Move m : l) {
            b.doMove(m);
            if (b.isMated()) {
                mated.add(m);
                l.remove(m);
            }
            if (b.isStaleMate()) {
                staleMate.add(m);
                l.remove(m);
            }
            b.undoMove();
        }
        return "Move to mate opponent : " + mated.toString() + "\n"
                + "Move leading to stalemate : " + staleMate.toString() + "\n"
                + "Other legal move : " + l.toString();
    }
}
