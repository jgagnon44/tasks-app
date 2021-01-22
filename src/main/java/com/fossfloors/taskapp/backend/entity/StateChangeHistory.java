package com.fossfloors.taskapp.backend.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class StateChangeHistory extends AbstractEntity {

  @ManyToOne
  private Task           task;

  private Task.TaskState oldState;

  @NotNull
  private Task.TaskState newState;

  public StateChangeHistory() {
    super();
  }

  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
  }

  public Task.TaskState getOldState() {
    return oldState;
  }

  public void setOldState(Task.TaskState oldState) {
    this.oldState = oldState;
  }

  public Task.TaskState getNewState() {
    return newState;
  }

  public void setNewState(Task.TaskState newState) {
    this.newState = newState;
  }

  @Override
  public String toString() {
    return "StateChangeHistory [task=" + task.hashCode() + ", oldState=" + oldState + ", newState="
        + newState + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((newState == null) ? 0 : newState.hashCode());
    result = prime * result + ((oldState == null) ? 0 : oldState.hashCode());
    result = prime * result + ((task == null) ? 0 : task.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    StateChangeHistory other = (StateChangeHistory) obj;
    if (newState != other.newState)
      return false;
    if (oldState != other.oldState)
      return false;
    if (task == null) {
      if (other.task != null)
        return false;
    } else if (!task.equals(other.task))
      return false;
    return true;
  }

}
