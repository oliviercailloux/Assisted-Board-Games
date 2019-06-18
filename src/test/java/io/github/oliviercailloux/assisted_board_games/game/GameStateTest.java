package io.github.oliviercailloux.assisted_board_games.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;

import io.github.oliviercailloux.assisted_board_games.model.GameEntity;
import io.github.oliviercailloux.assisted_board_games.model.MoveDAO;
import io.github.oliviercailloux.assisted_board_games.model.MoveEntity;
import io.github.oliviercailloux.assisted_board_games.model.state.GameState;
import io.github.oliviercailloux.assisted_board_games.model.state.PlayerState;

/**
 * 
 * @author theophile
 *
 */
class GameStateTest {

    @Test
    void testGameStateCreation() {
        GameEntity gameEntity = new GameEntity();
        GameState gameState = gameEntity.getGameState();
        PlayerState white = gameState.getPlayerState(Side.WHITE);
        assertEquals(gameEntity.getClockDuration(), white.getRemainingTime());
        PlayerState black = gameState.getPlayerState(Side.BLACK);
        assertEquals(gameEntity.getClockDuration(), black.getRemainingTime());
        //
        MoveDAO moveDAO = MoveDAO.createMoveDAO(Square.E4, Square.E2, Piece.NONE);
        MoveEntity moveEntity = MoveEntity.createMoveEntity(gameEntity, moveDAO, Duration.ofSeconds(8));
        gameEntity.addMove(moveEntity);
        fail("Not yet implemented");
    }
}
