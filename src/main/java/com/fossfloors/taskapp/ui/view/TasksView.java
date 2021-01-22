package com.fossfloors.taskapp.ui.view;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fossfloors.taskapp.backend.service.TaskNoteService;
import com.fossfloors.taskapp.backend.service.TaskService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.router.Route;

@Route("main")
@CssImport("./styles/shared-styles.css")
@SuppressWarnings("serial")
public class TasksView extends VerticalLayout {

  private static final Logger   logger = LoggerFactory.getLogger(TasksView.class);

  private final TaskService     taskService;
  private final TaskNoteService noteService;

  private TaskListView          taskListView;
  private NotesListView         notesListView;
  private NoteView              noteView;

  public TasksView(TaskService taskService, TaskNoteService noteService) {
    this.taskService = taskService;
    this.noteService = noteService;

    addClassName("tasks-view");
    setSizeFull();
    configureView();
  }

  @PostConstruct
  private void init() {
    taskListView.loadTasks(taskService.findAll());
  }

  private void configureView() {
    SplitLayout layout = new SplitLayout();
    layout.setOrientation(Orientation.VERTICAL);
    layout.setSizeFull();

    taskListView = new TaskListView(taskService);

    layout.addToPrimary(taskListView);
    layout.addToSecondary(configNotesView());
    layout.setSplitterPosition(60);

    add(layout);

    taskListView.addListener(TaskListView.TaskSelectionEvent.class, notesListView::loadNotes);
  }

  private Component configNotesView() {
    SplitLayout layout = new SplitLayout();
    layout.setOrientation(Orientation.HORIZONTAL);
    layout.setWidthFull();

    notesListView = new NotesListView(noteService);
    noteView = new NoteView();

    notesListView.addListener(NotesListView.NoteSelectionEvent.class, noteView::loadNote);

    layout.addToPrimary(notesListView);
    layout.addToSecondary(noteView);
    layout.setSplitterPosition(40);
    return layout;
  }

}
