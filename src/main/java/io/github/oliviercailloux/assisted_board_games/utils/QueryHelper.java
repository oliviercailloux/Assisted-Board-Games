package io.github.oliviercailloux.assisted_board_games.utils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RequestScoped
public class QueryHelper {

  @Inject
  EntityManagerFactory emf;

  /***
   * 
   * Code from
   * https://github.com/oliviercailloux/samples/blob/master/JavaEE-JPA-Inject-Servlets/src/main/java/io/github/oliviercailloux/javaee_jpa_inject_servlets/utils/QueryHelper.java
   * 
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
