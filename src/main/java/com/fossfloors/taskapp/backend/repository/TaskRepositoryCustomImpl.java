package com.fossfloors.taskapp.backend.repository;

import java.util.List;
import java.util.Optional;

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
  public Optional<Task> findById(long id) {
    Task task = entityManager
        .createQuery("select distinct t from Task t left join fetch t.notes where t.id=:id",
            Task.class)
        .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
        .setParameter("id", id)
        .getSingleResult();

    Optional<Task> result = Optional.of(task);
    return result;
  }
  // @formatter:on

  // @formatter:off
  public List<Task> findAll() {
    List<Task> tasks = entityManager
        .createQuery("select distinct t from Task t left join fetch t.notes", Task.class)
        .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
        .getResultList();

    return tasks;
  }
  // @formatter:on

  // @formatter:off
  public List<Task> filter(TaskFilterSpec filter) {
    List<Task> tasks = entityManager
        .createQuery("select distinct t from Task t left join fetch t.notes where 1=1" +
            buildFilterWhereClause(filter), Task.class)
        .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
        .getResultList();
    
    return tasks;
  }
  // @formatter:on

  private String buildFilterWhereClause(TaskFilterSpec filter) {
    StringBuilder sb = new StringBuilder();

    switch (filter.getStateFilter()) {
      case CLOSED:
        sb.append(" and t.state = 'CLOSED' ");
        break;
      case OPEN:
        sb.append(" and t.state = 'OPEN' ");
        break;
      case ARCHIVED:
        sb.append(" and t.state = 'ARCHIVED' ");
        break;
      default:
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
      default:
        break;
    }

    switch (filter.getTypeFilter()) {
      case ONE_TIME:
        sb.append(" and t.type = 'ONE_TIME' ");
        break;
      case RECURRING:
        sb.append(" and t.type = 'RECURRING' ");
        break;
      default:
        break;
    }

    logger.debug("WHERE CLAUSE: {}", sb.toString());
    return sb.toString();
  }

}
