package com.bagandov.stonks.parser;

import javax.xml.stream.events.StartElement;

public interface Deserializer<T> {
    T fromStartElement(StartElement startElement);
}
