package com.bagandov.stonks.parser;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ParsingUtilsTest {

    @Test
    void toDoubleOrNull() {
        Double result = ParsingUtils.toDoubleOrNull("0.15");
        assertEquals(0.15, result);
    }

    @Test
    void toDoubleOrNull_blankString() {
        Double result = ParsingUtils.toDoubleOrNull("");
        assertNull(result);
    }

    @Test
    void toIntegerOrNull() {
        Integer result = ParsingUtils.toIntegerOrNull("15");
        assertEquals(15, result);
    }

    @Test
    void toIntegerOrNull_blankString() {
        Integer result = ParsingUtils.toIntegerOrNull("");
        assertNull(result);
    }

    @Test
    void stringToHistoryDate() throws ParseException {
        Date date = ParsingUtils.stringToHistoryDate("2020-04-15");
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2020-04-15"), date);
    }

    @Test
    void stringToHistoryDate_wrongDateFormat() {
        assertThrows(ResponseStatusException.class, () -> ParsingUtils.stringToHistoryDate("2020/04/15"));
    }
}