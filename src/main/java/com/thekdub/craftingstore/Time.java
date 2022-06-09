package com.thekdub.craftingstore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Time utility class for ServerElements.
 */
public class Time {

  /**
   * An array of month lengths, indexed by month number starting from 0 (January) to 11 (December).
   */
  private static final int[] monthLengths = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

  /**
   * Converts the passed milliseconds to the passed date format.
   * Uses America/New_York timezone.
   * Example date format: "MM-dd-yyyy HH:mm"
   * <p>
   * Text: 4+ provides full-form, otherwise abbreviated or short form used.
   * Number: Number of letters is the minimum number of digits
   * Month: 3+ letters for text, otherwise numeric
   * <p>
   * Char | Label | Type | Examples
   * G | Era | Text | BC or AD
   * y | Year | Year | 1996, 96
   * Y | Week year | Year | 2009, 09
   * M | Month in year | Month | July; Jul; 07
   * w | Week in year | Number | 27
   * W | Week in month | Number | 2
   * D | Day in year | Number | 189
   * d | Day in month | Number | 10
   * F | Day of week in month | Number | 2
   * E | Day name in week | Text | Tuesday; Tue
   * u | Day number of week | Number | 1 (Mon) ... 7 (Sun)
   * a | AM/PM marker | Text | PM
   * H | Hour in day (0-23) | Number | 0
   * k | Hour in day (1-24) | Number | 24
   * K | Hour in am/pm (0-11) | Number | 0
   * h | Hour in am/pm (1-12) | Number | 12
   * m | Minute in hour | Number | 30
   * s | Second in minute | Number | 55
   * S | Millisecond | Number | 978
   * z | Time zone | General time zone | Pacific Standard Time; PST; GMT-08:00 (4x, 1x, 2-3x)
   * Z | Time zone | RFC 822 time zone | -0800
   * X | Time zone | ISO 8601 time zone | -08, -0800, -08:00 (1x, 2x, 3x)
   *
   * @param millis     the date time milliseconds.
   * @param dateFormat the date format to be used.
   * @return a string representing the milliseconds in the passed date format.
   */
  public static String millisToDateTime(long millis, String dateFormat) {
    // "MM-dd-yyyy HH:mm" = MonthMonth-DayDay-YearYearYearYear HourHour:MinuteMinute
    DateFormat format = new SimpleDateFormat(dateFormat);
    format.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    Date time = new Date(millis);
    return format.format(time);
  }

  /**
   * Converts the passed milliseconds to a condensed readable format.
   * Uses #y#mo#w#d#h#m formatting, sections &lt;1 are omitted. &lt;1m if less than 1 minute.
   *
   * @param millis the time in milliseconds to convert.
   * @return a condensed readable time string.
   */
  public static String millisToShortReadable(long millis) {
    long years = millis / 31536000000L;
    millis -= years * 31536000000L;
    long months = millis / 2628000000L;
    millis -= months * 2628000000L;
    long weeks = millis / 604800000L;
    millis -= weeks * 604800000L;
    long days = millis / 86400000L;
    millis -= days * 86400000L;
    long hours = millis / 3600000L;
    millis -= hours * 3600000L;
    long minutes = millis / 60000L;
    String output = (years > 0 ? years + "y" : "") + (months > 0 ? months + "mo" : "") +
          (weeks > 0 ? weeks + "w" : "") + (days > 0 ? days + "d" : "") + (hours > 0 ? hours + "h" : "") +
          (minutes > 0 ? minutes + "m" : "");
    return output.equals("") ? "<1m" : output;
  }

