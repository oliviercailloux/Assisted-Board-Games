package io.github.oliviercailloux.assisted_board_games.model;

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

    MoveEntity() {
        this(Square.NONE, Square.NONE, Piece.NONE);
    }

    MoveEntity(GameEntity game) {
        this();
        this.game = game;
    }

    MoveEntity(String from, String to) {
        this(Square.valueOf(from), Square.valueOf(to));
    }

    MoveEntity(String from, String to, String promotion) {
        this(Square.valueOf(from), Square.valueOf(to), Piece.valueOf(promotion));
    }

    MoveEntity(Square from, Square to) {
        this(from, to, Piece.NONE);
    }

    MoveEntity(Square from, Square to, Piece promotion) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }

    MoveEntity(GameEntity game, Square from, Square to) {
        this(from, to);
        this.game = game;
    }

    MoveEntity(GameEntity game, Square from, Square to, Piece promotion) {
        this(from, to, promotion);
        this.game = game;
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

    // Factory
    public static MoveEntity fromMove(Move move) {
        return new MoveEntity(move.getFrom(), move.getTo(), move.getPromotion());
    }

    public static Move asMove(MoveEntity move) {
        return new Move(move.getFrom(), move.getTo(), move.getPromotion());
    }
}
