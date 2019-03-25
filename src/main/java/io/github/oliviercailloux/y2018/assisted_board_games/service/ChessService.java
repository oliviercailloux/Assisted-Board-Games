package io.github.oliviercailloux.y2018.assisted_board_games.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import io.github.oliviercailloux.y2018.assisted_board_games.model.*;
import io.github.oliviercailloux.y2018.assisted_board_games.utils.QueryHelper;


@RequestScoped
public class ChessService {
	/***
	 * 
	 * @author Delmas Douo Bougna
	 *
	 */
	@PersistenceContext
	private EntityManager em;

	@Inject
	private QueryHelper helper;
	
	@Transactional
	public List<ChessGameEntity> getAllGame() {

		return em.createQuery(helper.selectAll(ChessGameEntity.class)).getResultList();

	}
	
	@Transactional
	public List<ChessStateEntity> getAllState() {

		return em.createQuery(helper.selectAll(ChessStateEntity.class)).getResultList();

	}
	
	@Transactional
	public List<ChessMoveEntity> getAllMove() {

		return em.createQuery(helper.selectAll(ChessMoveEntity.class)).getResultList();

	}
	
	@Transactional 
	public ChessGameEntity getGame(int idGame) {
		return em.createQuery(helper.selectAll(ChessGameEntity.class)).getResultList().get(idGame);
	}
	
	@Transactional 
	public ChessStateEntity getLastState(int idGame) {
		ChessGameEntity game = em.createQuery(helper.selectAll(ChessGameEntity.class)).getResultList().get(idGame);
		return game.getStates().get(game.getStates().size()-1);
	}
	
	@Transactional
	public ChessStateEntity getLastState (int idgame) {
		return em.createQuery(helper.selectAll(ChessGameEntity.class)).getResultList().get(idgame).getLastState();
		
	}
	
	@Transactional 
	public ChessGameEntity getLastGame() {
		return em.createQuery(helper.selectAll(ChessGameEntity.class)).getResultList().get(em.createQuery("select max(game.id) from ChessGameEntity game", Integer.class).getSingleResult());
	}
	
	@Transactional
	public void persist(ChessGameEntity game) {
		em.persist(game);
	}
	
	
	
	@Transactional
	public void persist(ChessStateEntity state) {
		em.persist(state);
	}
	
	@Transactional
	public void persist(ChessMoveEntity move) {
		em.persist(move);
	}

}
