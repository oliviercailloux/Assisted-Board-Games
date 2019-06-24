package io.github.oliviercailloux.assisted_board_games.model.state;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.Instant;

import org.jboss.weld.exceptions.IllegalArgumentException;

import com.github.bhlangonijr.chesslib.Side;

/**
 * 
 * @author theophile
 *
 */
public class PlayerState {

    private Side side;
    /**
     * The time at which the player's turn began.
     */
    private Instant timeAtTurnStart;
    /**
     * The remaining time for the player to play the moment his turn began.
     */
    private Duration remainingTime;

    private PlayerState(Side side, Instant turnStartTime, Duration turnStartClock) {
        requireNonNull(turnStartTime);
        requireNonNull(turnStartClock);
        this.side = side;
        this.timeAtTurnStart = turnStartTime;
        this.remainingTime = turnStartClock;
    }

    public Side getSide() {
        return side;
    }

    public Instant getTimeAtTurnStart() {
        return timeAtTurnStart;
    }

    public Duration getRemainingTime() {
        return remainingTime;
    }

    /**
     * This method computes the effective remaining time available for the player at
     * the given Instant. This Instant must be after the turn has started, otherwise
     * an IllegalArgumentException is raised.
     * 
     * If the return value is below zero, then the given player has consumed all the
     * imparted time and has thus lost the game.
     * 
     * @param instant must be after the turn has started
     * 
     * @return The duration the player still has available for playing at the given
     *         instant.
     */
    public Duration getRemainingTimeAt(Instant instant) {
        if (instant.isBefore(timeAtTurnStart)) {
            throw new IllegalArgumentException("instant cannot be before turn start");
        }
        final Duration timeSpent = Duration.between(timeAtTurnStart, instant);
        return remainingTime.minus(timeSpent);
    }

    public boolean hasLostByTime(Instant instant) {
        return getRemainingTimeAt(instant).isNegative();
    }

    public static PlayerState of(Side side, Instant turnStartTime, Duration turnStartClock) {
        return new PlayerState(side, turnStartTime, turnStartClock);
    }

    /**
     * Instantiates a PlayerState object giving an infinite amount of time for the
     * player to play.
     * 
     * This can be checked by verifying {@code PlayerState#getTimeAtTurnStart()} is
     * always {@code Instant.EPOCH} and that {@code PlayerState#getRemainingTime()}
     * always returns {@code Duration.ofNanos(Long.MAX_VALUE)}.
     * 
     * @param side The side the player is playing.
     * @return The PlayerState object representing an untimed (infinite time) move.
     */
    public static PlayerState of(Side side) {
        return new PlayerState(side, Instant.EPOCH, Duration.ofNanos(Long.MAX_VALUE));
    }
}
