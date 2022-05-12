package com.fossfloors.taskapp.backend.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fossfloors.taskapp.backend.entity.TaskNote;
import com.fossfloors.taskapp.backend.repository.TaskNoteRepository;

@Service
@Transactional
public class TaskNoteService {

  private static final Logger logger = LoggerFactory.getLogger(TaskNoteService.class);

  private TaskNoteRepository  repo;

  public TaskNoteService(TaskNoteRepository repo) {
    this.repo = repo;
  }

  public Optional<TaskNote> findById(long id) {
    return repo.findById(id);
  }

  public List<TaskNote> findAll() {
    return repo.findAll();
  }

  public void delete(TaskNote note) {
    if (note == null) {
      logger.warn("TaskNote is null.");
      return;
    }

    repo.delete(note);
  }

  public TaskNote save(TaskNote note) {
    if (note == null) {
      logger.warn("TaskNote is null.");
      return null;
    }

    return repo.save(note);
  }

}
