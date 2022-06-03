package io.github.oliviercailloux.abg;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class ChessBoardServiceTest {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ChessBoardServiceTest.class);

	@Inject
	EntityManager em;

	@Inject
	QueryHelper helper;

	@Transactional
	public List<ChessBoard> getAll() {
		return em.createQuery(helper.selectAll(ChessBoard.class)).getResultList();
	}

	@Transactional
	public void persist(ChessBoard chessboard) {
		em.persist(chessboard);
	}
}
