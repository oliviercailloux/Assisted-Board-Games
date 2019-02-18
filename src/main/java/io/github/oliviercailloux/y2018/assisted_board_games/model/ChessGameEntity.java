package io.github.oliviercailloux.y2018.assisted_board_games.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/***
 * 
 * @author Delmas Douo Bougna
 *
 */
@Entity
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

}
