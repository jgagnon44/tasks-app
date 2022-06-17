package com.fossfloors.taskapp.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.entity.Task.Priority;
import com.fossfloors.taskapp.backend.entity.Task.State;
import com.fossfloors.taskapp.backend.entity.Task.Type;
import com.fossfloors.taskapp.backend.repository.TaskRepository;
import com.fossfloors.taskapp.backend.repository.TaskRepositoryCustom;
import com.fossfloors.taskapp.backend.repository.TaskRepositoryCustomImpl;
import com.fossfloors.taskapp.backend.service.TaskService;

public class TestTaskService {

  private TaskService service;

  @Before
  public void setUp() {
    TaskRepository repo = new TaskRepositoryCustomImpl();
    service = new TaskService(repo);
  }

  @Test
  public void testDelete() {
    fail("Not yet implemented");
  }

  @Test
  public void testSave() {
    Task task = new Task();
    task.setTitle("testSave");
    task.setState(State.OPEN);
    task.setType(Type.ONE_TIME);
    task.setPriority(Priority.MEDIUM);

    Task saved = service.save(task);
  }

  @Test
  public void testSaveNote() {
    fail("Not yet implemented");
  }

  @Test
  public void testDeleteNote() {
    fail("Not yet implemented");
  }

}
