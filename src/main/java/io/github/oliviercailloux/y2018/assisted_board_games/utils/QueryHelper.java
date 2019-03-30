package io.github.oliviercailloux.y2018.assisted_board_games.utils;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RequestScoped
public class QueryHelper {
	@PersistenceUnit
	private EntityManagerFactory emf;

	/***
	 * 
	 * Code from https://github.com/oliviercailloux/samples/blob/master/JavaEE-JPA-Inject-Servlets/src/main/java/io/github/oliviercailloux/javaee_jpa_inject_servlets/utils/QueryHelper.java
	 * @author Olivier Cailloux
	 *
	 */
	public <T> CriteriaQuery<T> selectAll(Class<T> type) {
		final CriteriaBuilder criteriaBuilder = emf.getCriteriaBuilder();
		final CriteriaQuery<T> query = criteriaBuilder.createQuery(type);
		final Root<T> from = query.from(type);
		query.select(from);
		return query;
	}

}