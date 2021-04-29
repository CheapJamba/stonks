package com.bagandov.stonks.parser;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ParsingUtils {
    private static SimpleDateFormat historyDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Double toDoubleOrNull(String string) {
        if (!string.isEmpty()) {
            return Double.valueOf(string);
        }
        return null;
    }

    public static Integer toIntegerOrNull(String string) {
        if (!string.isEmpty()) {
            return Integer.valueOf(string);
        }
        return null;
    }

    public static Date stringToHistoryDate(String input) {
        try {
            return historyDateFormat.parse(input);
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Error parsing string " + input + " to history date. Correct format is: yyyy-MM-dd", e);
        }
    }
}
