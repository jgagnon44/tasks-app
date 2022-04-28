package com.fossfloors.taskapp.backend.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.RegexpValidator;

@SuppressWarnings("serial")
public class PasswordValidator extends RegexpValidator {

  private static final String PATTERN = "\\w(\\w|[!@#$%^&=~]){7,}";

  private transient Matcher   matcher = null;
  private Pattern             pattern;

  public PasswordValidator(String errorMessage) {
    super(errorMessage, PATTERN, true);
    pattern = Pattern.compile(PATTERN);
  }

  @Override
  public ValidationResult apply(String value, ValueContext context) {
    return toResult(value, isValid(value));
  }

  @Override
  protected boolean isValid(String value) {
    if (value == null) {
      return true;
    }

    return getMatcher(value).matches() && validate(value);
  }

  private Matcher getMatcher(String value) {
    if (matcher == null) {
      matcher = pattern.matcher(value);
    } else {
      matcher.reset(value);
    }

    return matcher;
  }

  private boolean validate(String value) {
    int wordCharCount = 0;
    int digitCharCount = 0;
    int specialCharCount = 0;

    for (int i = 0; i < value.length(); i++) {
      Character c = value.charAt(i);

      if (Character.isLetter(c)) {
        wordCharCount++;
      } else if (Character.isDigit(c)) {
        digitCharCount++;
      } else {
        specialCharCount++;
      }
    }

    return (wordCharCount >= 1) && (digitCharCount >= 1) && (specialCharCount >= 1);
  }

}
