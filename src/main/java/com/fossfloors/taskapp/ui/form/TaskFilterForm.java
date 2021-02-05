package com.fossfloors.taskapp.ui.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fossfloors.taskapp.backend.beans.TaskFilterSpec;
import com.fossfloors.taskapp.backend.entity.Task;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/shared-styles.css")
@SuppressWarnings("serial")
public class TaskFilterForm extends HorizontalLayout {

  private static final Logger         logger = LoggerFactory.getLogger(TaskFilterForm.class);

  private ComboBox<Task.TaskState>    stateFilter;
  private ComboBox<Task.TaskPriority> priorityFilter;
  private ComboBox<Task.TaskType>     typeFilter;

  private Binder<TaskFilterSpec>      binder;

  public TaskFilterForm() {
    addClassName("task-filter-view");
    configureView();
  }

  @Override
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  public void refresh() {
    fireEvent(new FilterChangedEvent(this, binder.getBean()));
  }

  private void configureView() {
    stateFilter = new ComboBox<>();
    stateFilter.setLabel("Task State");
    stateFilter.setItems(Task.TaskState.values());

    priorityFilter = new ComboBox<>();
    priorityFilter.setLabel("Priority");
    priorityFilter.setItems(Task.TaskPriority.values());

    typeFilter = new ComboBox<>();
    typeFilter.setLabel("Type");
    typeFilter.setItems(Task.TaskType.values());

    add(stateFilter, priorityFilter, typeFilter);

    binder = new Binder<>(TaskFilterSpec.class);
    binder.setBean(new TaskFilterSpec());
    binder.bindInstanceFields(this);
    binder.addValueChangeListener(event -> {
      refresh();
    });
  }

  public static abstract class TaskFilterEvent extends ComponentEvent<TaskFilterForm> {
    private TaskFilterSpec filterSpec;

    protected TaskFilterEvent(TaskFilterForm source, TaskFilterSpec filterSpec) {
      super(source, false);
      this.filterSpec = filterSpec;
    }

    public TaskFilterSpec getFilterSpec() {
      return filterSpec;
    }
  }

  public static class FilterChangedEvent extends TaskFilterEvent {
    public FilterChangedEvent(TaskFilterForm source, TaskFilterSpec filterSpec) {
      super(source, filterSpec);
    }
  }

}