  /**
   * Converts the passed milliseconds to a readable format.
   * Uses # Year, # Month, # Week, # Day, # Hour, # Minute formatting, sections &lt;1 are omitted. Sections &lt;1 are plural.
   * &lt; 1 Minute if less than 1 minute.
   *
   * @param millis the time in milliseconds to convert.
   * @return a readable time string.
   */
  public static String millisToReadable(long millis) {
    long y, mo, w, d, h, m;
    String t = ((y = millis / 31536000000L) > 0 ? y + " Year" + (y > 1 ? "s" : "") : "") +
          ((mo = (millis -= y * 31536000000L) / 2628000000L) > 0 ? (y > 0 ? ", " : "") + mo + " Month" + (mo > 1 ? "s" : "") : "") +
          ((w = (millis -= mo * 2628000000L) / 604800000L) > 0 ? (mo + y > 0 ? ", " : "") + w + " Week" + (w > 1 ? "s" : "") : "") +
          ((d = (millis -= w * 604800000L) / 86400000L) > 0 ? (w + mo + y > 0 ? ", " : "") + d + " Day" + (d > 1 ? "s" : "") : "") +
          ((h = (millis -= d * 86400000L) / 3600000L) > 0 ? (d + w + mo + y > 0 ? ", " : "") + h + " Hour" + (h > 1 ? "s" : "") : "") +
          ((m = (millis -= h * 3600000L) / 60000L) > 0 ? (h + d + w + mo + y > 0 ? ", " : "") + m + " Minute" + (m > 1 ? "s" : "") : "") +
          (m + h + d + w + mo + y == 0 ? "< 1 Minute" : "");
    return t;
  }

  /**
   * Converts the passed milliseconds to a more precise condensed readable format.
   * Uses #y#mo#w#d#h#m#s formatting, sections &lt;1 are omitted. &lt;1s if less than 1 second.
   *
   * @param millis the time in milliseconds to convert.
   * @return a more precise condensed readable time string.
   */
  public static String millisToShortReadablePrecise(long millis) {
    long years = millis / 31536000000L;
    millis -= years * 31536000000L;
    long months = millis / 2628000000L;
    millis -= months * 2628000000L;
    long weeks = millis / 604800000L;
    millis -= weeks * 604800000L;
    long days = millis / 86400000L;
    millis -= days * 86400000L;
    long hours = millis / 3600000L;
    millis -= hours * 3600000L;
    long minutes = millis / 60000L;
    millis -= minutes * 60000L;
    long seconds = millis / 1000L;
    String output = (years > 0 ? years + "y" : "") + (months > 0 ? months + "mo" : "") +
          (weeks > 0 ? weeks + "w" : "") + (days > 0 ? days + "d" : "") + (hours > 0 ? hours + "h" : "") +
          (minutes > 0 ? minutes + "m" : "") + (seconds > 0 ? seconds + "s" : "");
    return output.equals("") ? "<1s" : output;
  }

  /**
   * Converts the passed milliseconds to a more precise readable format.
   * Uses # Year, # Month, # Week, # Day, # Hour, # Minute, # Second formatting, sections &lt;1 are omitted..
   * Sections &gt;1 are plural. &lt; 1 Second if less than 1 second.
   *
   * @param millis the time in milliseconds to convert.
   * @return a more precise readable time string.
   */
  public static String millisToReadablePrecise(long millis) {
    long milli = millis;
    long years = milli / 31536000000L;
    milli -= years * 31536000000L;
    long months = milli / 2628000000L;
    milli -= months * 2628000000L;
    long weeks = milli / 604800000L;
    milli -= weeks * 604800000L;
    long days = milli / 86400000L;
    milli -= days * 86400000L;
    long hours = milli / 3600000L;
    milli -= hours * 3600000L;
    long minutes = milli / 60000L;
    millis -= minutes * 60000L;
    long seconds = millis / 1000L;
    String output = (years > 0 ? years + " Year" + (years > 1 ? "s" : "") : "") +
          (months > 0 ? (millis > 31536000000L ? ", " : "") + months + " Month" + (months != 1 ? "s" : "") : "") +
          (weeks > 0 ? (millis > 2628000000L ? ", " : "") + weeks + " Week" + (weeks != 1 ? "s" : "") : "") +
          (days > 0 ? (millis > 604800000L ? ", " : "") + days + " Day" + (days != 1 ? "s" : "") : "") +
          (hours > 0 ? (millis > 86400000L ? ", " : "") + hours + " Hour" + (hours != 1 ? "s" : "") : "") +
          (minutes > 0 ? (millis > 3600000L ? ", " : "") + minutes + " Minute" + (minutes != 1 ? "s" : "") : "") +
          (seconds > 0 ? (millis > 60000L ? ", " : "") + seconds + " Second" + (seconds != 1 ? "s" : "") : "");
    return output.equals("") ? "< 1 Second" : output;
  }

