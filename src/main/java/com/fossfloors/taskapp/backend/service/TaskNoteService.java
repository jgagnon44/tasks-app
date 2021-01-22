package com.fossfloors.taskapp.backend.service;

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

  private TaskNoteRepository  noteRepo;

  public TaskNoteService(TaskNoteRepository noteRepo) {
    this.noteRepo = noteRepo;
  }

  public void delete(TaskNote note) {
    if (note == null) {
      logger.warn("TaskNote is null.");
      return;
    }

    noteRepo.delete(note);
  }

  public void save(TaskNote note) {
    if (note == null) {
      logger.warn("TaskNote is null.");
      return;
    }

    noteRepo.save(note);
  }

}
