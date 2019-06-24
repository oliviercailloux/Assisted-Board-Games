package io.github.oliviercailloux.assisted_board_games.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;

import com.github.bhlangonijr.chesslib.Board;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

/**
 * 
 * @author tpiganeau
 *
 */
@JsonbPropertyOrder({ "startTime", "clockDuration", "clockIncrement", "position", "moves" })
public class GameDAO implements Serializable {

    private final Instant startTime;
    private final Duration clockDuration;
    private final Duration clockIncrement;
    private final String position;
    private final ImmutableList<MoveDAO> moves;

    GameDAO(Instant startTime, Duration clockDuration, Duration clockIncrement, String position, List<MoveDAO> moves) {
        this.startTime = startTime == null ? Instant.EPOCH : startTime;
        this.clockDuration = clockDuration == null ? Duration.ofSeconds(Long.MAX_VALUE) : clockDuration;
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
        // TODO
        return null;
    }
}
