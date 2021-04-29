package com.bagandov.stonks.repository;

import com.bagandov.stonks.condition.SortFilterDTO;
import com.bagandov.stonks.model.SecurityJoinHistory;

import java.util.Date;
import java.util.List;

public interface SecurityJoinHistoryRepository  {
    List<SecurityJoinHistory> getJoinTable(SortFilterDTO sortFilterDTO);
}
