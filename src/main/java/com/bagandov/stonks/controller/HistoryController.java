package com.bagandov.stonks.controller;

import com.bagandov.stonks.condition.SortFilterDTO;
import com.bagandov.stonks.condition.SortFilterPageableDTO;
import com.bagandov.stonks.model.History;
import com.bagandov.stonks.model.HistoryKey;
import com.bagandov.stonks.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("history")
public class HistoryController {

    private HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping
    public Page<History> getHistories(@Valid @RequestBody SortFilterPageableDTO sortFilterPageableDTO) {
        return historyService.findAll(sortFilterPageableDTO);
    }

    @GetMapping("getOneByKey")
    public History getHistoryByKey(@RequestBody HistoryKey targetKey) {
        return historyService.findOneByKey(targetKey);
    }

    @PostMapping
    public void addHistory(@RequestBody History history) {
        historyService.saveOne(history);
    }

    @PutMapping
    public void updateExistingHistory(@RequestBody History history) {
        historyService.saveOne(history);
    }

    @DeleteMapping
    public void deleteHistoryByKey(@RequestBody HistoryKey historyKey) {
        historyService.deleteOneByKey(historyKey);
    }
}
