package io.github.oliviercailloux.assisted_board_games.model;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;

import org.h2.util.StringUtils;

import com.github.bhlangonijr.chesslib.Board;

/**
 * 
 * @author tpiganeau
 *
 */
@JsonbPropertyOrder({ "position", "moves" })
public class GameDAO implements Serializable {

    private Instant startTime;
    private Duration clockDuration;
    private Duration clockIncrement;
    private String position;
    private List<MoveDAO> moves;

    GameDAO(Instant startTime, Duration clockDuration, Duration clockIncrement, String position, List<MoveDAO> moves) {
        if (moves.isEmpty()) {
            throw new IllegalArgumentException("game contains no move");
        }
        this.startTime = startTime == null ? Instant.EPOCH : startTime;
        this.clockDuration = clockDuration == null ? Duration.ofSeconds(Long.MAX_VALUE) : clockDuration;
        this.clockIncrement = clockIncrement == null ? Duration.ZERO : clockIncrement;
        if (StringUtils.isNullOrEmpty(position)) {
            position = new Board().getFen(); // start with initial position
        }
        this.position = position;
        this.moves = moves;
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
