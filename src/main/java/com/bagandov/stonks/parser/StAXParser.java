package com.bagandov.stonks.parser;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StAXParser<T> {

    List<String> paths;

    Handler<List<T>> handler;

    Deserializer<T> deserializer;

    int batchSize;

    public StAXParser(List<String> paths, Handler<List<T>> handler, Deserializer<T> deserializer, int batchSize) {
        this.paths = paths;
        this.handler = handler;
        this.deserializer = deserializer;
        this.batchSize = batchSize;
    }

    public StAXParser(String directoryPath, String pattern, Handler<List<T>> handler, Deserializer<T> deserializer, int batchSize) {
        this.paths = listFiles(directoryPath, pattern);
        this.handler = handler;
        this.deserializer = deserializer;
        this.batchSize = batchSize;
    }

    public void parse() throws FileNotFoundException, XMLStreamException {
        List<T> leftovers = new ArrayList<>();

        for(String path : paths) {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(new FileInputStream(path));
            leftovers = parseFile(reader, deserializer, leftovers);
        }
        handler.handle(leftovers);
    }

    private List<T> parseFile(XMLEventReader reader, Deserializer<T> deserializer
            , List<T> entries) throws XMLStreamException {

        boolean wereIn = false;

        while (!wereIn && reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();

                if (startElement.getName().getLocalPart().equals("rows")) {
                    wereIn = true;
                }
            }
        }

        while (wereIn && reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();

            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();

                if (startElement.getName().getLocalPart().equals("row")) {
                    entries.add(deserializer.fromStartElement(startElement));
                    if(entries.size() > batchSize - 1) {
                        handler.handle(entries);
                        entries.clear();
                    }
                }
            } else if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("rows")) {
                    wereIn = false;
                }
            }
        }

        return entries;
    }

    private static List<String> listFiles(String directoryPath, String pattern) {
        try (Stream<Path> stream = Files.list(Paths.get(directoryPath))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(fileName -> Pattern.compile(pattern).matcher(fileName).matches())
                    .map(fileName -> Paths.get(directoryPath, fileName).toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error listing files for parsing", e);
        }
}
}
