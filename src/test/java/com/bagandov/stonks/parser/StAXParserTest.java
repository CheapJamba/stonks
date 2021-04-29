package com.bagandov.stonks.parser;

import org.junit.jupiter.api.Test;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StAXParserTest {

    @Test
    void parse() throws IOException, XMLStreamException, URISyntaxException {
        Path path = Paths.get(getClass().getClassLoader().getResource("StAXParserTest").toURI());
        StringBuilder sb = new StringBuilder();
        Handler<List<String>> handler = (list -> list.forEach(sb::append));
        Deserializer<String> deserializer = (startElement ->
                startElement.getAttributeByName(new QName("string")).getValue()
        );
        StAXParser<String> stAXParser = new StAXParser<String>(path.toString(),
                "import_[0-9]+\\.xml",
                handler, deserializer, 2);
        stAXParser.parse();
        String result = sb.toString();
        assertEquals("s11s12s13s14s21s22s23", result);
    }
}