package com.fossfloors.taskapp.ui.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.ui.util.DateStringToLongConverter;
import com.fossfloors.taskapp.ui.util.LocalDateToLongConverter;
import com.fossfloors.taskapp.ui.view.NotesListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/shared-styles.css")
@SuppressWarnings("serial")
public class EditTaskForm extends VerticalLayout {

  private static final Logger         logger = LoggerFactory.getLogger(EditTaskForm.class);

  private TextField                   title;
  private TextArea                    description;
  private ComboBox<Task.TaskType>     type;
  private ComboBox<Task.TaskPriority> priority;
  private TextField                   notesCount;
  private TextField                   dateCreated;
  private TextField                   dateModified;
  private DatePicker                  dateDue;
  private DatePicker                  dateStarted;
  private DatePicker                  dateCompleted;

  private Button                      editNotesButton;
  private Button                      saveButton;
  private Button                      deleteButton;
  private Button                      cancelButton;

  private Binder<Task>                binder;

  private Task                        task;

  public EditTaskForm() {
    addClassName("edit-task-form");
    setSizeFull();
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
  }

  private void configureView() {
    add(configDetailsPane(), configButtons());
    setDefaultHorizontalComponentAlignment(Alignment.START);
  }

  // @formatter:off
  private void bindData() {
    binder = new Binder<>(Task.class);

    binder.forField(title)
      .asRequired("Title required")
      .bind("title");

    binder.forField(description).bind("description");
    binder.forField(type).bind("type");
    binder.forField(priority).bind("priority");

    binder.forField(notesCount).bind(task -> {
      return String.valueOf(task.getNotes().size());
    }, (task, value) -> {});

    binder.forField(dateCreated)
      .withConverter(new DateStringToLongConverter())
      .bind("dateCreated");

    binder.forField(dateModified)
      .withConverter(new DateStringToLongConverter())
      .bind("dateModified");

    binder.forField(dateDue)
      .withConverter(new LocalDateToLongConverter())
      .bind("dateDue");

    binder.forField(dateStarted)
      .withConverter(new LocalDateToLongConverter())
      .bind("dateStarted");

    binder.forField(dateCompleted)
      .withConverter(new LocalDateToLongConverter())
      .bind("dateCompleted");
  }
  // @formatter:on

  private Component configDetailsPane() {
    VerticalLayout layout = new VerticalLayout();
    layout.setPadding(false);

    title = new TextField("Title");
    title.setWidthFull();

    description = new TextArea("Description");
    description.addClassName("task-description");
    description.setWidthFull();

    HorizontalLayout row1 = new HorizontalLayout();

    type = new ComboBox<>("Task Type");
    type.setItems(Task.TaskType.ONE_TIME, Task.TaskType.RECURRING);

    priority = new ComboBox<>("Priority");
    priority.setItems(Task.TaskPriority.LOW, Task.TaskPriority.MEDIUM, Task.TaskPriority.HIGH);

    notesCount = new TextField("Notes Count");
    notesCount.setReadOnly(true);

    editNotesButton = new Button("Edit Notes");
    editNotesButton.addClickListener(event -> {
      String route = RouteConfiguration.forSessionScope().getUrl(NotesListView.class, task.getId());
      this.getUI().ifPresent(ui -> ui.navigate(route));
    });

    row1.add(type, priority, notesCount, editNotesButton);
    row1.setDefaultVerticalComponentAlignment(Alignment.END);

    HorizontalLayout row2 = new HorizontalLayout();

    dateCreated = new TextField("Created");
    dateCreated.setReadOnly(true);

    dateModified = new TextField("Last Modified");
    dateModified.setReadOnly(true);

    dateDue = new DatePicker("Due");
    dateStarted = new DatePicker("Started");
    dateCompleted = new DatePicker("Completed");

    row2.add(dateCreated, dateModified, dateDue, dateStarted, dateCompleted);

    layout.add(title, description, row1, row2);
    return layout;
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
    deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, task)));

    cancelButton = new Button("Cancel");
    cancelButton.addClickListener(event -> fireEvent(new CancelEvent(this)));
    cancelButton.addClickShortcut(Key.ESCAPE);

    layout.add(saveButton, deleteButton, cancelButton);
    return layout;
  }

  private void validateAndSave() {
    try {
      binder.writeBean(task);
      fireEvent(new SaveEvent(this, task));
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
    private Task task;

    protected EditTaskFormEvent(EditTaskForm source, Task task) {
      super(source, false);
      this.task = task;
    }

    public Task getTask() {
      return task;
    }
  }

  public static class SaveEvent extends EditTaskFormEvent {
    public SaveEvent(EditTaskForm source, Task task) {
      super(source, task);
    }
  }

  public static class DeleteEvent extends EditTaskFormEvent {
    public DeleteEvent(EditTaskForm source, Task task) {
      super(source, task);
    }
  }

  public static class CancelEvent extends EditTaskFormEvent {
    public CancelEvent(EditTaskForm source) {
      super(source, null);
    }
  }

}
