package com.bagandov.stonks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.namespace.QName;
import javax.xml.stream.events.StartElement;
import java.util.List;
import java.util.regex.Pattern;

import static com.bagandov.stonks.parser.ParsingUtils.toIntegerOrNull;

@Entity
@Table(name = "securities")
@Data
@NoArgsConstructor
public class Security {

    @Id
    @Column(name = "secid")
    String secid;

    @Column(name = "shortname")
    String shortname;

    @Column(name = "regnumber")
    String regnumber;

    @Column(name = "name")
    String name;

    @Column(name = "emitent_id")
    Integer emitentId;

    @Column(name = "emitent_title")
    String emitentTitle;

    @Column(name = "emitent_inn")
    String emitentInn;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "secid")
    List<History> histories;

    public static Security fromStartElement(StartElement startElement) {
        Security security = new Security();
        security.secid = startElement.getAttributeByName(new QName("secid")).getValue();
        security.shortname = startElement.getAttributeByName(new QName("shortname")).getValue();
        security.regnumber = startElement.getAttributeByName(new QName("regnumber")).getValue();
        security.name = startElement.getAttributeByName(new QName("name")).getValue();
        security.emitentId = toIntegerOrNull(startElement.getAttributeByName(new QName("emitent_id")).getValue());
        security.emitentTitle = startElement.getAttributeByName(new QName("emitent_title")).getValue();
        security.emitentInn = startElement.getAttributeByName(new QName("emitent_inn")).getValue();
        return security;
    }

    private static Pattern namePattern = Pattern.compile("[а-яА-Я0-9 ]*");

    @JsonIgnore
    public boolean isValid() {
        return namePattern.matcher(name).matches();
    }
}
