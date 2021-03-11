package com.fossfloors.taskapp.ui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class DateStringToLongConverter implements Converter<String, Long> {

  private static final long      serialVersionUID = 1L;

  private static final String    DEFAULT_FORMAT   = "MM/dd/yyyy HH:mm:ss";

  private final SimpleDateFormat formatter;

  public DateStringToLongConverter() {
    formatter = new SimpleDateFormat(DEFAULT_FORMAT);
  }

  public DateStringToLongConverter(String format) {
    formatter = new SimpleDateFormat(format);
  }

  @Override
  public Result<Long> convertToModel(String value, ValueContext context) {
    if (value != null) {
      try {
        return Result.ok(formatter.parse(value).getTime());
      } catch (ParseException e) {
        return Result.error("cannot parse date");
      }
    } else {
      return Result.error("value null");
    }
  }

  @Override
  public String convertToPresentation(Long value, ValueContext context) {
    if (value != null) {
      return formatter.format(Date.from(Instant.ofEpochMilli(value)));
    } else {
      return null;
    }
  }

}