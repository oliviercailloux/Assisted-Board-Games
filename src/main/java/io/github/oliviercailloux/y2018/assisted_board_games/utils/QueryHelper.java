package io.github.oliviercailloux.y2018.assisted_board_games.utils;

import static java.util.Objects.requireNonNull;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RequestScoped
public class QueryHelper {
	/***
	 * Code From https://github.com/oliviercailloux/samples/blob/master/JavaEE-JPA-Inject-Servlets/src/main/java/io/github/oliviercailloux/javaee_jpa_inject_servlets/utils/QueryHelper.java
	 * @Author : Olivier Cailloux
	 */
	
	@PersistenceUnit
	private EntityManagerFactory emFactory;

	public QueryHelper() {
		emFactory = null;
	}

	public EntityManagerFactory getEmFactory() {
		return requireNonNull(emFactory);
	}

	public <T> CriteriaQuery<T> selectAll(Class<T> type) {
		final CriteriaQuery<T> query = emFactory.getCriteriaBuilder().createQuery(type);
		final Root<T> from = query.from(type);
		query.select(from);
		return query;
	}

	public void setEmFactory(EntityManagerFactory emFactory) {
		this.emFactory = requireNonNull(emFactory);
	}

}
