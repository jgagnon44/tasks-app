package com.fossfloors.taskapp.backend.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fossfloors.taskapp.util.DateTimeUtil;

// @NamedEntityGraphs({
// @NamedEntityGraph(name = "graph.task.notes", attributeNodes = @NamedAttributeNode("notes")),
// @NamedEntityGraph(name = "graph.task.stateHistory", attributeNodes =
// @NamedAttributeNode("stateHistory")) })

// @NamedEntityGraph(name = "graph.task", attributeNodes = { @NamedAttributeNode("notes"),
// @NamedAttributeNode("stateHistory") })

@Entity
public class Task extends AbstractEntity {

  public enum TaskType {
    ALL, ONE_TIME, RECURRING
  }

  public enum TaskState {
    ALL, OPEN, CLOSED
  }

  public enum TaskPriority {
    ALL, LOW, MEDIUM, HIGH
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

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
  private List<StateChangeHistory> stateHistory = new ArrayList<>();

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
  private List<TaskNote>           notes        = new ArrayList<>();

  private Date                     dateClosed;
  private Date                     dateDue;
  private Date                     dateStarted;
  private Date                     dateCompleted;

  public Task() {
    super();

    // Set defaults.
    type = TaskType.ONE_TIME;
    state = TaskState.OPEN;
    priority = TaskPriority.MEDIUM;
  }

  public Task(String title) {
    this();
    this.title = title;
  }

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

  public Date getDateClosed() {
    return dateClosed;
  }

  public void setDateClosed(Date dateClosed) {
    this.dateClosed = dateClosed;
  }

  public Date getDateDue() {
    return dateDue;
  }

  public void setDateDue(Date dateDue) {
    this.dateDue = dateDue;
  }

  public Date getDateStarted() {
    return dateStarted;
  }

  public void setDateStarted(Date dateStarted) {
    this.dateStarted = dateStarted;
  }

  public Date getDateCompleted() {
    return dateCompleted;
  }

  public void setDateCompleted(Date dateCompleted) {
    this.dateCompleted = dateCompleted;
  }
  
  public int getNotesCount() {
    return notes.size();
  }

  @Override
  public String toString() {
    return "Task [title=" + title + ", description=" + description + ", type=" + type + ", state="
        + state + ", priority=" + priority + ", stateHistory=" + stateHistory + ", notes=" + notes
        + ", dateClosed=" + DateTimeUtil.format(dateClosed) + ", dateDue="
        + DateTimeUtil.format(dateDue) + ", dateStarted=" + DateTimeUtil.format(dateStarted)
        + ", dateCompleted=" + DateTimeUtil.format(dateCompleted) + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((dateClosed == null) ? 0 : dateClosed.hashCode());
    result = prime * result + ((dateCompleted == null) ? 0 : dateCompleted.hashCode());
    result = prime * result + ((dateDue == null) ? 0 : dateDue.hashCode());
    result = prime * result + ((dateStarted == null) ? 0 : dateStarted.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
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
    Task other = (Task) obj;
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