package io.github.oliviercailloux.assisted_board_games.model.state;

import java.time.Duration;
import java.time.Instant;

import org.glassfish.jersey.internal.guava.Preconditions;
import org.jboss.weld.exceptions.IllegalArgumentException;

import com.github.bhlangonijr.chesslib.Side;

import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.utils.ClockUtils;

/**
 * 
 * @author theophile
 *
 */
public class PlayerState {

    private Side side;
    private Instant turnStartTime;
    private Duration turnStartClock;

    private PlayerState(Side side, Instant turnStartTime, Duration turnStartClock) {
        this.side = side;
        this.turnStartTime = turnStartTime;
        this.turnStartClock = turnStartClock;
    }

    public Side getSide() {
        return side;
    }

    public Instant getTurnStartTime() {
        return turnStartTime;
    }

    public Duration getClockAtTurnStart() {
        return turnStartClock;
    }

    /**
     * This method computes the effective remaining time available for the player at
     * a given Instant. This Instant must be after the turn has started, otherwise
     * an IllegalArgumentException is raised.
     * 
     * If the return value is below zero, then the given player has consumed all the
     * imparted time and has thus lost the game.
     * 
     * @return The duration the player still has available for playing at the given
     *         instant.
     */
    public Duration getRemainingTimeAt(Instant instant) {
        if (instant.isBefore(turnStartTime)) {
            throw new IllegalArgumentException("instant cannot be before turn start");
        }
        final Duration timeSpent = Duration.between(turnStartTime, instant);
        return turnStartClock.minus(timeSpent);
    }

    public boolean hasLostByTime(Instant instant) {
        return getRemainingTimeAt(instant).isNegative();
    }

    public static PlayerState createPlayerState(GameEntity game, Side side) {
        Preconditions.checkNotNull(game);
        Preconditions.checkNotNull(side);
        final Duration gameDuration = ClockUtils.getGameDuration(game);
        final Instant turnStartTime = game.getStartTime().plus(gameDuration);
        final Duration turnStartClock = ClockUtils.getRemainingTime(game, side);
        return new PlayerState(side, turnStartTime, turnStartClock);
    }
}
