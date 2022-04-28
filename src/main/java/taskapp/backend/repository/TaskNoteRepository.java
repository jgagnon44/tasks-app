package taskapp.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import taskapp.backend.entity.TaskNote;

public interface TaskNoteRepository extends JpaRepository<TaskNote, Long> {
}
