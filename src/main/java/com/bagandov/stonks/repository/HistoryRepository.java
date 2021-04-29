package com.bagandov.stonks.repository;

import com.bagandov.stonks.model.History;
import com.bagandov.stonks.model.HistoryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HistoryRepository extends PagingAndSortingRepository<History, HistoryKey>, JpaSpecificationExecutor<History> {
}
