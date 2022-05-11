package com.fossfloors.taskapp.backend.beans;

import java.util.ArrayList;
import java.util.List;

public class ListDelta<T> {

  private List<T> added   = new ArrayList<>();
  private List<T> removed = new ArrayList<>();
  private List<T> changed = new ArrayList<>();

  public T added(T item) {
    added.add(item);
    return item;
  }

  public T removed(T item) {
    removed.add(item);
    return item;
  }

  public T changed(T item) {
    changed.add(item);
    return item;
  }

  public List<T> getAdded() {
    return added;
  }

  public List<T> getRemoved() {
    return removed;
  }

  public List<T> getChanged() {
    return changed;
  }

}
