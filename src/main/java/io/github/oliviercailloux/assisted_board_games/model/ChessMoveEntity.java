package io.github.oliviercailloux.assisted_board_games.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/***
 * 
 * @author Delmas Douo Bougna
 *
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "ChessMoveEntity.find", query = "SELECT c FROM ChessMoveEntity c WHERE c.id = :id"),
        @NamedQuery(name = "ChessMoveEntity.getLastMoveId", query = "SELECT MAX(c.id) FROM ChessMoveEntity c WHERE c.game.id = :id"),
        @NamedQuery(name = "ChessMoveEntity.getLastMove", query = "SELECT MAX(c.id) FROM ChessMoveEntity  c ") })
public class ChessMoveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String fromSquare;
    private String toSquare;
    @ManyToOne
    private ChessGameEntity game;

    public ChessMoveEntity() {
    }

    public ChessMoveEntity(String from, String to) {
        this.fromSquare = from;
        this.toSquare = to;
    }

    public int getId() {
        return id;
    }

    public String getFrom() {
        return fromSquare;
    }

    public String getTo() {
        return toSquare;
    }

    public void setFrom(String from) {
        this.fromSquare = from;
    }

    public void setTo(String to) {
        this.toSquare = to;
    }

    public void setGame(ChessGameEntity game) {
        this.game = game;
    }

    @Override
    public String toString() {
        return this.getId() + " : from" + getFrom() + "to" + getTo() + "\n";
    }
}
