package io.github.oliviercailloux.assisted_board_games.model.state;

import java.time.Duration;

import org.glassfish.jersey.internal.guava.Preconditions;

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
    private Duration clock;

    PlayerState(Side side, Duration clock) {
        this.side = side;
        this.clock = clock;
    }

    public Side getSide() {
        return side;
    }

    public Duration getClock() {
        return clock;
    }

    public static PlayerState createPlayerState(GameEntity game, Side side) {
        Preconditions.checkArgument(game != null);
        Preconditions.checkArgument(side != null);
        final Duration clock = ClockUtils.getRemainingTime(game, side);
        return new PlayerState(side, clock);
    }
}
