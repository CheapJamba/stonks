package com.bagandov.stonks.service;

import com.bagandov.stonks.condition.*;
import com.bagandov.stonks.model.History;
import com.bagandov.stonks.model.HistoryKey;
import com.bagandov.stonks.model.specification.HistorySpecifications;
import com.bagandov.stonks.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HistoryServiceImpl implements HistoryService {

    private HistoryRepository historyRepo;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepo) {
        this.historyRepo = historyRepo;
    }

    @Override
    public void saveAll(List<History> historiesToSave) {
        try {
            historyRepo.saveAll(historiesToSave);
        } catch (Exception e) {
            System.out.println("Error at HistoryServiceImpl.saveAll");
            System.out.println("Message: " + e.getMessage());
        }
    }

    @Override
    public Page<History> findAll(SortFilterPageableDTO sortFilterPageableDTO) {
        Map<String, FilterDTO> filterDTOs = sortFilterPageableDTO.getFilterDTOs();
        Specification<History> spec = Specification.where(null);
        for (String key : filterDTOs.keySet()) {
            if (key.equals("numtrades")) {
                spec = spec.and(HistorySpecifications.numtrades(filterDTOs.get(key)));
            }
            if (key.equals("value")) {
                spec = spec.and(HistorySpecifications.value(filterDTOs.get(key)));
            }
        }

        return historyRepo.findAll(spec, sortFilterPageableDTO.toPageable());
    }

    @Override
    public History findOneByKey(HistoryKey targetHistoryKey) {
        Optional<History> optionalHistory = historyRepo.findById(targetHistoryKey);
        if(optionalHistory.isPresent()) {
            return optionalHistory.get();
        } else {
            throw new RuntimeException("Couldn't find security with secid: " + targetHistoryKey);
        }
    }

    @Override
    public void saveOne(History historyToSave) {
        historyRepo.save(historyToSave);
    }

    @Override
    public void deleteOneByKey(HistoryKey targetHistoryKey) {
        historyRepo.deleteById(targetHistoryKey);
    }

}
