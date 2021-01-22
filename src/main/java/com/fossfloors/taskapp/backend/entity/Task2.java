package com.fossfloors.taskapp.backend.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
// @NamedEntityGraphs({
// @NamedEntityGraph(name = "graph.Task.notes", attributeNodes = @NamedAttributeNode("notes")),
// @NamedEntityGraph(name = "graph.Task.stateHistory", attributeNodes =
// @NamedAttributeNode("stateHistory")),
// @NamedEntityGraph(name = "graph.Task.children", attributeNodes = @NamedAttributeNode("children"))
// })
// @NamedEntityGraph(name = "taskGraph", attributeNodes = { @NamedAttributeNode("notes"),
// @NamedAttributeNode("stateHistory"), @NamedAttributeNode("children") })
public class Task2 extends AbstractEntity {

  public enum TaskType {
    ONE_TIME, RECURRING
  }

  public enum TaskState {
    ACTIVE, CLOSED
  }

  public enum TaskPriority {
    LOW, MEDIUM, HIGH
  }

  public Task2() {
    // Set defaults.
    type = TaskType.ONE_TIME;
    state = TaskState.ACTIVE;
    priority = TaskPriority.MEDIUM;

    // TODO find way to get logged in user
    createdBy = "user";
    modifiedBy = "user";

    // TODO better way to deal with date/time
    dateCreated = new Date(System.currentTimeMillis()).toString();
  }

  public Task2(String title) {
    this();
    this.title = title;
  }

  @NotNull
  @NotEmpty
  private String                   title;

  private String                   description;

  @NotNull
  @Enumerated(EnumType.STRING)
  private TaskType                 type;

  @NotNull
  @Enumerated(EnumType.STRING)
  private TaskState                state;

  @NotNull
  @Enumerated(EnumType.STRING)
  private TaskPriority             priority;

  @OneToMany(mappedBy = "task")
  private List<StateChangeHistory> stateHistory = new ArrayList<>();

  @OneToMany(mappedBy = "task")
  private List<TaskNote>           notes        = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "id", insertable = false, updatable = false)
  private Task2                     parent;

  @OneToMany(mappedBy = "parent")
  private List<Task2>               children     = new ArrayList<>();

  @NotNull
  @NotEmpty
  private String                   createdBy;

  @NotNull
  @NotEmpty
  private String                   modifiedBy;

  @NotNull
  @NotEmpty
  private String                   dateCreated;

  private String                   dateClosed;
  private String                   dateDue;
  private String                   dateStarted;
  private String                   dateCompleted;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TaskType getType() {
    return type;
  }

  public void setType(TaskType type) {
    this.type = type;
  }

  public TaskState getState() {
    return state;
  }

  public void setState(TaskState state) {
    this.state = state;
  }

  public TaskPriority getPriority() {
    return priority;
  }

  public void setPriority(TaskPriority priority) {
    this.priority = priority;
  }

  public List<StateChangeHistory> getStateHistory() {
    return stateHistory;
  }

  public void setStateHistory(List<StateChangeHistory> stateHistory) {
    this.stateHistory = stateHistory;
  }

  public List<TaskNote> getNotes() {
    return notes;
  }

  public void setNotes(List<TaskNote> notes) {
    this.notes = notes;
  }

  public Task2 getParent() {
    return parent;
  }

  public void setParent(Task2 parent) {
    this.parent = parent;
  }

  public List<Task2> getChildren() {
    return children;
  }

  public void setChildren(List<Task2> children) {
    this.children = children;
  }

  public String getDateClosed() {
    return dateClosed;
  }

  public void setDateClosed(String dateClosed) {
    this.dateClosed = dateClosed;
  }

  public String getDateDue() {
    return dateDue;
  }

  public void setDateDue(String dateDue) {
    this.dateDue = dateDue;
  }

  public String getDateStarted() {
    return dateStarted;
  }

  public void setDateStarted(String dateStarted) {
    this.dateStarted = dateStarted;
  }

  public String getDateCompleted() {
    return dateCompleted;
  }

  public void setDateCompleted(String dateCompleted) {
    this.dateCompleted = dateCompleted;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((children == null) ? 0 : children.hashCode());
    result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
    result = prime * result + ((dateClosed == null) ? 0 : dateClosed.hashCode());
    result = prime * result + ((dateCompleted == null) ? 0 : dateCompleted.hashCode());
    result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
    result = prime * result + ((dateDue == null) ? 0 : dateDue.hashCode());
    result = prime * result + ((dateStarted == null) ? 0 : dateStarted.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
    result = prime * result + ((notes == null) ? 0 : notes.hashCode());
    result = prime * result + ((priority == null) ? 0 : priority.hashCode());
    result = prime * result + ((state == null) ? 0 : state.hashCode());
    result = prime * result + ((stateHistory == null) ? 0 : stateHistory.hashCode());
    result = prime * result + ((title == null) ? 0 : title.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    Task2 other = (Task2) obj;
    if (children == null) {
      if (other.children != null)
        return false;
    } else if (!children.equals(other.children))
      return false;
    if (createdBy == null) {
      if (other.createdBy != null)
        return false;
    } else if (!createdBy.equals(other.createdBy))
      return false;
    if (dateClosed == null) {
      if (other.dateClosed != null)
        return false;
    } else if (!dateClosed.equals(other.dateClosed))
      return false;
    if (dateCompleted == null) {
      if (other.dateCompleted != null)
        return false;
    } else if (!dateCompleted.equals(other.dateCompleted))
      return false;
    if (dateCreated == null) {
      if (other.dateCreated != null)
        return false;
    } else if (!dateCreated.equals(other.dateCreated))
      return false;
    if (dateDue == null) {
      if (other.dateDue != null)
        return false;
    } else if (!dateDue.equals(other.dateDue))
      return false;
    if (dateStarted == null) {
      if (other.dateStarted != null)
        return false;
    } else if (!dateStarted.equals(other.dateStarted))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (modifiedBy == null) {
      if (other.modifiedBy != null)
        return false;
    } else if (!modifiedBy.equals(other.modifiedBy))
      return false;
    if (notes == null) {
      if (other.notes != null)
        return false;
    } else if (!notes.equals(other.notes))
      return false;
    if (priority != other.priority)
      return false;
    if (state != other.state)
      return false;
    if (stateHistory == null) {
      if (other.stateHistory != null)
        return false;
    } else if (!stateHistory.equals(other.stateHistory))
      return false;
    if (title == null) {
      if (other.title != null)
        return false;
    } else if (!title.equals(other.title))
      return false;
    if (type != other.type)
      return false;
    return true;
  }

}
