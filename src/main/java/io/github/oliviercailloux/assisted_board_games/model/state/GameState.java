package io.github.oliviercailloux.assisted_board_games.model.state;

import java.util.EnumMap;

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

    private EnumMap<Side, PlayerState> playerStates;
    private Board board;

    private GameState(GameEntity game) {
        playerStates = new EnumMap<>(Side.class);
        playerStates.put(Side.WHITE, PlayerState.createPlayerState(game, Side.WHITE));
        playerStates.put(Side.BLACK, PlayerState.createPlayerState(game, Side.BLACK));
        try {
            board = GameHelper.playMoves(game.getMoves());
        } catch (MoveException e) {
            // this exception can't happen here since the moves were already validated upon
            // insertion
            throw new AssertionError();
        }
    }

    public EnumMap<Side, PlayerState> getPlayers() {
        return playerStates;
    }

    public Board getBoard() {
        return board;
    }

    public PlayerState getPlayer(Side side) {
        return playerStates.get(side);
    }

    public PlayerState getCurrentPlayer() {
        return getPlayer(board.getSideToMove());
    }

    public static GameState createGameState(GameEntity game) {
        Preconditions.checkArgument(game != null);
        return new GameState(game);
    }
}
