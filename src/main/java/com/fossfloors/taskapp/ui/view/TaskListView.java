package com.fossfloors.taskapp.ui.view;

import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;

import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.entity.Task.TaskState;
import com.fossfloors.taskapp.backend.service.TaskService;
import com.fossfloors.taskapp.ui.form.EditTaskForm;
import com.fossfloors.taskapp.ui.form.TaskFilterForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route("main")
@CssImport("./styles/shared-styles.css")
public class TaskListView extends VerticalLayout {

  private static final long serialVersionUID = 1L;

  private final TaskService taskService;

  private Div               title;

  private Grid<Task>        grid;

  private Button            addButton;
  private Button            filtersButton;

  private TaskFilterForm    taskFilterForm;
  private EditTaskForm      editForm;

  public TaskListView(TaskService taskService) {
    this.taskService = taskService;

    addClassName("task-list-view");
    setSizeFull();

    configureView();

    editForm.addListener(EditTaskForm.SaveEvent.class, this::saveTask);
    editForm.addListener(EditTaskForm.DeleteEvent.class, this::deleteTask);
    editForm.addListener(EditTaskForm.CloseEvent.class, event -> closeEditor());

    taskFilterForm.addListener(TaskFilterForm.FilterChangedEvent.class, this::applyFilter);
    taskFilterForm.addListener(TaskFilterForm.CloseEvent.class, event -> closeFilterPanel());
  }

  @Override
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  @PostConstruct
  private void init() {
    grid.setItems(taskService.findAll());
  }

  private void configureView() {
    title = new Div();
    title.setText("Tasks List");
    title.addClassName("page-title");

    configureGrid();
    editForm = new EditTaskForm();

    add(title, configTopPanel(), grid, editForm);

    closeFilterPanel();
    closeEditor();
  }

  private Component configTopPanel() {
    VerticalLayout layout = new VerticalLayout();
    HorizontalLayout row1 = new HorizontalLayout();

    addButton = new Button("Add", this::add);
    addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    filtersButton = new Button("Filters");
    filtersButton.addClickListener(event -> {
      toggleFilterPanel();
    });

    row1.add(addButton, filtersButton);

    taskFilterForm = new TaskFilterForm();

    layout.add(row1, taskFilterForm);
    return layout;
  }

  // @formatter:off
  private void configureGrid() {
    grid = new Grid<>();
    grid.addClassName("tasks-grid");

    grid.addColumn(Task::getTitle).setHeader("Title");
    grid.addColumn(Task::getState).setHeader("State");
    grid.addColumn(Task::getPriority).setHeader("Priority");
    grid.addColumn(Task::getType).setHeader("Type");

    grid.addColumn(
        bean -> bean.getDateCreated().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")))
        .setHeader("Created");

    grid.getColumns().forEach(col -> {
      col.setAutoWidth(true);
      col.setResizable(true);
      col.setSortable(true);
    });

    grid.asSingleSelect().addValueChangeListener(event -> {
      editTask(event.getValue());
    });
  }
  // @formatter:on

  private void applyFilter(TaskFilterForm.FilterChangedEvent event) {
    grid.setItems(taskService.filter(event.getFilterSpec()));
  }

  private void add(ClickEvent<?> event) {
    grid.asSingleSelect().clear();
    editTask(new Task());
  }

  private void editTask(Task task) {
    if (task != null) {
      editForm.setTask(task);
      editForm.setVisible(true);
      addClassName("editing-task");
    } else {
      closeEditor();
    }
  }

  private void saveTask(EditTaskForm.SaveEvent event) {
    Task task = event.getTask();

    event.getOtherAction().ifPresent(action -> {
      switch (action) {
        case ARCHIVE:
          task.setState(TaskState.ARCHIVED);
          break;
        case CLOSE:
          task.setState(TaskState.CLOSED);
          break;
        case REOPEN:
        case UNARCHIVE:
          task.setState(TaskState.OPEN);
          break;
        default:
          break;
      }
    });

    taskService.save(task);
    taskFilterForm.refresh();
    closeEditor();
  }

  private void deleteTask(EditTaskForm.DeleteEvent event) {
    // TODO need confirmation
    Task task = event.getTask();
    task.setState(TaskState.DELETED);
    taskService.save(task);
    taskFilterForm.refresh();
    closeEditor();
  }

  private void toggleFilterPanel() {
    if (taskFilterForm.isVisible()) {
      closeFilterPanel();
    } else {
      taskFilterForm.setVisible(true);
      addClassName("filter-panel");
    }
  }

  private void closeFilterPanel() {
    taskFilterForm.setVisible(false);
    removeClassName("filter-panel");
  }

  private void closeEditor() {
    grid.asSingleSelect().clear();
    editForm.setTask(null);
    editForm.setVisible(false);
    removeClassName("editing-task");
  }

}
