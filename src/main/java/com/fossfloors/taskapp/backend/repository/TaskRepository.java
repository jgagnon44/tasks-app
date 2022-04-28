package com.fossfloors.taskapp.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fossfloors.taskapp.backend.beans.TaskFilterSpec;
import com.fossfloors.taskapp.backend.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryCustom {

  Optional<Task> findById(long id);

  List<Task> findAll();

  List<Task> filter(TaskFilterSpec filter);

}
