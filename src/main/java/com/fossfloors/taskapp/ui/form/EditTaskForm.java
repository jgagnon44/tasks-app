package com.fossfloors.taskapp.ui.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fossfloors.taskapp.backend.entity.Task;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.router.Route;

@Route("task")
@CssImport("./styles/shared-styles.css")
@SuppressWarnings("serial")
public class EditTaskForm extends VerticalLayout {

  private static final Logger         logger = LoggerFactory.getLogger(EditTaskForm.class);

  private TextField                   title;
  private TextArea                    description;
  private ComboBox<Task.TaskType>     type;
  private ComboBox<Task.TaskPriority> priority;
  private DatePicker                  dateDue;
  private DatePicker                  dateStarted;
  private DatePicker                  dateCompleted;
  private TextField                   notesCount;

  private Binder<Task>                binder;

  public EditTaskForm() {
    addClassName("edit-task-form");
    setSizeFull();
    configureView();
    bindData();
  }

  private void configureView() {
    add(configTopLayout(), configBottomLayout(), configDatesPanel());
    setDefaultHorizontalComponentAlignment(Alignment.START);
  }

  // @formatter:off
  private void bindData() {
    binder = new Binder<>(Task.class);
    binder.setBean(new Task());
    
    binder.forField(title).bind("title");
    binder.forField(description).bind("description");
    binder.forField(type).bind("type");
    binder.forField(priority).bind("priority");
    binder.forField(notesCount).bind("notesCount");

    binder.forField(dateDue)
      .withConverter(new LocalDateToDateConverter())
      .bind("dateDue");
    
    binder.forField(dateStarted)
      .withConverter(new LocalDateToDateConverter())
      .bind("dateStarted");
    
    binder.forField(dateCompleted)
      .withConverter(new LocalDateToDateConverter())
      .bind("dateCompleted");
  }
  // @formatter:on

  private Component configTopLayout() {
    HorizontalLayout layout = new HorizontalLayout();

    title = new TextField("Title");

    description = new TextArea("Description");
    description.addClassName("task-description");

    layout.add(title, description);
    layout.setDefaultVerticalComponentAlignment(Alignment.START);
    return layout;
  }

  private Component configBottomLayout() {
    HorizontalLayout layout = new HorizontalLayout();

    type = new ComboBox<>("Task Type");
    type.setItems(Task.TaskType.ONE_TIME, Task.TaskType.RECURRING);

    priority = new ComboBox<>("Priority");
    priority.setItems(Task.TaskPriority.LOW, Task.TaskPriority.MEDIUM, Task.TaskPriority.HIGH);

    notesCount = new TextField("Number of Notes");
    notesCount.setReadOnly(true);

    layout.add(type, priority, notesCount);
    return layout;
  }

  private Component configDatesPanel() {
    HorizontalLayout layout = new HorizontalLayout();

    dateDue = new DatePicker("Date Due");
    dateStarted = new DatePicker("Date Started");
    dateCompleted = new DatePicker("Date Completed");

    layout.add(dateDue, dateStarted, dateCompleted);

    Details panel = new Details("Task Dates", layout);
    return panel;
  }

}
