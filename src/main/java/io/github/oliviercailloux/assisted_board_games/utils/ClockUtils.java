package io.github.oliviercailloux.assisted_board_games.utils;

import java.time.Duration;
import java.time.LocalTime;
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
        List<LocalTime> moveTimes = moves.stream()
                        .map(MoveEntity::getTime)
                        .collect(Collectors.toList());
        if (moves.size() % 2 == 1) {
            // in this case, it is black turn and we need to add a virtual move time to get
            // the real clock time
            moveTimes.add(LocalTime.now());
        }
        Duration blackMoveDuration;
        Duration leftBlackDuration = game.getDuration();
        for (int i = 1; i < moveTimes.size(); i += 2) { // Si <= 1 élément => ok on rentre pas dans la boucle
            blackMoveDuration = Duration.between(moveTimes.get(i - 1), moveTimes.get(i));
            leftBlackDuration = leftBlackDuration.minus(blackMoveDuration).plus(game.getClockIncrement());
        }
        return leftBlackDuration;
    }

    public static Duration getWhiteRemainingDuration(GameEntity game) {
        List<MoveEntity> moves = game.getMoves();
        List<LocalTime> moveTimes = moves.stream()
                        .map(MoveEntity::getTime)
                        .collect(Collectors.toList());
        if (moves.size() % 2 == 0) {
            // in this case, it is white turn and we need to add a virtual move time to get
            // the real clock time
            moveTimes.add(LocalTime.now());
        }
        Duration whiteMoveDuration;
        Duration leftWhiteDuration = game.getDuration();
        if (!moves.isEmpty()) {
            whiteMoveDuration = Duration.between(game.getStartTime(), moveTimes.get(0));
            leftWhiteDuration = leftWhiteDuration.minus(whiteMoveDuration).plus(game.getClockIncrement());
        }
        for (int i = 2; i < moves.size(); i += 2) { // on entre dans la boucle que si y a 3 éléments
            whiteMoveDuration = Duration.between(moveTimes.get(i - 1), moveTimes.get(i));
            leftWhiteDuration = leftWhiteDuration.minus(whiteMoveDuration).plus(game.getClockIncrement());
        }
        return leftWhiteDuration;
    }
}
