package com.fossfloors.taskapp.ui.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

@CssImport("./styles/shared-styles.css")
@SuppressWarnings("serial")
public class NoteView extends VerticalLayout {

  private static final Logger logger = LoggerFactory.getLogger(NoteView.class);

  private TextArea            note;

  public NoteView() {
    addClassName("note-view");
    setSizeFull();
    configureView();
  }

  public void loadNote(NotesListView.NoteSelectionEvent event) {
    if (event.getSelected() != null) {
      note.setValue(event.getSelected().getNote());
    } else {
      note.clear();
    }
  }

  private void configureView() {
    note = new TextArea("Note");
    note.setSizeFull();
    add(note);
  }

}