  /**
   * Parses a time string in the format using #y, #mo, #w, #d, #h, #m, and/or #s consecutively to milliseconds.
   * Example: 3y5w2h35m for 3 years, 5 weeks, 2 hours, 35 minutes.
   *
   * @param time the time string to parse.
   * @return the millisecond equivalent of the time string.
   */
  public static long parseTimeStr(String time) {
    if (!isTimeStr(time)) {
      return -1;
    }
    Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:" +
          "([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:(" +
          "[0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
    Matcher m = timePattern.matcher(time);
    long years = 0;
    long msyear = 31536000000L;
    long months = 0;
    long msmonth = 2628000000L;
    long weeks = 0;
    long msweek = 604800000;
    long days = 0;
    long msday = 86400000;
    long hours = 0;
    long mshour = 3600000;
    long minutes = 0;
    long msminute = 60000;
    long seconds = 0;
    long mssecond = 1000;
    boolean found = false;
    while (m.find()) {
      if ((m.group() != null) && (!m.group().isEmpty())) {
        for (int i = 0; i < m.groupCount(); i++) {
          if ((m.group(i) != null) && (!m.group(i).isEmpty())) {
            found = true;
            break;
          }
        }
        if (found) {
          if ((m.group(1) != null) && (!m.group(1).isEmpty())) {
            years = Integer.parseInt(m.group(1));
          }
          if ((m.group(2) != null) && (!m.group(2).isEmpty())) {
            months = Integer.parseInt(m.group(2));
          }
          if ((m.group(3) != null) && (!m.group(3).isEmpty())) {
            weeks = Integer.parseInt(m.group(3));
          }
          if ((m.group(4) != null) && (!m.group(4).isEmpty())) {
            days = Integer.parseInt(m.group(4));
          }
          if ((m.group(5) != null) && (!m.group(5).isEmpty())) {
            hours = Integer.parseInt(m.group(5));
          }
          if ((m.group(6) != null) && (!m.group(6).isEmpty())) {
            minutes = Integer.parseInt(m.group(6));
          }
          if ((m.group(7) != null) && (!m.group(7).isEmpty())) {
            seconds = Integer.parseInt(m.group(7));
          }
        }
      }
    }
    long val = (years * msyear) + (months * msmonth) + (weeks * msweek) + (days * msday) + (hours * mshour) +
          (minutes * msminute) + (seconds * mssecond);
    return val < 0 ? Long.MAX_VALUE - 1 : (years + months + weeks + days + hours + minutes + seconds) > 0 ? val : -1;
  }

  /**
   * Converts the passed milliseconds to a # hour, # minute, # second format. Sections &lt;1 are omitted.
   * Sections &gt;1 are plural.
   *
   * @param millis the time milliseconds to parse.
   * @return a string representation of the milliseconds in hour minute second format.
   */
  public static String millisToHMS(Long millis) {
    if (millis == 0) {
      return "0";
    }
    long h = millis / (1000 * 60 * 60);
    millis -= h * (1000 * 60 * 60);
    long m = millis / (1000 * 60);
    millis -= m * (1000 * 60);
    long s = millis / (1000);
    StringBuilder out = new StringBuilder();
    if (h > 0) {
      out.append(h);
      out.append(" Hour");
      if (h > 1) {
        out.append("s");
      }
    }
    if (m > 0) {
      if (out.length() > 0) {
        out.append(", ");
      }
      out.append(m);
      out.append(" Minute");
      if (m > 1) {
        out.append("s");
      }
    }
    if (s > 0) {
      if (out.length() > 0) {
        out.append(", ");
      }
      out.append(s);
      out.append(" Second");
      if (s > 1) {
        out.append("s");
      }
    }
    return out.length() > 0 ? out.toString() : "< 1 Second";
  }

