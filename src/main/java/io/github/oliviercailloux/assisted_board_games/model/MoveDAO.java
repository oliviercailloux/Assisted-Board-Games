package io.github.oliviercailloux.assisted_board_games.model;

import java.io.Serializable;

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
@JsonbPropertyOrder({ "gameId", "from", "to", "promotion" })
public class MoveDAO implements Serializable {

    private int gameId;
    private Square from;
    private Square to;
    private Piece promotion;

    public MoveDAO() {
        this.from = Square.NONE;
        this.to = Square.NONE;
        this.promotion = Piece.NONE;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Square getFrom() {
        return from;
    }

    public void setFrom(Square from) {
        this.from = from;
    }

    public Square getTo() {
        return to;
    }

    public void setTo(Square to) {
        this.to = to;
    }

    public Piece getPromotion() {
        return promotion;
    }

    public void setPromotion(Piece promotion) {
        this.promotion = promotion;
    }
}
