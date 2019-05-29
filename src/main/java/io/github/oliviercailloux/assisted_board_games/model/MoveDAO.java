package io.github.oliviercailloux.assisted_board_games.model;

import java.io.Serializable;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;

/**
 * The purpose of this class is to serve as middleware between the MoveEntity
 * representation used by JPA and Hibernate and the API endpoints that require
 * writeable objects to serialize / deserialize.
 * 
 * @author Theophile Dano
 *
 */
@JsonbPropertyOrder({ "from", "to", "promotion" })
public class MoveDAO implements Serializable {

    private Square from;
    private Square to;
    private Piece promotion;

    @JsonbCreator
    public MoveDAO(@JsonbProperty("from") Square from,
                    @JsonbProperty("to") Square to,
                    @JsonbProperty("promotion") Piece promotion) {
        if (from == Square.NONE) {
            throw new IllegalArgumentException("from is NONE");
        }
        if (to == Square.NONE) {
            throw new IllegalArgumentException("to is NONE");
        }
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }

    public Square getFrom() {
        return from;
    }

    public Square getTo() {
        return to;
    }

    public Piece getPromotion() {
        return promotion;
    }
}
