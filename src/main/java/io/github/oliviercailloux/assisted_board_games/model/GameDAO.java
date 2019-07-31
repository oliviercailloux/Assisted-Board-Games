package io.github.oliviercailloux.assisted_board_games.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import io.github.oliviercailloux.assisted_board_games.model.state.GameState;
import io.github.oliviercailloux.assisted_board_games.model.state.PlayerState;

/**
 * 
 * @author tpiganeau
 *
 */
@JsonbPropertyOrder({ "position", "moves", "startTime", "clockDuration", "clockIncrement" })
public class GameDAO implements Serializable {

    private final Instant startTime;
    private final Duration clockDuration;
    private final Duration clockIncrement;
    private final String position;
    private final ImmutableList<MoveDAO> moves;

    private GameDAO(Instant startTime, Duration clockDuration, Duration clockIncrement, String position,
            List<MoveDAO> moves) {
        this.startTime = startTime == null ? Instant.EPOCH : startTime;
        this.clockDuration = clockDuration == null ? PlayerState.MAX_TURN_DURATION : clockDuration;
        this.clockIncrement = clockIncrement == null ? Duration.ZERO : clockIncrement;
        this.position = Strings.isNullOrEmpty(position) ? new Board().getFen() : position;
        this.moves = ImmutableList.copyOf(moves);
    }

    @JsonbCreator
    public static GameDAO createGameDAO(@JsonbProperty("startTime") Instant startTime,
            @JsonbProperty("clockDuration") Duration clockDuration,
            @JsonbProperty("clockIncrement") Duration clockIncrement,
            @JsonbProperty("position") String position,
            @JsonbProperty("moves") List<MoveDAO> moves) {
        return new GameDAO(startTime, clockDuration, clockIncrement, position, moves);
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Duration getClockDuration() {
        return clockDuration;
    }

    public Duration getClockIncrement() {
        return clockIncrement;
    }

    public String getPosition() {
        return position;
    }

    public List<MoveDAO> getMoves() {
        return moves;
    }

    public GameEntity asGameEntity() {
        final ChessBoard board = ChessBoard.createChessBoard(position);
        final GameState gameState = GameState.of(board, PlayerState.of(Side.WHITE), PlayerState.of(Side.BLACK));
        final GameEntity gameEntity = new GameEntity(gameState, startTime, clockDuration, clockIncrement);
        for (MoveDAO moveDAO : moves) {
            final MoveEntity moveEntity = MoveEntity.createMoveEntity(gameEntity, moveDAO);
            gameEntity.addMove(moveEntity);
        }
        return gameEntity;
    }
}
