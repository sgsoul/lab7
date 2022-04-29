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
    private static DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss");
    private static DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy' 'HH:mm:ss");

    public static String dateToString(Date date) {
        return dateFormatter.format(date);
    }

    public static String dateToString(LocalDate date) {
        return date.format(localDateFormatter);
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
