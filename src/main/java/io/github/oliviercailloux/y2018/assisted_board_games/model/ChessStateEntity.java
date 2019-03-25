package io.github.oliviercailloux.y2018.assisted_board_games.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/***
 * 
 * @author Delmas Douo Bougna
 *
 */
@Entity
public class ChessStateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id_state;

	@ManyToOne
	private ChessGameEntity game;
	
	@OneToMany(mappedBy = "state")
	private List<ChessMoveEntity> moves;

	public ChessStateEntity() {
	}

	public int getId_state() {
		return id_state;
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

