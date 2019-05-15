package io.github.oliviercailloux.assisted_board_games.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import io.github.oliviercailloux.assisted_board_games.model.ChessGameEntity;
import io.github.oliviercailloux.assisted_board_games.model.ChessMoveEntity;
import io.github.oliviercailloux.assisted_board_games.utils.QueryHelper;

@RequestScoped
public class ChessService {

    /***
     * 
     * @author Delmas Douo Bougna
     *
     */
    @Inject
    EntityManager em;
    @Inject
    QueryHelper helper;

    @Transactional
    public List<ChessGameEntity> getAllGames() {
        return em.createQuery(helper.selectAll(ChessGameEntity.class)).getResultList();
    }

    @Transactional
    public List<ChessMoveEntity> getAllMoves() {
        return em.createQuery(helper.selectAll(ChessMoveEntity.class)).getResultList();
    }

    @Transactional
    public ChessGameEntity getGame(int idGame) {
        ChessGameEntity game = em.createNamedQuery("ChessGameEntity.find", ChessGameEntity.class)
                .setParameter("id", idGame).getSingleResult();
        return game;
    }

    @Transactional
    public int getLastGameId() {
        return em.createNamedQuery("ChessGameEntity.getLastGameId", Integer.class)
                .getSingleResult();
    }

    @Transactional
    public ChessMoveEntity getMove(int idMove) {
        ChessMoveEntity q = em.createNamedQuery("ChessMoveEntity.find", ChessMoveEntity.class)
                .setParameter("id", idMove).getSingleResult();
        return q;
    }

    /**
     * Gets the id of the last move played for the given game
     * 
     * @param idGame
     * @return id of the last Move played for the given game
     */
    @Transactional
    public int getLastMoveId(int idGame) {
        int q = em.createNamedQuery("ChessMoveEntity.getLastMoveId", Integer.class)
                .setParameter("id", idGame).getSingleResult();
        return q;
    }

    @Transactional
    public void persist(ChessGameEntity game) {
        em.getTransaction().begin();
        em.persist(game);
        em.getTransaction().commit();
    }

    @Transactional
    public void persist(ChessMoveEntity move) {
        em.getTransaction().begin();
        em.persist(move);
        em.getTransaction().commit();
    }
}
