package taskapp.ui.form;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import taskapp.backend.entity.Task;
import taskapp.ui.util.StringToLocalDateTimeConverter;

@CssImport("./styles/shared-styles.css")
public class EditTaskForm extends VerticalLayout {

  private static final long       serialVersionUID = 1L;

  private static final Logger     logger           = LoggerFactory.getLogger(EditTaskForm.class);

  private TextField               title;
  private TextArea                description;

  private ComboBox<Task.State>    state;
  private ComboBox<Task.Type>     type;
  private ComboBox<Task.Priority> priority;

  private TextField               dateCreated;
  private TextField               dateModified;
  private DatePicker              dateDue;
  private DatePicker              dateStarted;
  private DatePicker              dateCompleted;

  private Button                  saveButton;
  private Button                  cancelButton;
  private ComboBox<TaskAction>    otherActions;

  private Binder<Task>            binder;

  private Task                    task;

  private EditNotesForm           notesForm;

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
      notesForm.setTask(task);
    }

    updateActions();
  }

  private void configureView() {
    Div pageTitle = new Div();
    pageTitle.setText("Edit Task");
    pageTitle.addClassName("page-title");

    title = new TextField("Title");
    title.addClassName("task-title");

    description = new TextArea("Description");
    description.addClassName("task-description");

    add(pageTitle, configButtons(), title, description, configDetails(), configNotes());
    setDefaultHorizontalComponentAlignment(Alignment.START);
  }

  private Component configDetails() {
    VerticalLayout layout = new VerticalLayout();

    state = new ComboBox<>("State");
    state.setReadOnly(true);

    type = new ComboBox<>("Task Type");
    type.setItems(Task.Type.ONE_TIME, Task.Type.RECURRING);

    priority = new ComboBox<>("Priority");
    priority.setItems(Task.Priority.LOW, Task.Priority.MEDIUM, Task.Priority.HIGH);

    HorizontalLayout row1 = new HorizontalLayout();
    row1.add(state, type, priority);

    dateDue = new DatePicker("Due");
    dateStarted = new DatePicker("Started");
    dateCompleted = new DatePicker("Completed");

    HorizontalLayout row2 = new HorizontalLayout();
    row2.add(dateDue, dateStarted, dateCompleted);

    dateCreated = new TextField("Created");
    dateCreated.setReadOnly(true);

    dateModified = new TextField("Last Modified");
    dateModified.setReadOnly(true);

    HorizontalLayout row3 = new HorizontalLayout();
    row3.add(dateCreated, dateModified);

    layout.add(row1, row2, row3);

    Details details = new Details();
    details.setSummaryText("Details");
    details.addContent(layout);
    details.addClassName("task-detail");
    details.setOpened(false);

    return details;
  }

  private Component configNotes() {
    notesForm = new EditNotesForm();

    Details details = new Details();
    details.setSummaryText("Notes");
    details.addContent(notesForm);
    details.addClassName("task-notes-detail");
    details.setOpened(false);

    return details;
  }

  private Component configButtons() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setPadding(false);
    layout.setWidthFull();
    layout.setJustifyContentMode(JustifyContentMode.CENTER);

    saveButton = new Button("Save");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.addClickListener(event -> validateAndSave());

    otherActions = new ComboBox<>("Other Actions");

    cancelButton = new Button("Cancel");
    cancelButton.addClickListener(event -> fireEvent(new CancelEvent(this)));
    cancelButton.addClickShortcut(Key.ESCAPE);

    layout.add(otherActions, saveButton, cancelButton);
    layout.setDefaultVerticalComponentAlignment(Alignment.END);
    return layout;
  }

  // @formatter:off
  private void bindData() {
    binder = new Binder<>(Task.class);
  
    binder.forField(title)
      .asRequired("Title required")
      .bind("title");
  
    binder.forField(description).bind("description");
    binder.forField(state).bind("state");
    binder.forField(type).bind("type");
    binder.forField(priority).bind("priority");
  
    binder.forField(dateCreated)
      .withConverter(new StringToLocalDateTimeConverter())
      .bind("dateCreated");
  
    binder.forField(dateModified)
      .withConverter(new StringToLocalDateTimeConverter())
      .bind("dateModified");
  
    binder.forField(dateDue).bind("dateDue");
    binder.forField(dateStarted).bind("dateStarted");
    binder.forField(dateCompleted).bind("dateCompleted");
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

  private void validateAndSave() {
    try {
      binder.writeBean(task);
      fireEvent(new SaveEvent(this, task, Optional.ofNullable(otherActions.getValue())));
    } catch (ValidationException e) {
      // TODO - temporary
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

    public SaveEvent(EditTaskForm source, Task task, Optional<TaskAction> otherAction) {
      super(source, task);
      this.otherAction = otherAction;
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
