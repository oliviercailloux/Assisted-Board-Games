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

    private GameState(Board board, PlayerState whitePlayer, PlayerState blackPlayer) {
        requireNonNull(board);
        requireNonNull(whitePlayer);
        requireNonNull(blackPlayer);
        this.board = board;
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
        return side == board.getSideToMove();
    }

    public PlayerState getPlayerState(Side side) {
        return playerStates.get(side);
    }

    public PlayerState getCurrentPlayerState() {
        return getPlayerState(board.getSideToMove());
    }

    public static GameState of(String fenPosition, PlayerState whitePlayer, PlayerState blackPlayer) {
        Board board = new Board();
        board.loadFromFen(fenPosition);
        return new GameState(board, whitePlayer, blackPlayer);
    }

    public static GameState of(Board board, PlayerState whitePlayer, PlayerState blackPlayer) {
        return new GameState(board, whitePlayer, blackPlayer);
    }
}
