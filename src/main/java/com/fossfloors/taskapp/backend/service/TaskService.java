package com.fossfloors.taskapp.backend.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fossfloors.taskapp.backend.beans.TaskFilterSpec;
import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.entity.TaskNote;
import com.fossfloors.taskapp.backend.repository.TaskRepository;

@Service
@Transactional
public class TaskService {

  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

  private TaskRepository      repo;

  public TaskService(TaskRepository taskRepo) {
    this.repo = taskRepo;
  }

  public Optional<Task> findById(long id) {
    return repo.findById(id);
  }

  public List<Task> findAll() {
    return repo.findAll();
  }

  public List<Task> filter(TaskFilterSpec filter) {
    return repo.filter(filter);
  }

  public void delete(Task task) {
    if (task == null) {
      logger.warn("Task is null.");
      return;
    }

    repo.delete(task);
  }

  public Task save(Task task) {
    if (task == null) {
      logger.warn("Task is null.");
      return null;
    }

    return repo.save(task);
  }

  public void saveNote(Task task, TaskNote note) {
    if (!task.getNotes().contains(note)) {
      task.addNote(note);
    }

    save(task);
  }

  public void deleteNote(Task task, TaskNote note) {
    task.deleteNote(note);
    save(task);
  }

  @PostConstruct
  public void populateTestData() {
    if (repo.count() == 0) {
      Task task = null;
      TaskNote note = null;

      // Task 1
      task = new Task("Task-1");
      task.setPriority(Task.Priority.HIGH);
      task.setType(Task.Type.RECURRING);

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

      repo.save(task);

      // Task 2
      task = new Task("Task-2");
      task.setPriority(Task.Priority.LOW);
      task.setType(Task.Type.ONE_TIME);
      task.setState(Task.State.CLOSED);

      repo.save(task);
    }
  }

}