  /**
   * Converts the passed milliseconds to a # hour, # minute format. Sections &lt;1 are omitted.
   * Sections &gt;1 are plural.
   *
   * @param millis the time milliseconds to parse.
   * @return a string representation of the milliseconds in hour minute format.
   */
  public static String millisToHM(Long millis) {
    if (millis == 0) {
      return "0";
    }
    long h = millis / (1000 * 60 * 60);
    millis -= h * (1000 * 60 * 60);
    long m = millis / (1000 * 60);
    StringBuilder out = new StringBuilder();
    if (h > 0) {
      out.append(h);
      out.append(" Hour");
      if (h > 1) {
        out.append("s");
      }
    }
    if (m > 0) {
      if (out.length() > 0) {
        out.append(", ");
      }
      out.append(m);
      out.append(" Minute");
      if (m > 1) {
        out.append("s");
      }
    }
    return out.length() > 0 ? out.toString() : "< 1 Minute";
  }

  /**
   * Converts the passed milliseconds to a # hour format. Sections &lt;1 are omitted.
   * Sections &gt;1 are plural.
   *
   * @param millis the time milliseconds to parse.
   * @return a string representation of the milliseconds in hour format.
   */
  public static String millisToH(Long millis) {
    if (millis == 0) {
      return "0";
    }
    long h = millis / (1000 * 60 * 60);
    StringBuilder out = new StringBuilder();
    if (h > 0) {
      out.append(h);
      out.append(" Hour");
      if (h > 1) {
        out.append("s");
      }
    }
    return out.length() > 0 ? out.toString() : "< 1 Hour";
  }

  /**
   * Determines whether or not the passed string is a valid time string.
   *
   * @param string the string in question.
   * @return a boolean value representing whether or not the passed string is a valid time string.
   */
  public static boolean isTimeStr(String string) {
    return string.matches("^([0-9]+y)?([0-9]+y)?([0-9]+mo)?([0-9]+w)?([0-9]+d)?([0-9]+h)?([0-9]+m)?([0-9]+s?)?$");
  }

  /**
   * Returns a yyyy-MM-dd formatted string representation of the current day.
   * Uses America/New_York timezone.
   *
   * @return Returns a string representation of the current day.
   */
  public static String today() {
    return millisToDateTime(System.currentTimeMillis(), "yyyy-MM-dd");
  }

  /**
   * Returns a HH:mm:ss z formatted string representation of the current time.
   * Uses America/New_York timezone.
   *
   * @return Returns a string representation of the current time.
   */
  public static String now() {return millisToDateTime(System.currentTimeMillis(), "HH:mm:ss z");}

