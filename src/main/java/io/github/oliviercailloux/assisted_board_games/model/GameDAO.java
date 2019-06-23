package io.github.oliviercailloux.assisted_board_games.model;

import java.io.Serializable;
import java.util.List;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;

import org.h2.util.StringUtils;

import com.github.bhlangonijr.chesslib.Board;

/**
 * 
 * @author tpiganeau
 *
 */
@JsonbPropertyOrder({ "position", "moves" })
public class GameDAO implements Serializable {

    private String position;
    private List<MoveDAO> moves;

    GameDAO(String position, List<MoveDAO> moves) {
        if (StringUtils.isNullOrEmpty(position)) {
            position = new Board().getFen(); // start with initial position
        }
        this.position = position;
        this.moves = moves;
        if (moves.isEmpty()) {
            throw new IllegalArgumentException("game contains no move");
        }
    }

    @JsonbCreator
    public static GameDAO createGameDAO(@JsonbProperty("position") String position,
            @JsonbProperty("moves") List<MoveDAO> moves) {
        return new GameDAO(position, moves);
    }

    public String getPosition() {
        return position;
    }

    public List<MoveDAO> getMoves() {
        return moves;
    }
}
