package io.github.oliviercailloux.assisted_board_games.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;

import io.github.oliviercailloux.assisted_board_games.model.state.GameState;
import io.github.oliviercailloux.assisted_board_games.model.state.PlayerState;
import io.github.oliviercailloux.assisted_board_games.utils.GameHelper;

/***
 * 
 * @author Delmas Douo Bougna
 * @author Theophile Dano
 *
 */
@Entity
@Table(name = "games")
@NamedQueries({
        @NamedQuery(name = "Game.find", query = "SELECT e FROM GameEntity e WHERE e.id = :id"),
        @NamedQuery(name = "Game.getLastGameId", query = "SELECT MAX(e.id) FROM GameEntity e")
})
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @CreationTimestamp
    Instant startTime;
    /**
     * Duration of the game for each side, by default 30 minutes.
     */
    Duration clockDuration;
    /**
     * Duration of the move increment, by default 10 seconds.
     */
    Duration clockIncrement;
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    List<MoveEntity> moves;

    public GameEntity() {
        clockDuration = Duration.ofSeconds(1800);
        clockIncrement = Duration.ofSeconds(10);
    }

    public int getId() {
        return id;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public List<MoveEntity> getMoves() {
        return moves;
    }

    public MoveEntity getLastMove() {
        return moves.get(moves.size() - 1);
    }

    public Duration getClockDuration() {
        return clockDuration;
    }

    public Duration getClockIncrement() {
        return clockIncrement;
    }

    private Duration getGameDuration() {
        return moves.stream()
                .map(MoveEntity::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public Duration getCurrentMoveDuration() {
        final Instant now = Instant.now();
        final Duration gameDuration = getGameDuration();
        final Instant lastMove = startTime.plus(gameDuration);
        return Duration.between(lastMove, now);
    }

    public Duration getRemainingTime(Side side, boolean withCurrentTurn) {
        final IntPredicate turnOf = i -> i % 2 == (side == Side.WHITE ? 0 : 1);
        Duration timeLeft = clockDuration;
        final List<Duration> moveDurations = moves.stream()
                .map(MoveEntity::getDuration)
                .collect(Collectors.toList());
        final List<Duration> blackMoveDurations = IntStream.range(0, moveDurations.size())
                .filter(turnOf)
                .mapToObj(moveDurations::get)
                .collect(Collectors.toList());
        final Duration playedDuration = blackMoveDurations.stream().reduce(Duration.ZERO, Duration::plus);
        timeLeft = timeLeft.minus(playedDuration);
        final Duration incrementDuration = clockIncrement.multipliedBy(blackMoveDurations.size());
        timeLeft = timeLeft.plus(incrementDuration);
        if (withCurrentTurn && turnOf.test(moveDurations.size())) {
            final Duration currentMoveDuration = getCurrentMoveDuration();
            timeLeft = timeLeft.minus(currentMoveDuration);
        }
        return timeLeft;
    }

    public Duration getRemainingTime(Side side) {
        return getRemainingTime(side, false);
    }

    public GameState asGameState() {
        final Board board;
        try {
            board = GameHelper.playMoves(moves);
        } catch (Exception e) {
            // this exception can't happen here since the moves were already validated upon
            // insertion
            throw new AssertionError("moves should have been validated");
        }
        final Duration gameDuration = getGameDuration();
        final Instant whiteTimeAtTurnStart = startTime.plus(gameDuration);
        final Duration whiteRemainingTime = getRemainingTime(Side.WHITE);
        final PlayerState whitePlayer = PlayerState.of(Side.WHITE, whiteTimeAtTurnStart, whiteRemainingTime);
        final Instant blackTimeAtTurnStart = startTime.plus(gameDuration);
        final Duration blackRemainingTime = getRemainingTime(Side.BLACK);
        final PlayerState blackPlayer = PlayerState.of(Side.BLACK, blackTimeAtTurnStart, blackRemainingTime);
        return GameState.of(board, whitePlayer, blackPlayer);
    }

    public List<PlayerState> getPlayerStateList() {
        final List<PlayerState> playerStates = new ArrayList<>();
        Instant time = startTime;
        Duration[] remainingTimes = new Duration[] { clockDuration, clockDuration };
        for (int i = 0; i < moves.size(); i++) {
            MoveEntity move = moves.get(i);
            Side side = i % 2 == 0 ? Side.WHITE : Side.BLACK;
            playerStates.add(PlayerState.of(side, time, remainingTimes[i % 2]));
            remainingTimes[i % 2] = remainingTimes[i % 2].minus(move.getDuration());
            remainingTimes[i % 2] = remainingTimes[i % 2].plus(clockIncrement);
            time = time.plus(move.getDuration());
        }
        return playerStates;
    }
}
