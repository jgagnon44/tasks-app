package com.fossfloors.taskapp.ui.form;

import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.entity.Task.State;
import com.fossfloors.taskapp.ui.util.StringToLocalDateTimeConverter;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class EditTaskDetailsForm extends VerticalLayout {

  private static final long       serialVersionUID = 1L;

  private ComboBox<Task.State>    state;
  private ComboBox<Task.Type>     type;
  private ComboBox<Task.Priority> priority;

  private TextField               dateCreated;
  private TextField               dateModified;
  private DatePicker              dateDue;
  private DatePicker              dateStarted;
  private DatePicker              dateCompleted;

  private Binder<Task>            binder;
  private Task                    task;

  public EditTaskDetailsForm() {
    this.addClassName("edit-task-detail-form");
    this.setSizeFull();
    configureView();
    bindData();
  }

  public void setTask(Task task) {
    this.task = task;
    binder.readBean(task);
    updateEnablement();
  }

  private void configureView() {
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

    add(row1, row2, row3);
  }

  // @formatter:off
  private void bindData() {
    binder = new Binder<>(Task.class);
  
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

  private void updateEnablement() {
    if (task != null) {
      type.setEnabled(task.getState() == State.OPEN);
      priority.setEnabled(task.getState() == State.OPEN);
      dateDue.setEnabled(task.getState() == State.OPEN);
      dateStarted.setEnabled(task.getState() == State.OPEN);
      dateCompleted.setEnabled(task.getState() == State.OPEN);
    }
  }

}
