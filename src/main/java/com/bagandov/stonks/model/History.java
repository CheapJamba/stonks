package com.bagandov.stonks.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;
import java.util.Date;

import static com.bagandov.stonks.parser.ParsingUtils.stringToHistoryDate;
import static com.bagandov.stonks.parser.ParsingUtils.toDoubleOrNull;

@Entity
@IdClass(HistoryKey.class)
@Table(name = "history")
@Data
@NoArgsConstructor
public class History {

    @Id
    @Column(name = "boardid")
    String boardid;

    @Id
    @Column(name = "tradedate")
    Date tradedate;

    @Id
    @Column(name = "secid")
    String secid;

    @Column(name = "shortname")
    String shortname;

    @Column(name = "numtrades")
    Double numtrades;

    @Column(name = "value")
    Double value;

    @Column(name = "open")
    Double open;

    @Column(name = "low")
    Double low;

    @Column(name = "high")
    Double high;

    @Column(name = "close")
    Double close;

    public static History fromStartElement(StartElement startElement) {
        History history = new History();
        history.boardid = startElement.getAttributeByName(new QName("BOARDID")).getValue();
        history.tradedate = stringToHistoryDate(startElement.getAttributeByName(new QName("TRADEDATE")).getValue());
        history.secid = startElement.getAttributeByName(new QName("SECID")).getValue();
        history.shortname = startElement.getAttributeByName(new QName("SHORTNAME")).getValue();
        history.numtrades = toDoubleOrNull(startElement.getAttributeByName(new QName("NUMTRADES")).getValue());
        history.value = toDoubleOrNull(startElement.getAttributeByName(new QName("VALUE")).getValue());
        history.open = toDoubleOrNull(startElement.getAttributeByName(new QName("OPEN")).getValue());
        history.low = toDoubleOrNull(startElement.getAttributeByName(new QName("LOW")).getValue());
        history.high = toDoubleOrNull(startElement.getAttributeByName(new QName("HIGH")).getValue());
        history.close = toDoubleOrNull(startElement.getAttributeByName(new QName("CLOSE")).getValue());
        return history;
    }

}
