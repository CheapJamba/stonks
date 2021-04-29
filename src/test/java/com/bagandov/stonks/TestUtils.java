package com.bagandov.stonks;

import com.bagandov.stonks.model.History;
import com.bagandov.stonks.model.Security;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestUtils {

    public static Security createSecurity(int number) {
        Security security = new Security();
        security.setSecid("secid " + number);
        security.setShortname("shortname " + number);
        security.setName("name " + number);
        security.setRegnumber("regnumber " + number);
        security.setEmitentInn("emitentInn " + number);
        security.setEmitentTitle("emitentTitle " + number);
        security.setEmitentId(number);

        List<History> histories = new ArrayList<>();
        History history = new History();
        history.setSecid("secid " + number);
        history.setBoardid("boardid " + number);
        history.setTradedate(new Date(2016 - 1900, Calendar.JANUARY, number > 30 ? 30 : number));
        history.setNumtrades((double) number);
        history.setValue((double) (number * 100));
        history.setOpen((double) (number * 100) + 1.1);
        history.setLow((double) (number * 100) + 2.2);
        history.setHigh((double) (number * 100) + 3.3);
        history.setClose((double) (number * 100) + 4.4);
        histories.add(history);

        security.setHistories(histories);

        return security;
    }

    public static List<Security> createSecurityList(int ...numbers) {
        List<Security> result = new ArrayList<>();
        for (int number : numbers) {
            result.add(createSecurity(number));
        }
        return result;
    }

    public static String toJson(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
}
