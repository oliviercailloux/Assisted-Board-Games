package io.github.oliviercailloux.assisted_board_games;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class GameServiceTest {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(GameServiceTest.class);

	@Inject
	EntityManager em;

	@Inject
	QueryHelper helper;

	@Transactional
	public List<GameEntity> getAll() {
		return em.createQuery(helper.selectAll(GameEntity.class)).getResultList();
	}

	@Transactional
	public void persist(GameEntity game) {
		em.persist(game);
	}
}
