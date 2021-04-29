package com.bagandov.stonks.service;

import com.bagandov.stonks.condition.SortFilterPageableDTO;
import com.bagandov.stonks.model.History;
import com.bagandov.stonks.model.HistoryKey;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HistoryService {
    Page<History> findAll(SortFilterPageableDTO sortFilterPageableDTO);

    History findOneByKey(HistoryKey targetHistoryKey);

    void saveOne(History historyToSave);

    void deleteOneByKey(HistoryKey targetHistoryKey);

    void saveAll(List<History> historiesToSave);
}
