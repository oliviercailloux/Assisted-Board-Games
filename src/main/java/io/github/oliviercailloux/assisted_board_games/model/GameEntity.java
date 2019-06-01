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
    int id;
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    List<MoveEntity> moves;

    public int getId() {
        return id;
    }

    public List<MoveEntity> getMoves() {
        return moves;
    }

    public MoveEntity getLastMove() {
        return moves.get(moves.size() - 1);
    }

    public void addMove(MoveEntity move) {
        moves.add(move);
    }
}
