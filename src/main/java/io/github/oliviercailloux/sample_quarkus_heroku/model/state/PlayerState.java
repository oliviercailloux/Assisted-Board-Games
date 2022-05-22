package io.github.oliviercailloux.sample_quarkus_heroku.model.state;

import static java.util.Objects.requireNonNull;

import com.github.bhlangonijr.chesslib.Side;
import java.time.Duration;
import java.time.Instant;
import org.jboss.weld.exceptions.IllegalArgumentException;

/**
 * 
 * @author theophile
 *
 */
public class PlayerState {

  /**
   * We use here Long.MAX_VALUE nanoseconds as the maximum amount of time per turn per player. We
   * can't use this same amount of seconds because upon serialization, the representation switches
   * to nanoseconds, causing an overflow since it has to multiply the amount of seconds by 1e+9 to
   * get the amount of nanoseconds.
   */
  public static final Duration MAX_TURN_DURATION = Duration.ofNanos(Long.MAX_VALUE);
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
   * This method computes the effective remaining time available for the player at the given
   * Instant. This Instant must be after the turn has started, otherwise an IllegalArgumentException
   * is raised.
   * 
   * If the return value is below zero, then the given player has consumed all the imparted time and
   * has thus lost the game.
   * 
   * @param instant must be after the turn has started
   * 
   * @return The duration the player still has available for playing at the given instant.
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
   * Instantiates a PlayerState object giving an infinite amount of time for the player to play.
   * 
   * This can be checked by verifying {@code PlayerState#getTimeAtTurnStart()} is always
   * {@code Instant.EPOCH} and that {@code PlayerState#getRemainingTime()} always returns
   * {@code PlayerState.MAX_TURN_DURATION}.
   * 
   * @param side The side the player is playing.
   * @return The PlayerState object representing an untimed (infinite time) move.
   */
  public static PlayerState of(Side side) {
    return new PlayerState(side, Instant.EPOCH, MAX_TURN_DURATION);
  }
}
