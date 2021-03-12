package com.fossfloors.taskapp.ui.view;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;

@Route("notes")
@CssImport("./styles/shared-styles.css")
public class NotesListView extends VerticalLayout implements HasUrlParameter<Long> {

  private static final long serialVersionUID = 1L;

  private final TaskService taskService;

  private Grid<TaskNote>    grid;

  private Button            addButton;
  private Button            backButton;

  private EditNoteForm      editForm;

  private Task              parentTask;

  private Div               title;

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
    title.setText(parentTask.getTitle() + ": Notes");
  }

  @Override
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  private void configureView() {
    title = new Div();

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

  private void configureGrid() {
    grid = new Grid<>();
    grid.addClassName("notes-grid");

    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    grid.addColumn(bean -> formatter.format(new Date(bean.getDateCreated()))).setHeader("Created")
        .setSortable(true);

    grid.addColumn(TaskNote::getNote).setHeader("Note").setSortable(true);

    grid.getColumns().forEach(col -> {
      col.setAutoWidth(true);
      col.setResizable(true);
    });

    grid.asSingleSelect().addValueChangeListener(event -> {
      editNote(event.getValue());
    });
  }

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
