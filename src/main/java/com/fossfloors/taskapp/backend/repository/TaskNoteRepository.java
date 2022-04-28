package com.fossfloors.taskapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fossfloors.taskapp.backend.entity.TaskNote;

public interface TaskNoteRepository extends JpaRepository<TaskNote, Long> {
}
