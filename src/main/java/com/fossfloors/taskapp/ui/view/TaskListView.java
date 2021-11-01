package com.fossfloors.taskapp.ui.view;

import javax.annotation.PostConstruct;

import com.fossfloors.taskapp.backend.beans.TaskFilterSpec;
import com.fossfloors.taskapp.backend.entity.Task;
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
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route("main")
@CssImport("./styles/shared-styles.css")
public class TaskListView extends VerticalLayout {

  private static final long serialVersionUID = 1L;

  private final TaskService taskService;

  private Grid<Task>        grid;

  private Button            addButton;
  private Button            editButton;
  private Button            deleteButton;
  private Button            filtersButton;

  private TaskFilterSpec    filterBean       = new TaskFilterSpec();

  private EditTaskForm      editForm;

  public TaskListView(TaskService taskService) {
    this.taskService = taskService;
    this.addClassName("task-list-view");
    this.setSizeFull();
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
    Div title = new Div();
    title.setText("Tasks List");
    title.addClassName("page-title");

    configureGrid();
    editForm = new EditTaskForm();

    add(title, configTopPanel(), grid, editForm);

    closeEditor();
  }

  private Component configTopPanel() {
    HorizontalLayout layout = new HorizontalLayout();

    addButton = new Button("Add", this::add);

    editButton = new Button("Edit", this::edit);
    editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    editButton.setEnabled(false);

    deleteButton = new Button("Delete", this::delete);
    deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    deleteButton.setEnabled(false);

    filtersButton = new Button("Filters");
    filtersButton.addClickListener(event -> {
      openFilterDialog();
    });

    layout.add(addButton, editButton, deleteButton, filtersButton);
    return layout;
  }

  private void configureGrid() {
    grid = new Grid<>();
    grid.addClassName("tasks-grid");

    grid.setSelectionMode(SelectionMode.MULTI);

    grid.addColumn(Task::getTitle).setHeader("Title");
    grid.addColumn(Task::getState).setHeader("State");
    grid.addColumn(Task::getPriority).setHeader("Priority");
    grid.addColumn(Task::getType).setHeader("Type");

    grid.addColumn(new LocalDateTimeRenderer<>(Task::getDateCreated, "MM/dd/yyyy HH:mm:ss"))
        .setHeader("Created");

    grid.getColumns().forEach(col -> {
      col.setAutoWidth(true);
      col.setResizable(true);
      col.setSortable(true);
    });

    grid.asMultiSelect().addSelectionListener(event -> {
      editButton.setEnabled(event.getAllSelectedItems().size() == 1);
      deleteButton.setEnabled(!event.getAllSelectedItems().isEmpty());
    });
  }

  private void refreshGrid(TaskFilterSpec filterSpec) {
    grid.setItems(taskService.filter(filterSpec));
  }

  private void applyFilter(TaskFilterForm.FilterChangedEvent event) {
    refreshGrid(event.getFilterSpec());
  }

  private void add(ClickEvent<?> event) {
    editTask(new Task());
    refreshGrid(filterBean);
  }

  private void edit(ClickEvent<?> event) {
    grid.getSelectedItems().forEach(task -> {
      editTask(task);
      grid.deselect(task);
    });
  }

  private void delete(ClickEvent<?> event) {
    grid.getSelectedItems().forEach(task -> {
      deleteTask(task);
    });

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

  private void deleteTask(Task task) {
    task.setState(Task.State.DELETED);
    taskService.save(task);
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
    closeEditor();
  }

  private void deleteTask(EditTaskForm.DeleteEvent event) {
    // TODO need confirmation
    Task task = event.getTask();
    task.setState(Task.State.DELETED);
    taskService.save(task);
    closeEditor();
  }

  private void openFilterDialog() {
    TaskFilterForm form = new TaskFilterForm(filterBean);
    form.addListener(TaskFilterForm.FilterChangedEvent.class, this::applyFilter);

    Dialog dialog = new Dialog(form);
    dialog.setModal(true);
    dialog.setDraggable(true);
    dialog.setCloseOnOutsideClick(true);
    dialog.setCloseOnEsc(true);

    dialog.open();
  }

  private void closeEditor() {
    editForm.setTask(null);
    editForm.setVisible(false);
    this.removeClassName("editing-task");
  }

}
