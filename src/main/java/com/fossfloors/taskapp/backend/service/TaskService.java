package com.fossfloors.taskapp.backend.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fossfloors.taskapp.backend.beans.TaskFilterSpec;
import com.fossfloors.taskapp.backend.entity.StateChangeHistory;
import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.entity.TaskNote;
import com.fossfloors.taskapp.backend.entity.Task.TaskPriority;
import com.fossfloors.taskapp.backend.entity.Task.TaskState;
import com.fossfloors.taskapp.backend.entity.Task.TaskType;
import com.fossfloors.taskapp.backend.repository.TaskRepository;

@Service
@Transactional
public class TaskService {

  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

  private TaskRepository      taskRepo;

  public TaskService(TaskRepository taskRepo) {
    this.taskRepo = taskRepo;
  }

  public List<Task> findAll() {
    return taskRepo.findAll();
  }

  public List<Task> filter(TaskFilterSpec filter) {
    return taskRepo.filter(filter);
  }

  public void delete(Task task) {
    if (task == null) {
      logger.warn("Task is null.");
      return;
    }

    taskRepo.delete(task);
  }

  public void save(Task task) {
    if (task == null) {
      logger.warn("Task is null.");
      return;
    }

    taskRepo.save(task);
  }

  @PostConstruct
  public void populateTestData() {
    if (taskRepo.count() == 0) {
      Task task = null;
      TaskNote note = null;
      StateChangeHistory history = null;

      // Task 1
      task = new Task("Task-1");
      task.setPriority(TaskPriority.HIGH);
      task.setType(TaskType.RECURRING);

      // Note 1
      note = new TaskNote();
      note.setTask(task);
      note.setNote("note-1");
      task.getNotes().add(note);

      // Note 2
      note = new TaskNote();
      note.setTask(task);
      note.setNote("note-2");
      task.getNotes().add(note);

      history = new StateChangeHistory();
      history.setTask(task);
      history.setNewState(TaskState.OPEN);
      task.getStateHistory().add(history);

      taskRepo.save(task);

      // Task 2
      task = new Task("Task-2");
      task.setPriority(TaskPriority.LOW);
      task.setType(TaskType.ONE_TIME);
      task.setState(TaskState.CLOSED);

      history = new StateChangeHistory();
      history.setTask(task);
      history.setOldState(TaskState.OPEN);
      history.setNewState(TaskState.CLOSED);
      task.getStateHistory().add(history);

      taskRepo.save(task);
    }
  }

}
