package com.bagandov.stonks.repository;

import com.bagandov.stonks.model.History;
import com.bagandov.stonks.model.HistoryKey;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface HistoryRepository extends PagingAndSortingRepository<History, HistoryKey>, JpaSpecificationExecutor<History> {
    void deleteAllBySecid(String secid);

    List<History> getAllBySecid(String secid);
}
