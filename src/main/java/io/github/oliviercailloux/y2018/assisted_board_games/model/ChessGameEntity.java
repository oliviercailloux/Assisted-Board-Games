package io.github.oliviercailloux.y2018.assisted_board_games.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/***
 * 
 * @author Delmas Douo Bougna
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name="ChessGameEntity.find", query="SELECT c FROM ChessGameEntity c WHERE c.id = :id"),		
	@NamedQuery(name="ChessStateEntity.getLastGame", query="SELECT c FROM ChessGameEntity c WHERE c.id = (SELECT MAX(c.id) FROM ChessGameEntity c") 
})
public class ChessGameEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@OneToMany(mappedBy = "game")
	private List<ChessStateEntity> states;

	public ChessGameEntity() {
	}

	public int getId() {
		return id;
	}
	
	public List<ChessStateEntity> getStates() {
		return states;
	}
	
	public ChessStateEntity getLastState() {
		return states.get(states.size() -1);
	}
	public void addState(ChessStateEntity state) {
		this.states.add(state);
	}

}
