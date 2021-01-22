package com.fossfloors.taskapp.ui.view;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fossfloors.taskapp.backend.entity.TaskNote;
import com.fossfloors.taskapp.backend.service.TaskNoteService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/shared-styles.css")
@SuppressWarnings("serial")
public class NotesListView extends VerticalLayout {

  private static final Logger   logger = LoggerFactory.getLogger(NotesListView.class);

  private final TaskNoteService noteService;

  private Grid<TaskNote>        grid   = new Grid<>(TaskNote.class);

  public NotesListView(TaskNoteService noteService) {
    this.noteService = noteService;
    addClassName("notes-list-view");
    setSizeFull();
    configureView();
  }

  @Override
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  public void loadNotes(TaskListView.TaskSelectionEvent event) {
    if (event.getSelected() != null) {
      grid.setItems(event.getSelected().getNotes());
    } else {
      grid.setItems(new ArrayList<>());
    }
  }

  private void configureView() {
    configureGrid();
    add(configButtonPanel(), grid);
  }

  private Component configButtonPanel() {
    HorizontalLayout layout = new HorizontalLayout();

    Button addButton = new Button("Add", this::add);
    Button deleteButton = new Button("Delete", this::delete);

    layout.add(addButton, deleteButton);
    return layout;
  }

  private void configureGrid() {
    grid.addClassName("notes-grid");

    grid.setColumns("note", "dateCreated");
    grid.getColumns().forEach(col -> col.setAutoWidth(true));

    grid.asSingleSelect().addValueChangeListener(event -> {
      fireEvent(new NoteSelectionEvent(this, event.getValue()));
    });
  }

  private void add(ClickEvent<?> event) {
    // TODO
  }

  private void delete(ClickEvent<?> event) {
    // TODO
  }

  public static class NoteSelectionEvent extends ComponentEvent<NotesListView> {
    private TaskNote selected;

    public NoteSelectionEvent(NotesListView source, TaskNote selected) {
      super(source, false);
      this.selected = selected;
    }

    public TaskNote getSelected() {
      return selected;
    }
  }

}
