package io.github.oliviercailloux.assisted_board_games.model.state;

import java.util.EnumMap;
import java.util.Map;

import org.glassfish.jersey.internal.guava.Preconditions;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.MoveException;

import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.utils.GameHelper;

/**
 * 
 * @author theophile
 *
 */
public class GameState {

    private EnumMap<Side, PlayerState> players;
    private Board board;

    private GameState(GameEntity game) {
        players = new EnumMap<>(Side.class);
        players.put(Side.WHITE, PlayerState.createPlayerState(game, Side.WHITE));
        players.put(Side.BLACK, PlayerState.createPlayerState(game, Side.BLACK));
        try {
            board = GameHelper.playMoves(game.getMoves());
        } catch (MoveException e) {
            // this exception can't happen here since the moves were already validated upon
            // insertion
        }
    }

    public Map<Side, PlayerState> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public PlayerState getPlayer(Side side) {
        return players.get(side);
    }

    public PlayerState getPlayerToMove() {
        return getPlayer(board.getSideToMove());
    }

    public static GameState createGameState(GameEntity game) {
        Preconditions.checkArgument(game != null);
        return new GameState(game);
    }
}
