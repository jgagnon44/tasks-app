package com.fossfloors.taskapp.backend.beans;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.fossfloors.taskapp.backend.entity.Task;

public class TaskFilterSpec {

  private String        titleFilter;
  private String        descriptionFilter;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Task.State    stateFilter    = Task.State.ALL;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Task.Priority priorityFilter = Task.Priority.ALL;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Task.Type     typeFilter     = Task.Type.ALL;

  public void reset() {
    titleFilter = null;
    descriptionFilter = null;
    stateFilter = Task.State.ALL;
    priorityFilter = Task.Priority.ALL;
    typeFilter = Task.Type.ALL;
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

  public Task.State getStateFilter() {
    return stateFilter;
  }

  public void setStateFilter(Task.State stateFilter) {
    this.stateFilter = stateFilter;
  }

  public Task.Priority getPriorityFilter() {
    return priorityFilter;
  }

  public void setPriorityFilter(Task.Priority priorityFilter) {
    this.priorityFilter = priorityFilter;
  }

  public Task.Type getTypeFilter() {
    return typeFilter;
  }

  public void setTypeFilter(Task.Type typeFilter) {
    this.typeFilter = typeFilter;
  }

}
