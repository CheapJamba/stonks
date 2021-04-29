package com.bagandov.stonks.service;

import com.bagandov.stonks.condition.*;
import com.bagandov.stonks.model.DTO.SecurityUpdateDTO;
import com.bagandov.stonks.model.SecurityJoinHistory;
import com.bagandov.stonks.model.Security;
import com.bagandov.stonks.model.specification.SecuritySpecifications;
import com.bagandov.stonks.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class SecurityServiceImpl implements SecurityService {

    private SecurityRepository securityRepo;

    @Autowired
    public SecurityServiceImpl(SecurityRepository securityRepo) {
        this.securityRepo = securityRepo;
    }

    @Override
    public Page<Security> findAll(SortFilterPageableDTO sortFilterPageableDTO) {
        Map<String, FilterDTO> filterDTOs = sortFilterPageableDTO.getFilterDTOs();
        Specification<Security> spec = Specification.where(null);
        for (String key : filterDTOs.keySet()) {
            if (key.equals("emitentTitle")) {
                spec = spec.and(SecuritySpecifications.emitentTitle(filterDTOs.get(key)));
            }
            if (key.equals("regnumber")) {
                spec = spec.and(SecuritySpecifications.regnumber(filterDTOs.get(key)));
            }
        }

        return securityRepo.findAll(spec, sortFilterPageableDTO.toPageable());
    }

    @Override
    public Security findOneBySecid(String targetSecid) {
        Optional<Security> optionalSecurity = securityRepo.findById(targetSecid);
        if(optionalSecurity.isPresent()) {
            return optionalSecurity.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find security with secid: " + targetSecid);
        }
    }

    @Override
    public void saveOne(Security securityToSave) {
        securityRepo.save(securityToSave);
    }

    @Override
    public Security update(SecurityUpdateDTO updateDTO) {
        Optional<Security> optionalSecurity = securityRepo.findById(updateDTO.getSecid());
        if (!optionalSecurity.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find security with secid: " + updateDTO.getSecid());
        }
        Security targetSecurity = optionalSecurity.get();
        updateDTO.update(targetSecurity);
        return securityRepo.save(targetSecurity);
    }

    @Override
    public void deleteOneBySecid(String targetSecid) {
        if (!securityRepo.existsById(targetSecid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't find security with secid: " + targetSecid);
        }
        securityRepo.deleteById(targetSecid);
    }

    @Override
    public void saveAll(List<Security> securityList) {
        securityRepo.saveAll(securityList);
    }

    @Override
    public List<SecurityJoinHistory> getJoinTable(SortFilterDTO sortFilterDTO) {
        return securityRepo.getJoinTable(sortFilterDTO);
    }

    @Override
    public List<String> getSecidsExistingInRange(List<String> secidsRange) {
        return securityRepo.getSecidsExistingInRange(secidsRange);
    }
}
