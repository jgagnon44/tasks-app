package com.fossfloors.taskapp.ui.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fossfloors.taskapp.backend.entity.TaskNote;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/shared-styles.css")
public class EditNoteForm extends VerticalLayout {

  private static final long   serialVersionUID = 1L;

  private static final Logger logger           = LoggerFactory.getLogger(EditNoteForm.class);

  private TextArea            note;

  private Button              saveButton;
  private Button              deleteButton;
  private Button              closeButton;

  private Binder<TaskNote>    binder;

  private TaskNote            taskNote;

  public EditNoteForm() {
    addClassName("edit-view-form");
    setSizeFull();
    configureView();
  }

  @Override
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  public void setTaskNote(TaskNote taskNote) {
    this.taskNote = taskNote;
    binder.readBean(taskNote);
  }

  private void configureView() {
    note = new TextArea("Note");
    note.setSizeFull();

    add(note, configButtons());

    binder = new Binder<>(TaskNote.class);
    binder.bindInstanceFields(this);
  }

  private Component configButtons() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setPadding(false);
    layout.setWidthFull();
    layout.setJustifyContentMode(JustifyContentMode.CENTER);

    saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(event -> validateAndSave());

    deleteButton = new Button("Delete");
    deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, taskNote)));

    closeButton = new Button("Close");
    closeButton.addClickListener(event -> fireEvent(new CloseEvent(this)));
    closeButton.addClickShortcut(Key.ESCAPE);

    layout.add(saveButton, deleteButton, closeButton);
    return layout;
  }

  private void validateAndSave() {
    try {
      binder.writeBean(taskNote);
      fireEvent(new SaveEvent(this, taskNote));
    } catch (ValidationException e) {
      logger.error("Exception", e);
    }
  }

  public static abstract class EditNoteFormEvent extends ComponentEvent<EditNoteForm> {
    private static final long serialVersionUID = 1L;
    private TaskNote          taskNote;

    protected EditNoteFormEvent(EditNoteForm source, TaskNote task) {
      super(source, false);
      this.taskNote = task;
    }

    public TaskNote getTaskNote() {
      return taskNote;
    }
  }

  public static class SaveEvent extends EditNoteFormEvent {
    private static final long serialVersionUID = 1L;

    public SaveEvent(EditNoteForm source, TaskNote note) {
      super(source, note);
    }
  }

  public static class DeleteEvent extends EditNoteFormEvent {
    private static final long serialVersionUID = 1L;

    public DeleteEvent(EditNoteForm source, TaskNote note) {
      super(source, note);
    }
  }

  public static class CloseEvent extends EditNoteFormEvent {
    private static final long serialVersionUID = 1L;

    public CloseEvent(EditNoteForm source) {
      super(source, null);
    }
  }

}
