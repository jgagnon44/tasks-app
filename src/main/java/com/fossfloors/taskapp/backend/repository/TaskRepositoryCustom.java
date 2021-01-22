package com.fossfloors.taskapp.backend.repository;

import java.util.List;

import com.fossfloors.taskapp.backend.beans.TaskFilterSpec;
import com.fossfloors.taskapp.backend.entity.Task;

public interface TaskRepositoryCustom {

  public List<Task> findAll();

  public List<Task> filter(TaskFilterSpec filter);

}
