package com.bagandov.stonks.controller;

import com.bagandov.stonks.model.History;
import com.bagandov.stonks.model.Security;
import com.bagandov.stonks.parser.Handler;
import com.bagandov.stonks.parser.StAXParser;
import com.bagandov.stonks.service.HistoryService;
import com.bagandov.stonks.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("import")
public class ImportController {

    private SecurityService securityService;

    private HistoryService historyService;

    @Autowired
    public ImportController(SecurityService securityService, HistoryService historyService) {
        this.securityService = securityService;
        this.historyService = historyService;
    }

    @PostMapping
    public void importData() {

        Handler<List<Security>> securityHandler = (list -> {
            securityService.saveAll(list);
        });

        Handler<List<History>> historyHandler = (list -> {
            List<String> existingSecids = securityService.getSecidsExistingInRange(
                    list.stream().map(History::getSecid).collect(Collectors.toList())
            );
            List<History> filtered = list.stream().filter(
                    history -> existingSecids.contains(history.getSecid())).collect(Collectors.toList()
            );
            historyService.saveAll(filtered);
        });

        StAXParser<Security> securityParser = new StAXParser<>(System.getProperty("user.dir") + "/data",
                "securities_[0-9]+\\.xml", securityHandler, Security::fromStartElement, 100);

        StAXParser<History> historyParser = new StAXParser<>(System.getProperty("user.dir") + "/data",
                "history_[0-9]+\\.xml", historyHandler, History::fromStartElement, 100);

        try {
            securityParser.parse();
            historyParser.parse();
        } catch (XMLStreamException XMLex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing xml", XMLex);
        } catch (FileNotFoundException NotFoundEx) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File for import not found", NotFoundEx);
        }


    }
}
