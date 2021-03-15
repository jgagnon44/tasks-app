package com.fossfloors.taskapp.ui.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

  private static final long       serialVersionUID = 1L;

  private static final String     DEFAULT_FORMAT   = "MM/dd/yyyy HH:mm:ss";

  private final DateTimeFormatter formatter;

  public StringToLocalDateTimeConverter() {
    formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
  }

  public StringToLocalDateTimeConverter(String format) {
    formatter = DateTimeFormatter.ofPattern(format);
  }

  @Override
  public Result<LocalDateTime> convertToModel(String value, ValueContext context) {
    if (value != null && !value.isEmpty()) {
      return Result.ok(LocalDateTime.parse(value, formatter));
    } else {
      return Result.ok(null);
    }
  }

  @Override
  public String convertToPresentation(LocalDateTime value, ValueContext context) {
    if (value != null) {
      return value.format(formatter);
    } else {
      return null;
    }
  }

}