package taskapp.ui.dialog;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public class ConfirmDeleteDialog extends Dialog {

  private static final long serialVersionUID = 1L;

  private Span              messageText;

  public ConfirmDeleteDialog() {
    super();
    configureView();
  }

  @Override
  public <S extends ComponentEvent<?>> Registration addListener(Class<S> eventType,
      ComponentEventListener<S> listener) {
    return getEventBus().addListener(eventType, listener);
  }

  public void setMessage(String message) {
    messageText.setText(message);
  }

  private void configureView() {
    VerticalLayout layout = new VerticalLayout();
    layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

    messageText = new Span();

    layout.add(messageText, configButtons());

    add(layout);
  }

  private Component configButtons() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.setSizeFull();
    layout.setJustifyContentMode(JustifyContentMode.CENTER);

    Button okButton = new Button("OK");
    okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    okButton.addClickListener(event -> fireEvent(new ConfirmEvent<>(this)));

    Button cancelButton = new Button("Cancel");
    cancelButton.addClickListener(event -> fireEvent(new CancelEvent<>(this)));

    layout.add(okButton, cancelButton);
    return layout;
  }

  public static abstract class ConfirmDeleteFormEvent extends ComponentEvent<ConfirmDeleteDialog> {
    private static final long serialVersionUID = 1L;

    protected ConfirmDeleteFormEvent(ConfirmDeleteDialog source) {
      super(source, false);
    }
  }

  public static class ConfirmEvent<T> extends ConfirmDeleteFormEvent {
    private static final long serialVersionUID = 1L;

    public ConfirmEvent(ConfirmDeleteDialog source) {
      super(source);
    }
  }

  public static class CancelEvent<T> extends ConfirmDeleteFormEvent {
    private static final long serialVersionUID = 1L;

    public CancelEvent(ConfirmDeleteDialog source) {
      super(source);
    }
  }

}
