package io.github.oliviercailloux.assisted_board_games.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.github.bhlangonijr.chesslib.Side;

import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.model.MoveEntity;

/**
 * 
 * @author MarylineChen
 *
 */
public class ClockUtils {

    private ClockUtils() {
    }

    public static Duration getGameDuration(GameEntity game) {
        return game.getMoves().stream()
                .map(MoveEntity::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public static Duration getCurrentMoveDuration(GameEntity game) {
        final Instant now = Instant.now();
        final Duration gameDuration = getGameDuration(game);
        final Instant lastMove = game.getStartTime().plus(gameDuration);
        return Duration.between(lastMove, now);
    }

    public static Duration getRemainingTime(GameEntity game, Side side) {
        final IntPredicate turnOf = i -> i % 2 == (side == Side.WHITE ? 0 : 1);
        Duration timeLeft = game.getClockDuration();
        final List<Duration> moveDurations = game.getMoves()
                .stream()
                .map(MoveEntity::getDuration)
                .collect(Collectors.toList());
        final List<Duration> blackMoveDurations = IntStream.range(0, moveDurations.size())
                .filter(turnOf)
                .mapToObj(moveDurations::get)
                .collect(Collectors.toList());
        final Duration playedDuration = blackMoveDurations.stream().reduce(Duration.ZERO, Duration::plus);
        timeLeft = timeLeft.minus(playedDuration);
        final Duration incrementDuration = game.getClockIncrement().multipliedBy(blackMoveDurations.size());
        timeLeft = timeLeft.plus(incrementDuration);
        if (turnOf.test(moveDurations.size())) {
            final Duration currentMoveDuration = getCurrentMoveDuration(game);
            timeLeft = timeLeft.minus(currentMoveDuration);
        }
        return timeLeft;
    }
}
