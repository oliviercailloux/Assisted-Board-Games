package io.github.oliviercailloux.y2018.assistedboardgames.assisted_board_games.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ChessStateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id_state;

	private String jsonState;

	@ManyToOne
	private ChessGameEntity game;

	public ChessStateEntity() {

	}

	public int getId() {
		return id_state;
	}

	public String getStates() {
		return jsonState;
	}

	public void setName(String stateJson) {
		this.jsonState = stateJson;
	}

}
