package com.fossfloors.taskapp.ui.form;

import com.fossfloors.taskapp.backend.beans.TaskFilterSpec;
import com.fossfloors.taskapp.backend.entity.Task;
import com.fossfloors.taskapp.backend.entity.Task.TaskState;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

@CssImport("./styles/shared-styles.css")
public class TaskFilterForm extends VerticalLayout {

  private static final long           serialVersionUID = 1L;

  private TextField                   titleFilter;
  private TextField                   descriptionFilter;

  private ComboBox<Task.TaskState>    stateFilter;
  private ComboBox<Task.TaskPriority> priorityFilter;
  private ComboBox<Task.TaskType>     typeFilter;

  private Button                      applyButton;
  private Button                      resetButton;
  private Button                      closeButton;

  private Binder<TaskFilterSpec>      binder;
  private TaskFilterSpec              filterBean       = new TaskFilterSpec();

  public TaskFilterForm() {
    addClassName("task-filter-view");
    configureView();
  }

  @Override
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  public void refresh() {
    fireEvent(new FilterChangedEvent(this, binder.getBean()));
  }

  private void configureView() {
    HorizontalLayout row1 = new HorizontalLayout();

    titleFilter = new TextField("Title");
    descriptionFilter = new TextField("Description");

    row1.add(titleFilter, descriptionFilter);

    HorizontalLayout row2 = new HorizontalLayout();

    stateFilter = new ComboBox<>("Task State");
    stateFilter.setItems(TaskState.ALL, TaskState.OPEN, TaskState.CLOSED, TaskState.ARCHIVED);

    priorityFilter = new ComboBox<>("Priority");
    priorityFilter.setItems(Task.TaskPriority.values());

    typeFilter = new ComboBox<>("Type");
    typeFilter.setItems(Task.TaskType.values());

    row2.add(stateFilter, priorityFilter, typeFilter);

    HorizontalLayout row3 = new HorizontalLayout();
    row3.setAlignItems(Alignment.CENTER);

    applyButton = new Button("Apply");
    applyButton.addClickListener(event -> refresh());

    resetButton = new Button("Reset");
    resetButton.addClickListener(event -> {
      filterBean.reset();
      binder.readBean(filterBean);
    });

    closeButton = new Button("Close");
    closeButton.addClickListener(event -> {
      refresh();
      fireEvent(new CloseEvent(this));
    });

    row3.add(applyButton, resetButton, closeButton);

    add(row1, row2, row3);

    binder = new Binder<>(TaskFilterSpec.class);
    binder.setBean(filterBean);
    binder.bindInstanceFields(this);
  }

  public static abstract class TaskFilterEvent extends ComponentEvent<TaskFilterForm> {
    private static final long serialVersionUID = 1L;

    private TaskFilterSpec    filterSpec;

    protected TaskFilterEvent(TaskFilterForm source, TaskFilterSpec filterSpec) {
      super(source, false);
      this.filterSpec = filterSpec;
    }

    public TaskFilterSpec getFilterSpec() {
      return filterSpec;
    }
  }

  public static class FilterChangedEvent extends TaskFilterEvent {
    private static final long serialVersionUID = 1L;

    public FilterChangedEvent(TaskFilterForm source, TaskFilterSpec filterSpec) {
      super(source, filterSpec);
    }
  }

  public static class CloseEvent extends TaskFilterEvent {
    private static final long serialVersionUID = 1L;

    public CloseEvent(TaskFilterForm source) {
      super(source, null);
    }
  }

}
