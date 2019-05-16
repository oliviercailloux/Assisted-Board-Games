package io.github.oliviercailloux.assisted_board_games.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    private int id;
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private List<MoveEntity> moves;

    public void addMove(MoveEntity move) {
        move.setGame(this);
        moves.add(move);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setMoves(List<MoveEntity> moves) {
        this.moves = moves;
    }

    public List<MoveEntity> getMoves() {
        return moves;
    }

    public MoveEntity getLastMoves() {
        return moves.get(moves.size() - 1);
    }
}
