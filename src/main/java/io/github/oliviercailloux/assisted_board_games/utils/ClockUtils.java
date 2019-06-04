package io.github.oliviercailloux.assisted_board_games.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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

    public static Duration getBlackRemainingDuration(GameEntity game) {
        List<MoveEntity> moves = game.getMoves();
        List<Instant> moveTimes = moves.stream()
                .map(MoveEntity::getTime)
                .collect(Collectors.toList());
        if (moves.size() % 2 == 1) {
            // in this case, it is black turn and we need to add a virtual move time to get
            // the real clock time
            moveTimes.add(Instant.now());
        }
        Duration timeLeft = game.getDuration();
        for (int i = 0; i < moveTimes.size() - 1; i += 2) {
            Instant whiteTime = moveTimes.get(i);
            Instant blackTime = moveTimes.get(i + 1); // this works because we made sure the list contained an odd
                                                      // amount of moves by adding a virtual one
            Duration moveDuration = Duration.between(whiteTime, blackTime);
            timeLeft = timeLeft.minus(moveDuration);
            // increment only when the move is not virtual (i.e. it is black's turn)
            if (i != moves.size() - 1) {
                timeLeft = timeLeft.plus(game.getClockIncrement());
            }
        }
        return timeLeft;
    }

    public static Duration getWhiteRemainingDuration(GameEntity game) {
        List<MoveEntity> moves = game.getMoves();
        List<Instant> moveTimes = moves.stream()
                .map(MoveEntity::getTime)
                .collect(Collectors.toList());
        Duration timeLeft = game.getDuration();
        if (moves.size() % 2 == 0) {
            // in this case, it is white turn and we need to add a virtual move time to get
            // the real clock time
            moveTimes.add(Instant.now());
        }
        for (int i = 0; i < moveTimes.size(); i += 2) {
            Instant whiteTime = moveTimes.get(i);
            Instant blackTime = i == 0 ? game.getStartTime() : moveTimes.get(i - 1);
            Duration moveDuration = Duration.between(blackTime, whiteTime);
            timeLeft = timeLeft.minus(moveDuration);
            // increment only when the move is not virtual (i.e. it is white's turn)
            if (i != moves.size()) {
                timeLeft = timeLeft.plus(game.getClockIncrement());
            }
        }
        return timeLeft;
    }
}
