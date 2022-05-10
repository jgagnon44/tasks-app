package com.fossfloors.taskapp.ui.form;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.entity.Task.State;
import com.fossfloors.taskapp.ui.util.PagedTabs;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/shared-styles.css")
public class EditTaskForm extends VerticalLayout {

  private static final long    serialVersionUID = 1L;

  private static final Logger  logger           = LoggerFactory.getLogger(EditTaskForm.class);

  private TextField            title;
  private TextArea             description;

  private Button               saveButton;
  private Button               saveAndCloseButton;
  private Button               closeButton;
  private ComboBox<TaskAction> otherActions;

  private Binder<Task>         binder;
  private Task                 task;

  private EditTaskDetailsForm  detailsForm;
  private EditNotesForm        notesForm;

  public enum TaskAction {
    CLOSE, REOPEN, ARCHIVE, UNARCHIVE
  }

  public EditTaskForm() {
    this.addClassName("edit-task-form");
    this.setSizeFull();
    configureView();
    bindData();
  }

  @Override
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  public void setTask(Task task) {
    this.task = task;
    binder.readBean(task);

    if (task != null) {
      detailsForm.setTask(task);
      notesForm.setTask(task);
    }

    updateActions();
    updateEnablement();
  }

  private void configureView() {
    Div pageTitle = new Div();
    pageTitle.setText("Edit Task");
    pageTitle.addClassName("page-title");

    title = new TextField("Title");
    title.addClassName("task-title");

    description = new TextArea("Description");
    description.addClassName("task-description");

    PagedTabs tabs = new PagedTabs(this);
    tabs.getContent().addClassName("paged-tabs-titles");

    add(pageTitle, configButtons(), title, description, tabs);
    setDefaultHorizontalComponentAlignment(Alignment.START);

    detailsForm = new EditTaskDetailsForm();
    tabs.add("Details", detailsForm, false);

    notesForm = new EditNotesForm();
    tabs.add("Notes", notesForm, false);
  }

  private Component configButtons() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setPadding(false);
    layout.setWidthFull();
    layout.setJustifyContentMode(JustifyContentMode.CENTER);

    otherActions = new ComboBox<>("Other Actions");

    saveAndCloseButton = new Button("Save & Close");
    saveAndCloseButton.addClickListener(event -> validateAndSave(true));

    saveButton = new Button("Save");
    saveButton.addClickListener(event -> validateAndSave(false));

    closeButton = new Button("Close");
    closeButton.addClickListener(event -> fireEvent(new CancelEvent(this)));
    closeButton.addClickShortcut(Key.ESCAPE);

    layout.add(otherActions, saveAndCloseButton, saveButton, closeButton);
    layout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
    return layout;
  }

  // @formatter:off
  private void bindData() {
    binder = new Binder<>(Task.class);
  
    binder.forField(title)
      .asRequired("Title required")
      .bind("title");
  
    binder.forField(description).bind("description");
  }
  // @formatter:on

  private void updateActions() {
    if (task != null) {
      switch (task.getState()) {
        case CLOSED:
          otherActions.setItems(TaskAction.REOPEN, TaskAction.ARCHIVE);
          break;
        case OPEN:
          otherActions.setItems(TaskAction.CLOSE);
          break;
        case ARCHIVED:
          otherActions.setItems(TaskAction.UNARCHIVE);
          break;
        default:
          break;
      }
    }
  }

  private void updateEnablement() {
    if (task != null) {
      title.setEnabled(task.getState() == State.OPEN);
      description.setEnabled(task.getState() == State.OPEN);
    }
  }

  private void validateAndSave(boolean close) {
    try {
      detailsForm.applyChanges();
      notesForm.applyChanges();

      binder.writeBean(task);
      fireEvent(new SaveEvent(this, task, close, Optional.ofNullable(otherActions.getValue())));
    } catch (ValidationException e) {
      // TODO - future development
      logger.error("field validation errors:");
      e.getFieldValidationErrors().forEach(er -> {
        logger.error("status: {}", er.getStatus());
        logger.error("message: {}", er.getMessage().get());
      });

      logger.error("bean validation errors:");
      e.getBeanValidationErrors().forEach(er -> {
        logger.error(er.getErrorMessage());
      });
    }
  }

  public static abstract class EditTaskFormEvent extends ComponentEvent<EditTaskForm> {
    private static final long serialVersionUID = 1L;
    private Task              task;

    protected EditTaskFormEvent(EditTaskForm source, Task task) {
      super(source, false);
      this.task = task;
    }

    public Task getTask() {
      return task;
    }
  }

  public static class SaveEvent extends EditTaskFormEvent {
    private static final long    serialVersionUID = 1L;
    private Optional<TaskAction> otherAction;
    private boolean              closeEditor;

    public SaveEvent(EditTaskForm source, Task task, boolean closeEditor,
        Optional<TaskAction> otherAction) {
      super(source, task);
      this.closeEditor = closeEditor;
      this.otherAction = otherAction;
    }

    public boolean getCloseEditor() {
      return closeEditor;
    }

    public Optional<TaskAction> getOtherAction() {
      return otherAction;
    }
  }

  public static class CancelEvent extends EditTaskFormEvent {
    private static final long serialVersionUID = 1L;

    public CancelEvent(EditTaskForm source) {
      super(source, null);
    }
  }

}
