package com.fossfloors.taskapp.ui.view;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.service.TaskService;
import com.fossfloors.taskapp.ui.form.TaskFilterForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/shared-styles.css")
@SuppressWarnings("serial")
public class TaskListView extends VerticalLayout {

  private static final Logger logger = LoggerFactory.getLogger(TaskListView.class);

  private final TaskService   taskService;

  private Grid<Task>          grid   = new Grid<>(Task.class);

  public TaskListView(TaskService taskService) {
    this.taskService = taskService;
    addClassName("task-list-view");
    setSizeFull();
    configureView();
  }

  @Override
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  public void loadTasks(List<Task> tasks) {
    grid.setItems(tasks);
  }

  private void configureView() {
    configureGrid();
    add(configFilterPanel(), configButtonPanel(), grid);
  }

  private Component configFilterPanel() {
    TaskFilterForm taskFilterForm = new TaskFilterForm();
    taskFilterForm.addListener(TaskFilterForm.FilterChangedEvent.class, this::filter);
    return new Details("Filters", taskFilterForm);
  }

  private Component configButtonPanel() {
    HorizontalLayout layout = new HorizontalLayout();

    Button addButton = new Button("Add", this::add);
    Button deleteButton = new Button("Delete", this::delete);

    layout.add(addButton, deleteButton);
    return layout;
  }

  private void configureGrid() {
    grid.addClassName("tasks-grid");

    grid.setColumns("title", "state", "priority", "type", "dateCreated");
    grid.getColumns().forEach(col -> col.setAutoWidth(true));

    grid.asSingleSelect().addValueChangeListener(event -> {
      fireEvent(new TaskSelectionEvent(this, event.getValue()));
    });
  }

  private void filter(TaskFilterForm.FilterChangedEvent event) {
    grid.setItems(taskService.filter(event.getFilterSpec()));
  }

  private void add(ClickEvent<?> event) {
    // TODO
  }

  private void delete(ClickEvent<?> event) {
    // TODO
  }

  public static class TaskSelectionEvent extends ComponentEvent<TaskListView> {
    private Task selected;

    public TaskSelectionEvent(TaskListView source, Task selected) {
      super(source, false);
      this.selected = selected;
    }

    public Task getSelected() {
      return selected;
    }
  }

}
