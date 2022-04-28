package taskapp.backend.repository;

import java.util.List;
import java.util.Optional;

import taskapp.backend.beans.TaskFilterSpec;
import taskapp.backend.entity.Task;

public interface TaskRepositoryCustom {

  Optional<Task> findById(long id);

  List<Task> findAll();

  List<Task> filter(TaskFilterSpec filter);

}
