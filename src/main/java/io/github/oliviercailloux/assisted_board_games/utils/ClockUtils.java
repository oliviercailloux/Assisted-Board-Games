package io.github.oliviercailloux.assisted_board_games.utils;

import java.time.Duration;
import java.util.List;

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
        Duration blackMoveDuration;
        Duration leftBlackDuration = game.getDuration();
        for (int i = 1; i < moves.size(); i += 2) { // Si <= 1 élément => ok on rentre pas dans la boucle
            blackMoveDuration = Duration.between(moves.get(i - 1).getTime(), moves.get(i).getTime());
            leftBlackDuration = leftBlackDuration.minus(blackMoveDuration).plus(game.getClockIncrement());
        }
        return leftBlackDuration;
    }

    public static Duration getWhiteRemainingDuration(GameEntity game) {
        List<MoveEntity> moves = game.getMoves();
        Duration whiteMoveDuration;
        Duration leftWhiteDuration = game.getDuration();
        if (!moves.isEmpty()) {
            whiteMoveDuration = Duration.between(game.getStartTime(), moves.get(0).getTime());
            leftWhiteDuration = leftWhiteDuration.minus(whiteMoveDuration).plus(game.getClockIncrement());
        }
        for (int i = 2; i < moves.size(); i += 2) { // on entre dans la boucle que si y a 3 éléments
            whiteMoveDuration = Duration.between(moves.get(i - 1).getTime(), moves.get(i).getTime());
            leftWhiteDuration = leftWhiteDuration.minus(whiteMoveDuration).plus(game.getClockIncrement());
        }
        return leftWhiteDuration;
    }
}
