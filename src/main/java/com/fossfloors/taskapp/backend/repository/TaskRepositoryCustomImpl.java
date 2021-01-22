package com.fossfloors.taskapp.backend.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fossfloors.taskapp.backend.beans.TaskFilterSpec;
import com.fossfloors.taskapp.backend.entity.Task;

@Repository
@Transactional
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

  private static final Logger logger = LoggerFactory.getLogger(TaskRepositoryCustomImpl.class);

  @PersistenceContext
  private EntityManager       entityManager;

  // @formatter:off
  @Override
  public List<Task> findAll() {
//    EntityGraph<?> graph = entityManager.getEntityGraph("graph.task");

    List<Task> tasks = entityManager
        .createQuery("select distinct t from Task t " +
            "left join fetch t.notes", Task.class)
//        .setHint("javax.persistence.fetchgraph", graph)
        .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
//        .setHint(GraphSemantic.FETCH.getJpaHintName(), graph)
        .getResultList();
    
    tasks = entityManager
        .createQuery("select distinct t from Task t " +
            "left join fetch t.stateHistory h " +
            "where t in :tasks", Task.class)
        .setParameter("tasks", tasks)
//        .setHint("javax.persistence.fetchgraph", graph)
        .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
//        .setHint(GraphSemantic.FETCH.getJpaHintName(), graph)
        .getResultList();

    return tasks;
  }
  // @formatter:on

  // @formatter:off
  public List<Task> filter(TaskFilterSpec filter) {
    List<Task> tasks = entityManager
        .createQuery("select distinct t from Task t " +
            "left join fetch t.notes n " +
            "where 1=1" +
            buildFilterWhereClause(filter), Task.class)
        .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
        .getResultList();
    
    tasks = entityManager
        .createQuery("select distinct t from Task t " +
            "left join fetch t.stateHistory h " +
            "where t in :tasks" +
            buildFilterWhereClause(filter), Task.class)
        .setParameter("tasks", tasks)
        .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
        .getResultList();
    
    return tasks;
  }
  // @formatter:on

  @SuppressWarnings("incomplete-switch")
  private String buildFilterWhereClause(TaskFilterSpec filter) {
    StringBuilder sb = new StringBuilder();

    switch (filter.getStateFilter()) {
      case CLOSED:
        sb.append(" and t.state = 'CLOSED' ");
        break;
      case OPEN:
        sb.append(" and t.state = 'OPEN' ");
        break;
    }

    switch (filter.getPriorityFilter()) {
      case HIGH:
        sb.append(" and t.priority = 'HIGH' ");
        break;
      case LOW:
        sb.append(" and t.priority = 'LOW' ");
        break;
      case MEDIUM:
        sb.append(" and t.priority = 'MEDIUM' ");
        break;
    }

    switch (filter.getTypeFilter()) {
      case ONE_TIME:
        sb.append(" and t.type = 'ONE_TIME' ");
        break;
      case RECURRING:
        sb.append(" and t.type = 'RECURRING' ");
        break;
    }

    logger.debug("WHERE CLAUSE: {}", sb.toString());
    return sb.toString();
  }

}
