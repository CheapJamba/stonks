package com.bagandov.stonks.service;

import com.bagandov.stonks.condition.SortFilterDTO;
import com.bagandov.stonks.condition.SortFilterPageableDTO;
import com.bagandov.stonks.model.DTO.SecurityUpdateDTO;
import com.bagandov.stonks.model.SecurityJoinHistory;
import com.bagandov.stonks.model.Security;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SecurityService {

    Page<Security> findAll(SortFilterPageableDTO sortFilterPageableDTO);

    Security findOneBySecid(String targetSecid);

    void saveOne(Security securityToSave);

    void deleteOneBySecid(String targetSecid);

    void saveAll(List<Security> securitiesToSave);

    Security update(SecurityUpdateDTO updateDTO);

    List<SecurityJoinHistory> getJoinTable(SortFilterDTO sortFilterDTO);

    List<String> getSecidsExistingInRange(List<String> secidsRange);
}
