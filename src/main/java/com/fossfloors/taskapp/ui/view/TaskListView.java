package com.fossfloors.taskapp.ui.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route("main")
@CssImport("./styles/shared-styles.css")
@SuppressWarnings("serial")
public class TaskListView extends VerticalLayout {

  private static final Logger logger = LoggerFactory.getLogger(TaskListView.class);

  private final TaskService   taskService;

  private Grid<Task>          grid;

  private Button              addButton;

  private TaskFilterForm      taskFilterForm;
  private EditTaskForm        editForm;

  public TaskListView(TaskService taskService) {
    this.taskService = taskService;

    addClassName("task-list-view");
    setSizeFull();

    configureView();

    editForm.addListener(EditTaskForm.SaveEvent.class, this::saveTask);
    editForm.addListener(EditTaskForm.DeleteEvent.class, this::deleteTask);
    editForm.addListener(EditTaskForm.CloseEvent.class, event -> closeEditor());
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
    configureGrid();
    editForm = new EditTaskForm();

    add(configTopPanel(), grid, editForm);

    closeEditor();
  }

  private Component configTopPanel() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setPadding(false);

    addButton = new Button("Add", this::add);
    addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    taskFilterForm = new TaskFilterForm();
    taskFilterForm.addListener(TaskFilterForm.FilterChangedEvent.class, this::filter);

    layout.add(addButton, taskFilterForm);
    layout.setDefaultVerticalComponentAlignment(Alignment.END);
    return layout;
  }

  private void configureGrid() {
    grid = new Grid<>();
    grid.addClassName("tasks-grid");

    grid.addColumn(Task::getTitle).setHeader("Title").setSortable(true);
    grid.addColumn(Task::getState).setHeader("State").setSortable(true);
    grid.addColumn(Task::getPriority).setHeader("Priority").setSortable(true);
    grid.addColumn(Task::getType).setHeader("Type").setSortable(true);

    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    grid.addColumn(bean -> formatter.format(new Date(bean.getDateCreated()))).setHeader("Created")
        .setSortable(true);

    grid.getColumns().forEach(col -> {
      col.setAutoWidth(true);
      col.setResizable(true);
    });

    grid.asSingleSelect().addValueChangeListener(event -> {
      editTask(event.getValue());
    });
  }

  private void filter(TaskFilterForm.FilterChangedEvent event) {
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

  private void closeEditor() {
    grid.asSingleSelect().clear();
    editForm.setTask(null);
    editForm.setVisible(false);
    removeClassName("editing-task");
  }

}
