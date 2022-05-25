package common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import common.exceptions.InvalidDateFormatException;

/**
 * Методы для преобразования между строкой и датой.
 */

public class DateConverter {
    private static String pattern = "yyyy-MM-dd";

    private static DateFormat dateFormatter = new SimpleDateFormat(pattern);

    private static DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern(pattern);

    public static void setPattern(String p) {
        pattern = p;
        dateFormatter = new SimpleDateFormat(pattern);
        localDateFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    public static String dateToString(Date date) {
        return date!=null?dateFormatter.format(date):null;
    }

    public static String dateToString(LocalDate date) {
        return date!=null?date.format(localDateFormatter):null;
    }

    public static LocalDate parseLocalDate(String s) throws InvalidDateFormatException {
        try {
            return LocalDate.parse(s, localDateFormatter);
        } catch (java.time.format.DateTimeParseException e) {
            throw new InvalidDateFormatException();
        }
    }

    public static Date parseDate(String s) throws InvalidDateFormatException {
        try {
            return dateFormatter.parse(s);
        } catch (ParseException e) {
            throw new InvalidDateFormatException();
        }
    }
}
