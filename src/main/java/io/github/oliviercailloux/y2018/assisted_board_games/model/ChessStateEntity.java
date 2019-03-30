package io.github.oliviercailloux.y2018.assisted_board_games.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
	@NamedQuery(name="ChessStateEntity.find", query="SELECT c FROM ChessStateEntity c WHERE c.id = :id"),		
	@NamedQuery(name="ChessStateEntity.getLastState", query="SELECT c FROM ChessStateEntity c WHERE c.game.id = :gameId AND c.id = (SELECT MAX(c.id) FROM ChessStateEntity c WHERE c.game.id = :gameId") 
})

public class ChessStateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id	;

	@ManyToOne
	private ChessGameEntity game;
	
	@OneToMany(mappedBy = "state")
	private List<ChessMoveEntity> moves;

	public ChessStateEntity() {
	}

	public int getId() {
		return id;
	}

	public List<ChessMoveEntity> getMoves() {
		return moves;
	}
	
	public void setMoves(List<ChessMoveEntity> moves) {
		this.moves = moves;
	}
	public void setGame(ChessGameEntity game) {
		this.game= game;
	}
	
	public void addMove(ChessMoveEntity move) {
		this.moves.add(move);
	}
	
}

