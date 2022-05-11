package com.fossfloors.taskapp.ui.form;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fossfloors.taskapp.backend.beans.ListDelta;
import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.entity.Task.State;
import com.fossfloors.taskapp.backend.service.TaskService;
import com.fossfloors.taskapp.backend.entity.TaskNote;
import com.fossfloors.taskapp.ui.dialog.ConfirmDeleteDialog;
import com.fossfloors.taskapp.ui.dialog.EditNoteDialog;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.shared.Registration;

public class EditNotesForm extends VerticalLayout {

  private static final long   serialVersionUID = 1L;

  private static final Logger logger           = LoggerFactory.getLogger(EditNotesForm.class);

  private final TaskService   taskService;

  private Button              newButton;

  private TextField           searchField;
  private Button              searchButton;

  private Task                task;

  private Grid<TaskNote>      grid;

  private EditNoteDialog      editDialog;

  private ConfirmDeleteDialog confirmDeleteDialog;

  private ListDelta<TaskNote> delta            = new ListDelta<>();

  public EditNotesForm(TaskService taskService) {
    this.taskService = taskService;

    this.addClassName("edit-notes-form");
    this.setSizeFull();
    configureView();

    configEditDialog();
    configDeleteConfirmDialog();

    editDialog.getForm().addListener(EditNoteForm.SaveEvent.class, this::saveNote);
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
      grid.setItems(task.getNotes());
      updateEnablement();
    }
  }

  public ListDelta<TaskNote> getTaskNoteDeltas() {
    if (task != null) {
      List<TaskNote> taskNotes = task.getNotes();
      List<TaskNote> gridContents = grid.getDataProvider().fetch(new Query<>())
          .collect(Collectors.toList());
    }

    return delta;
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
        logger.info("delete: {}", note);
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

    grid.addColumn(TaskNote::getNote).setHeader("Note");

    grid.addColumn(new LocalDateTimeRenderer<>(TaskNote::getDateCreated, "MM/dd/yyyy HH:mm:ss"))
        .setHeader("Created");
    grid.addColumn(new LocalDateTimeRenderer<>(TaskNote::getDateModified, "MM/dd/yyyy HH:mm:ss"))
        .setHeader("Last Modified");

    grid.getColumns().forEach(col -> {
      col.setAutoWidth(true);
      col.setResizable(true);
    });

    // grid.asSingleSelect().addValueChangeListener(event -> {
    // editNote(event.getValue());
    // });

    GridContextMenu<TaskNote> menu = grid.addContextMenu();

    menu.addItem("Edit", event -> {
      event.getItem().ifPresent(note -> {
        logger.info("editing note: {}", note);
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

  private void updateEnablement() {
    if (task != null) {
      newButton.setEnabled(task.getState() == State.OPEN);
      searchField.setEnabled(task.getState() == State.OPEN);
      searchButton.setEnabled(task.getState() == State.OPEN);
      grid.setEnabled(task.getState() == State.OPEN);
    }
  }

  private void addNew(ClickEvent<?> event) {
    TaskNote newNote = delta.added(new TaskNote());
    editNote(newNote);
  }

  private void editNote(TaskNote note) {
    editDialog.getForm().setTaskNote(note);
    editDialog.open();
  }

  private void deleteNote(TaskNote note) {
    delta.removed(note);
  }

  private void saveNote(EditNoteForm.SaveEvent event) {
    delta.changed(event.getTaskNote());
  }

}
