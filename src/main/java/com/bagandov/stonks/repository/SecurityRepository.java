package com.bagandov.stonks.repository;

import com.bagandov.stonks.model.Security;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SecurityRepository extends PagingAndSortingRepository<Security, String>, JpaSpecificationExecutor<Security>,
        SecurityJoinHistoryRepository {

    @Query("select s.secid from Security s where s.secid in ?1")
    List<String> getSecidsExistingInRange(List<String> secidsRange);
}
