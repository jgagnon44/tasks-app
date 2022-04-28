package taskapp.backend.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import taskapp.backend.beans.TaskFilterSpec;
import taskapp.backend.entity.Task;
import taskapp.backend.entity.TaskNote;
import taskapp.backend.repository.TaskRepository;

@Service
@Transactional
public class TaskService {

  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

  private TaskRepository      taskRepo;

  public TaskService(TaskRepository taskRepo) {
    this.taskRepo = taskRepo;
  }

  public Optional<Task> findById(long id) {
    return taskRepo.findById(id);
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
    if (taskRepo.count() == 0) {
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

      taskRepo.save(task);

      // Task 2
      task = new Task("Task-2");
      task.setPriority(Task.Priority.LOW);
      task.setType(Task.Type.ONE_TIME);
      task.setState(Task.State.CLOSED);

      taskRepo.save(task);
    }
  }

}
