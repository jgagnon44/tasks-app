package com.fossfloors.taskapp.ui.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class LocalDateToLongConverter implements Converter<LocalDate, Long> {

  private static final long serialVersionUID = 7461070875253826208L;

  private ZoneId            zoneId;

  public LocalDateToLongConverter(ZoneId zoneId) {
    this.zoneId = Objects.requireNonNull(zoneId, "Zone id cannot be null");
  }

  public LocalDateToLongConverter() {
    this(ZoneId.systemDefault());
  }

  @Override
  public Result<Long> convertToModel(LocalDate value, ValueContext context) {
    if (value != null) {
      return Result.ok(Date.from(value.atStartOfDay(zoneId).toInstant()).getTime());
    } else {
      // Null values are allowed.
      return Result.ok(null);
    }
  }

  @Override
  public LocalDate convertToPresentation(Long value, ValueContext context) {
    if (value != null) {
      return Instant.ofEpochMilli(value).atZone(zoneId).toLocalDate();
    } else {
      return null;
    }
  }

}