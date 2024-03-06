package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    public static LocalDate convertStringToDate(String date) {
        try {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return LocalDate.parse(date, formatter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
