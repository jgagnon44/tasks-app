package taskapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

  private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public static Date now() {
    return new Date(System.currentTimeMillis());
  }

  public static String format(final Date date) {
    if (date == null) {
      return "null";
    }

    return FORMAT.format(date);
  }

  public static String format(final long dateMs) {
    if (dateMs <= 0) {
      return "null";
    }

    Date date = new Date(dateMs);
    return FORMAT.format(date);
  }

}
