package io.github.oliviercailloux.y2018.assisted_board_games.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	@NamedQuery(name="ChessGameEntity.find", query="SELECT c FROM ChessGameEntity c WHERE c.id = :id")	,	
	@NamedQuery(name="ChessGameEntity.getLastGameId", query="SELECT MAX(id) FROM ChessGameEntity") 
	
})
public class ChessGameEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@OneToMany(mappedBy = "game",fetch = FetchType.EAGER)
	private List<ChessMoveEntity> moves;

	public ChessGameEntity() {
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
	
	public ChessMoveEntity getLastMoves() {
		return moves.get(moves.size() -1);
	}
	public void addMove(ChessMoveEntity move) {
		this.moves.add(move);
	}
	

}
