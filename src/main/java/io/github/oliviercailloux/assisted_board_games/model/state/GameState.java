package io.github.oliviercailloux.assisted_board_games.model.state;

import static java.util.Objects.requireNonNull;

import java.util.EnumMap;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;

/**
 * 
 * @author theophile
 *
 */
public class GameState {

    private EnumMap<Side, PlayerState> playerStates;
    private Board board;
    private Side sideToMove;

    private GameState(Board board, Side sideToMove, PlayerState whitePlayer, PlayerState blackPlayer) {
        requireNonNull(board);
        requireNonNull(sideToMove);
        requireNonNull(whitePlayer);
        requireNonNull(blackPlayer);
        this.board = board;
        this.sideToMove = sideToMove;
        this.playerStates = new EnumMap<>(Side.class);
        playerStates.put(Side.WHITE, whitePlayer);
        playerStates.put(Side.BLACK, blackPlayer);
    }

    public EnumMap<Side, PlayerState> getPlayerStates() {
        return playerStates;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isSideToMove(Side side) {
        return side == sideToMove;
    }

    public PlayerState getPlayerState(Side side) {
        return playerStates.get(side);
    }

    public PlayerState getCurrentPlayerState() {
        return getPlayerState(sideToMove);
    }

    public static GameState of(Board board, Side sideToMove, PlayerState whitePlayer, PlayerState blackPlayer) {
        return new GameState(board, sideToMove, whitePlayer, blackPlayer);
    }
}
