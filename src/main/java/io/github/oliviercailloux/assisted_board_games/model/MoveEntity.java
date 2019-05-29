package io.github.oliviercailloux.assisted_board_games.model;

import java.util.Objects;

import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

/***
 * 
 * @author Delmas Douo Bougna
 * @author Theophile Dano
 *
 */
@Entity
@Table(name = "moves")
@NamedQueries({
        @NamedQuery(name = "Move.find", query = "SELECT c FROM MoveEntity c WHERE c.id = :id"),
        @NamedQuery(name = "Move.getLastMoveId", query = "SELECT MAX(c.id) FROM MoveEntity c WHERE c.game.id = :id"),
        @NamedQuery(name = "Move.getLastMove", query = "SELECT MAX(c.id) FROM MoveEntity c ")
})
@JsonbPropertyOrder({ "from", "to", "promotion", "game" })
public class MoveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    Square from;
    Square to;
    Piece promotion;
    @ManyToOne
    GameEntity game;

    public MoveEntity() {
        from = Square.NONE;
        to = Square.NONE;
        promotion = Piece.NONE;
    }

    public MoveEntity(GameEntity game, MoveDAO move) {
        this();
        Objects.requireNonNull(move);
        this.game = Objects.requireNonNull(game);
        this.from = move.getFrom();
        this.to = move.getTo();
        this.promotion = move.getPromotion();
    }

    public int getId() {
        return id;
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

    public GameEntity getGame() {
        return game;
    }

    public static Move asMove(MoveEntity move) {
        return new Move(move.getFrom(), move.getTo(), move.getPromotion());
    }
}
