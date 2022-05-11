package com.fossfloors.taskapp.ui.dialog;

import com.fossfloors.taskapp.ui.form.EditNoteForm;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.shared.Registration;

public class EditNoteDialog extends Dialog {

  private static final long serialVersionUID = 1L;

  private EditNoteForm      form;

  public EditNoteDialog() {
    super();
    configureView();
  }

  @Override
  public <S extends ComponentEvent<?>> Registration addListener(Class<S> eventType,
      ComponentEventListener<S> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  public EditNoteForm getForm() {
    return form;
  }

  private void configureView() {
    form = new EditNoteForm();
    add(form);
  }

}
