package io.github.oliviercailloux.y2018.assisted_board_games.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ChessPieceEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id_piece;
	private String name_piece ;
	
	private List<String> from ;
	private List<String> to ; 
	
	@ManyToOne
	private ChessStateEntity state;
	
	public ChessPieceEntity() {
		
	}
	
	public int getId_piece() {
		return id_piece;
	}
	
	public String getName_piece() {
		return name_piece;
	}
	
	public String getFrom() {
		return from.get(from.size() -1 );
	}
	
	public void setName_piece(String name_piece) {
		this.name_piece = name_piece;
	}
	
	public String getTo() {
		return to.get(to.size() - 1);
	}
	
	public void setFrom(String from) {
		this.from.add(from);
	}
	
	public void setTo(String to) {
		this.to.add(to);
	}

}
