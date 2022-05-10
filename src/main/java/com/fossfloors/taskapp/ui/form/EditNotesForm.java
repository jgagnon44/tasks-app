package com.fossfloors.taskapp.ui.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.entity.TaskNote;
import com.fossfloors.taskapp.backend.entity.Task.State;
import com.fossfloors.taskapp.ui.dialog.ConfirmDeleteDialog;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;

public class EditNotesForm extends VerticalLayout {

  private static final long   serialVersionUID = 1L;

  private static final Logger logger           = LoggerFactory.getLogger(EditNotesForm.class);

  private Button              newButton;

  private TextField           searchField;
  private Button              searchButton;

  private Task                task;

  private Grid<TaskNote>      grid;

  private Dialog              editDialog;

  private ConfirmDeleteDialog confirmDeleteDialog;

  public EditNotesForm() {
    this.addClassName("edit-notes-form");
    this.setSizeFull();
    configureView();

    configEditDialog();
    configDeleteConfirmDialog();
  }

  public void setTask(Task task) {
    if (task != null) {
      this.task = task;
      grid.setItems(task.getNotes());
      updateEnablement();
    }
  }

  public void applyChanges() throws ValidationException {
    // TODO
  }

  private void configureView() {
    add(configButtons(), configGrid());
  }

  private void configEditDialog() {
    editDialog = new Dialog();
    editDialog.setSizeFull();
    editDialog.setModal(true);
    editDialog.setResizable(false);

    editDialog.add(new EditNoteForm());
  }

  private void configDeleteConfirmDialog() {
    confirmDeleteDialog = new ConfirmDeleteDialog();
    confirmDeleteDialog.setModal(true);
    confirmDeleteDialog.setResizable(false);

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
    editNote(new TaskNote());
  }

  private void editNote(TaskNote note) {
    editDialog.open();
  }

  private void deleteNote(TaskNote note) {
    // TODO process the deletion of the note
  }

}
