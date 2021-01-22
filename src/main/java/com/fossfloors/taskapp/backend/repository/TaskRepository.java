package com.fossfloors.taskapp.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fossfloors.taskapp.backend.beans.TaskFilterSpec;
import com.fossfloors.taskapp.backend.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {
  @Override
  // @EntityGraph(value = "graph.task", type = EntityGraphType.LOAD)
  // @EntityGraph(attributePaths = { "notes", "stateHistory" })
  public List<Task> findAll();

  public List<Task> filter(TaskFilterSpec filter);

}
