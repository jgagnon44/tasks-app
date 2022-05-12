package com.fossfloors.taskapp.ui.form;

import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.entity.Task.State;
import com.fossfloors.taskapp.backend.entity.TaskNote;
import com.fossfloors.taskapp.backend.service.TaskService;
import com.fossfloors.taskapp.ui.dialog.ConfirmDeleteDialog;
import com.fossfloors.taskapp.ui.dialog.EditNoteDialog;
import com.fossfloors.taskapp.util.ApplicationContextHelper;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/shared-styles.css")
public class EditNotesForm extends VerticalLayout {

  private static final long   serialVersionUID = 1L;

  private TaskService         taskService;

  private Button              newButton;

  private TextField           searchField;
  private Button              searchButton;

  private Task                task;

  private Grid<TaskNote>      grid;

  private EditNoteDialog      editDialog;

  private ConfirmDeleteDialog confirmDeleteDialog;

  public EditNotesForm() {
    this.addClassName("edit-notes-form");
    this.setSizeFull();

    taskService = ApplicationContextHelper.getBean(TaskService.class);

    configureView();

    configEditDialog();
    configDeleteConfirmDialog();

    editDialog.getForm().addListener(EditNoteForm.SaveEvent.class, event -> {
      saveNote(event);
      editDialog.close();
    });
    editDialog.getForm().addListener(EditNoteForm.CloseEvent.class, event -> editDialog.close());
  }

  @Override
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  public void setTask(Task task) {
    if (task != null) {
      this.task = task;
      updateGrid();
      updateEnablement();
    }
  }

  private void configureView() {
    add(configButtons(), configGrid());
  }

  private void configEditDialog() {
    editDialog = new EditNoteDialog();
    editDialog.setHeight("50%");
    editDialog.setWidth("50%");

    editDialog.setModal(true);

    editDialog.setResizable(false);
    editDialog.setDraggable(true);

    editDialog.setCloseOnEsc(false);
    editDialog.setCloseOnOutsideClick(false);
  }

  private void configDeleteConfirmDialog() {
    confirmDeleteDialog = new ConfirmDeleteDialog();
    confirmDeleteDialog.setModal(true);
    confirmDeleteDialog.setResizable(false);

    confirmDeleteDialog.setCloseOnEsc(false);
    confirmDeleteDialog.setCloseOnOutsideClick(false);

    confirmDeleteDialog.setMessage("Delete the selected note?");

    confirmDeleteDialog.addListener(ConfirmDeleteDialog.ConfirmEvent.class, event -> {
      // Close dialog.
      confirmDeleteDialog.close();

      // Delete selected note.
      grid.getSelectedItems().forEach(note -> {
        deleteNote(note);
      });
    });

    confirmDeleteDialog.addListener(ConfirmDeleteDialog.CancelEvent.class, event -> {
      // Canceled, close dialog.
      confirmDeleteDialog.close();
    });
  }

  private Component configButtons() {
    HorizontalLayout layout = new HorizontalLayout();

    newButton = new Button("New");
    newButton.addClickListener(this::addNew);

    searchField = new TextField();
    searchField.setPlaceholder("Enter search text");

    searchButton = new Button("Search");
    // TODO implement search

    layout.add(newButton, searchField, searchButton);
    return layout;
  }

  private Component configGrid() {
    grid = new Grid<>();
    grid.addClassName("task-notes-grid");

    grid.addColumn(TaskNote::getId).setHeader("ID");
    grid.addColumn(TaskNote::getNote).setHeader("Note");

    grid.addColumn(new LocalDateTimeRenderer<>(TaskNote::getDateCreated, "MM/dd/yyyy HH:mm:ss"))
        .setHeader("Created");
    grid.addColumn(new LocalDateTimeRenderer<>(TaskNote::getDateModified, "MM/dd/yyyy HH:mm:ss"))
        .setHeader("Last Modified");

    grid.getColumns().forEach(col -> {
      col.setAutoWidth(true);
      col.setResizable(true);
    });

    GridContextMenu<TaskNote> menu = grid.addContextMenu();

    menu.addItem("Edit", event -> {
      event.getItem().ifPresent(note -> {
        editNote(note);
      });
    });

    menu.addItem("Delete", event -> {
      event.getItem().ifPresent(note -> {
        confirmDeleteDialog.open();
      });
    });

    return grid;
  }

  private void updateGrid() {
    grid.setItems(task.getNotes());
  }

  private void updateEnablement() {
    if (task != null) {
      newButton.setEnabled(task.getState() == State.OPEN);
      searchField.setEnabled(task.getState() == State.OPEN);
      searchButton.setEnabled(task.getState() == State.OPEN);
      grid.setEnabled(task.getState() == State.OPEN);
    }
  }

  private void addNew(ClickEvent<?> event) {
    editNote(new TaskNote());
  }

  private void editNote(TaskNote note) {
    editDialog.getForm().setTaskNote(note);
    editDialog.open();
  }

  private void deleteNote(TaskNote note) {
    taskService.deleteNote(task, note);
    updateGrid();
  }

  private void saveNote(EditNoteForm.SaveEvent event) {
    taskService.saveNote(task, event.getTaskNote());
    updateGrid();
  }

}
