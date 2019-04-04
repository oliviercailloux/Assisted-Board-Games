package io.github.oliviercailloux.y2018.assisted_board_games.model;

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
	@NamedQuery(name="ChessMoveEntity.find", query="SELECT c FROM ChessMoveEntity c WHERE c.id = :id")	,	
	@NamedQuery(name="ChessMoveEntity.getLastMoveId", query="SELECT MAX(id) FROM ChessMoveEntity c WHERE c.game.id = :id"), 
	@NamedQuery(name="ChessMoveEntity.getLastMove", query="SELECT MAX(id) FROM ChessMoveEntity ")
})
public class ChessMoveEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id_move;
	private String  from ;
	private String  to ; 
	
	@ManyToOne
	private ChessGameEntity game;
	
	
	public ChessMoveEntity() {
	}
	public ChessMoveEntity(String from, String to) {
		this.from=from;
		this.to=to;
	}
	
	public int getId_move() {
		return id_move;
	} 

	public String getFrom() {
		return from ;
	}
	
	public String getTo() {
		return to ;
	}
	
	public void setFrom(String from) {
		this.from = from ;
	}
	
	public void setTo(String to) {
		this.to = to ;
	}
	public void setGame(ChessGameEntity game) {
		this.game=game;
	}
	@Override
	public String toString() {
		return this.getId_move()+" : from"+ getFrom()+"to"+getTo()+"\n";
	}

}
