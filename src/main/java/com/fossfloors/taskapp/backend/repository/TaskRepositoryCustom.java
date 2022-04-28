package com.fossfloors.taskapp.backend.repository;

import java.util.List;
import java.util.Optional;

import com.fossfloors.taskapp.backend.beans.TaskFilterSpec;
import com.fossfloors.taskapp.backend.entity.Task;

public interface TaskRepositoryCustom {

  Optional<Task> findById(long id);

  List<Task> findAll();

  List<Task> filter(TaskFilterSpec filter);

}
