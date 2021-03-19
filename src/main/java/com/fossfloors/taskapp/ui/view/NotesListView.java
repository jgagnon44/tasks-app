package com.fossfloors.taskapp.ui.view;

import java.util.Optional;

import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.entity.TaskNote;
import com.fossfloors.taskapp.backend.service.TaskService;
import com.fossfloors.taskapp.ui.form.EditNoteForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route("notes")
@CssImport("./styles/shared-styles.css")
public class NotesListView extends VerticalLayout implements HasUrlParameter<Long> {

  private static final long serialVersionUID = 1L;

  private final TaskService taskService;

  private Div               title;

  private Grid<TaskNote>    grid;

  private Button            addButton;
  private Button            backButton;

  private EditNoteForm      editForm;

  private Task              parentTask;

  public NotesListView(TaskService taskService) {
    this.taskService = taskService;

    addClassName("notes-list-view");
    setSizeFull();

    configureView();

    editForm.addListener(EditNoteForm.SaveEvent.class, this::saveNote);
    editForm.addListener(EditNoteForm.DeleteEvent.class, this::deleteNote);
    editForm.addListener(EditNoteForm.CloseEvent.class, event -> closeEditor());
  }

  @Override
  public void setParameter(BeforeEvent event, Long parameter) {
    Optional<Task> optTask = taskService.findById(parameter);
    optTask.ifPresent(task -> parentTask = task);
    refreshNotes();
    title.setText("Notes: " + parentTask.getTitle());
  }

  @Override
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  private void configureView() {
    title = new Div();
    title.addClassName("page-title");

    HorizontalLayout buttonLayout = new HorizontalLayout();

    addButton = new Button("Add", this::add);
    backButton = new Button("Back");
    backButton.addClickListener(event -> {
      this.getUI().ifPresent(ui -> ui.navigate("main"));
    });

    buttonLayout.add(addButton, backButton);

    configureGrid();
    editForm = new EditNoteForm();

    add(title, buttonLayout, grid, editForm);

    closeEditor();
  }

  private void refreshNotes() {
    if (parentTask != null) {
      grid.setItems(parentTask.getNotes());
    } else {
      grid.setItems();
    }
  }

  // @formatter:off
  private void configureGrid() {
    grid = new Grid<>();
    grid.addClassName("notes-grid");

    grid.addColumn(new LocalDateTimeRenderer<>(TaskNote::getDateCreated, "MM/dd/yyyy HH:mm:ss"))
      .setHeader("Created")
      .setResizable(true)
      .setSortable(true);

    grid.addColumn(TaskNote::getNote)
      .setHeader("Note")
      .setWidth("80%")
      .setResizable(true)
      .setSortable(true);

    grid.asSingleSelect().addValueChangeListener(event -> {
      editNote(event.getValue());
    });
  }
  // @formatter:on

  private void editNote(TaskNote note) {
    if (note != null) {
      editForm.setTaskNote(note);
      editForm.setVisible(true);
      addClassName("editing-note");
    } else {
      closeEditor();
    }
  }

  private void closeEditor() {
    grid.asSingleSelect().clear();
    editForm.setTaskNote(null);
    editForm.setVisible(false);
    removeClassName("editing-note");
    refreshNotes();
  }

  private void add(ClickEvent<?> event) {
    grid.asSingleSelect().clear();
    editNote(new TaskNote());
  }

  private void saveNote(EditNoteForm.SaveEvent event) {
    taskService.saveNote(parentTask, event.getTaskNote());
    closeEditor();
  }

  private void deleteNote(EditNoteForm.DeleteEvent event) {
    // TODO need confirmation
    taskService.deleteNote(parentTask, event.getTaskNote());
    closeEditor();
  }

}
