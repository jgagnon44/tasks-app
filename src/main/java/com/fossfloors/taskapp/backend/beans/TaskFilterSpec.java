package com.fossfloors.taskapp.backend.beans;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.fossfloors.taskapp.backend.entity.Task.TaskPriority;
import com.fossfloors.taskapp.backend.entity.Task.TaskState;
import com.fossfloors.taskapp.backend.entity.Task.TaskType;

public class TaskFilterSpec {

  private String       titleFilter;
  private String       descriptionFilter;

  @NotNull
  @Enumerated(EnumType.STRING)
  private TaskState    stateFilter    = TaskState.ALL;

  @NotNull
  @Enumerated(EnumType.STRING)
  private TaskPriority priorityFilter = TaskPriority.ALL;

  @NotNull
  @Enumerated(EnumType.STRING)
  private TaskType     typeFilter     = TaskType.ALL;

  public void reset() {
    titleFilter = null;
    descriptionFilter = null;
    stateFilter = TaskState.ALL;
    priorityFilter = TaskPriority.ALL;
    typeFilter = TaskType.ALL;
  }

  public String getTitleFilter() {
    return titleFilter;
  }

  public void setTitleFilter(String titleFilter) {
    this.titleFilter = titleFilter;
  }

  public String getDescriptionFilter() {
    return descriptionFilter;
  }

  public void setDescriptionFilter(String descriptionFilter) {
    this.descriptionFilter = descriptionFilter;
  }

  public TaskState getStateFilter() {
    return stateFilter;
  }

  public void setStateFilter(TaskState stateFilter) {
    this.stateFilter = stateFilter;
  }

  public TaskPriority getPriorityFilter() {
    return priorityFilter;
  }

  public void setPriorityFilter(TaskPriority priorityFilter) {
    this.priorityFilter = priorityFilter;
  }

  public TaskType getTypeFilter() {
    return typeFilter;
  }

  public void setTypeFilter(TaskType typeFilter) {
    this.typeFilter = typeFilter;
  }

}
