package com.fossfloors.taskapp.ui.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.fossfloors.taskapp.backend.beans.TaskFilterSpec;
import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.service.TaskService;
import com.fossfloors.taskapp.ui.dialog.TaskFilterDialog;
import com.fossfloors.taskapp.ui.form.EditTaskForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route("main")
@CssImport("./styles/shared-styles.css")
public class TaskListView extends HorizontalLayout {

  private static final long serialVersionUID = 1L;

  private final TaskService taskService;

  private Grid<Task>        grid;

  private Button            newButton;
  private Button            filtersButton;

  private TaskFilterSpec    filterBean       = new TaskFilterSpec();

  private EditTaskForm      editForm;

  public TaskListView(@Autowired TaskService taskService) {
    this.taskService = taskService;
    this.setSizeFull();
    this.addClassName("task-list-view");
    configureView();

    editForm.addListener(EditTaskForm.SaveEvent.class, this::saveTask);
    editForm.addListener(EditTaskForm.CancelEvent.class, event -> closeEditor());
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
    editForm = new EditTaskForm();
    add(configListPanel(), editForm);
    closeEditor();
  }

  private Component configListPanel() {
    VerticalLayout layout = new VerticalLayout();

    layout.addClassName("task-list-panel");

    Div title = new Div();
    title.setText("Tasks List");
    title.addClassName("page-title");

    configureGrid();
    editForm = new EditTaskForm();

    layout.add(title, configTopPanel(), grid);
    return layout;
  }

  private Component configTopPanel() {
    HorizontalLayout layout = new HorizontalLayout();

    newButton = new Button("New", this::newTask);

    filtersButton = new Button("Filters");
    filtersButton.addClickListener(event -> {
      openFilterDialog();
    });

    layout.add(newButton, filtersButton);
    return layout;
  }

  private void configureGrid() {
    grid = new Grid<>();
    grid.addClassName("tasks-grid");

    grid.addColumn(Task::getTitle).setHeader("Title");
    grid.addColumn(Task::getState).setHeader("State");
    grid.addColumn(Task::getPriority).setHeader("Priority");
    grid.addColumn(Task::getType).setHeader("Type");

    grid.addColumn(new LocalDateTimeRenderer<>(Task::getDateCreated, "MM/dd/yyyy HH:mm:ss"))
        .setHeader("Created");

    grid.addColumn(new LocalDateTimeRenderer<>(Task::getDateModified, "MM/dd/yyyy HH:mm:ss"))
        .setHeader("Last Modified");

    grid.getColumns().forEach(col -> {
      col.setAutoWidth(true);
      col.setResizable(true);
      col.setSortable(true);
    });

    grid.asSingleSelect().addValueChangeListener(event -> {
      editTask(event.getValue());
    });
  }

  private void refreshGrid(TaskFilterSpec filterSpec) {
    grid.setItems(taskService.filter(filterSpec));
  }

  private void applyFilter(TaskFilterDialog.FilterChangedEvent event) {
    refreshGrid(event.getFilterSpec());
  }

  private void newTask(ClickEvent<?> event) {
    editTask(new Task());
    refreshGrid(filterBean);
  }

  private void editTask(Task task) {
    if (task != null) {
      editForm.setTask(task);
      editForm.setVisible(true);
      this.addClassName("editing-task");
    } else {
      closeEditor();
    }
  }

  private void closeEditor() {
    editForm.setTask(null);
    editForm.setVisible(false);
    this.removeClassName("editing-task");
    refreshGrid(filterBean);
  }

  private void saveTask(EditTaskForm.SaveEvent event) {
    Task task = event.getTask();

    event.getOtherAction().ifPresent(action -> {
      switch (action) {
        case ARCHIVE:
          task.setState(Task.State.ARCHIVED);
          break;
        case CLOSE:
          task.setState(Task.State.CLOSED);
          break;
        case REOPEN:
        case UNARCHIVE:
          task.setState(Task.State.OPEN);
          break;
        default:
          break;
      }
    });

    taskService.save(task);

    if (event.getCloseEditor()) {
      closeEditor();
    }
  }

  private void openFilterDialog() {
    TaskFilterDialog form = new TaskFilterDialog(filterBean);
    form.setModal(true);
    form.setDraggable(true);
    form.setCloseOnEsc(true);
    form.setCloseOnOutsideClick(true);

    form.addListener(TaskFilterDialog.FilterChangedEvent.class, this::applyFilter);

    form.open();
  }

}