  /**
   * Returns the millisecond time of the beginning of the year.
   * Uses America/New_York timezone.
   *
   * @return the millisecond time of the beginning of the year.
   */
  public static long startOfYear() {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.setTime(new Date(System.currentTimeMillis()));
    cal.set(Calendar.DAY_OF_YEAR, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime().getTime();
  }

  /**
   * Returns the millisecond time of the beginning of the month.
   * Uses America/New_York timezone.
   *
   * @return the millisecond time of the beginning of the month.
   */
  public static long startOfMonth() {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.setTime(new Date(System.currentTimeMillis()));
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime().getTime();
  }

  /**
   * Returns the millisecond time of the beginning of the week.
   * Uses America/New_York timezone.
   *
   * @return the millisecond time of the beginning of the week.
   */
  public static long startOfWeek() {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.setTime(new Date(System.currentTimeMillis()));
    cal.set(Calendar.DAY_OF_WEEK, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime().getTime();
  }

  /**
   * Returns the millisecond time of the beginning of the day.
   * Uses America/New_York timezone.
   *
   * @return the millisecond time of the beginning of the day.
   */
  public static long startOfDay() {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.setTime(new Date(System.currentTimeMillis()));
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime().getTime();
  }

  /**
   * Returns the millisecond time of the beginning of the passed day.
   * Uses America/New_York timezone.
   *
   * @param year  the year.
   * @param month the month.
   * @param day   the day.
   * @return the millisecond time of the beginning of the passed day.
   */
  public static long getDayStart(int year, int month, int day) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.set(year, month, day, 0, 0, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime().getTime();
  }

  /**
   * Returns the millisecond time of the end of the passed day.
   * Uses America/New_York timezone.
   *
   * @param year  the year.
   * @param month the month.
   * @param day   the day.
   * @return the millisecond time of the end of the passed day.
   */
  public static long getDayEnd(int year, int month, int day) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.set(year, month, day, 23, 59, 59);
    cal.set(Calendar.MILLISECOND, 999);
    return cal.getTime().getTime();
  }

  /**
   * Returns the millisecond time of the beginning of the passed month.
   * Uses America/New_York timezone.
   *
   * @param year  the year.
   * @param month the month.
   * @return the millisecond time of the beginning of the passed month.
   */
  public static long getMonthStart(int year, int month) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.set(year, month, 0, 0, 0, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime().getTime();
  }

  /**
   * Returns the millisecond time of the end of the passed month.
   * Uses America/New_York timezone.
   *
   * @param year  the year.
   * @param month the month.
   * @return the millisecond time of the end of the passed month.
   */
  public static long getMonthEnd(int year, int month) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.set(year, month, daysInMonth(month) + (month == 1 && isLeapYear(year) ? 1 : 0),
          23, 59, 59);
    cal.set(Calendar.MILLISECOND, 999);
    return cal.getTime().getTime();
  }

  /**
   * Returns the millisecond time of the beginning of the passed year.
   * Uses America/New_York timezone.
   *
   * @param year the year.
   * @return the millisecond time of the beginning of the passed year.
   */
  public static long getYearStart(int year) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.set(year, Calendar.JANUARY, 0, 0, 0, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime().getTime();
  }

  /**
   * Returns the millisecond time of the end of the passed year.
   * Uses America/New_York timezone.
   *
   * @param year the year.
   * @return the millisecond time of the end of the passed year.
   */
  public static long getYearEnd(int year) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
    cal.set(Calendar.MILLISECOND, 999);
    return cal.getTime().getTime();
  }

  /**
   * Returns the integer year value for the passed milliseconds.
   *
   * @param millis the time in milliseconds.
   * @return the integer year value for the passed milliseconds.
   */
  public static int getYear(long millis) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.setTimeInMillis(millis);
    return cal.get(Calendar.YEAR);
  }

  /**
   * Returns the integer month value for the passed milliseconds.
   * Note: Months begin at 0 (January) and end at 11 (December).
   *
   * @param millis the time in milliseconds.
   * @return the integer month value for the passed milliseconds.
   */
  public static int getMonth(long millis) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.setTimeInMillis(millis);
    return cal.get(Calendar.MONTH);
  }

  /**
   * Returns the integer day value for the passed milliseconds.
   *
   * @param millis the time in milliseconds.
   * @return the integer day value for the passed milliseconds.
   */
  public static int getDay(long millis) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    cal.setTimeInMillis(millis);
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Returns the number of days in the passed month. Does not account for leap years.
   * Note: Months begin at 0 (January) and end at 11 (December).
   *
   * @param month the numeric month.
   * @return the number of days in the month.
   */
  public static int daysInMonth(int month) {
    return month < 0 || month > 11 ? 0 : monthLengths[month];
  }

  /**
   * Returns the number of days in the passed month. Accounts for leap years.
   * Note: Months begin at 0 (January) and end at 11 (December).
   *
   * @param year  the numeric year.
   * @param month the numeric month.
   * @return the number of days in the month.
   */
  public static int daysInMonth(int year, int month) {
    return month < 0 || month > 11 ? 0 : monthLengths[month] + (month == 1 ? (isLeapYear(year) ? 1 : 0) : 0);
  }

  /**
   * Returns whether or not the passed year is a leap year.
   *
   * @param year the numeric year.
   * @return a boolean value representing whether or not the passed year is a leap year.
   */
  public static boolean isLeapYear(int year) {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
  }
}
