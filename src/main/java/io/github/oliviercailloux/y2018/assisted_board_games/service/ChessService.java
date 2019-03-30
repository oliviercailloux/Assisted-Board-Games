package io.github.oliviercailloux.y2018.assisted_board_games.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
	public List<ChessGameEntity> getAllGames() {
		return em.createQuery(helper.selectAll(ChessGameEntity.class)).getResultList();
	}
	
	@Transactional
	public List<ChessStateEntity> getAllStates() {
		return em.createQuery(helper.selectAll(ChessStateEntity.class)).getResultList();
	}
	
	@Transactional
	public List<ChessMoveEntity> getAllMoves() {
		return em.createQuery(helper.selectAll(ChessMoveEntity.class)).getResultList();
	}
	
	@Transactional 
	public ChessGameEntity getGame(int idGame) {
		Query q = em.createNamedQuery("ChessGameEntity.find").setParameter("id", idGame); 
		return (ChessGameEntity) q.getSingleResult();
	}
	
	@Transactional
	public ChessStateEntity getLastState (int idGame) {		
		Query q = em.createNamedQuery("ChessStateEntity.getLastState").setParameter("gameId", idGame);
		return (ChessStateEntity) q.getSingleResult();		
	}
	
	@Transactional 
	public ChessGameEntity getLastGame() {		
		Query q = em.createNamedQuery("ChessGameEntity.getLastGame");
		return (ChessGameEntity) q.getSingleResult();
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
