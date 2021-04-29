package com.bagandov.stonks.model.specification;

import com.bagandov.stonks.condition.FilterDTO;
import com.bagandov.stonks.model.History;
import com.bagandov.stonks.model.History_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class HistorySpecifications {

    public static Specification<History> numtrades(FilterDTO filterDTO) {
        return new Specification<History>() {
            @Override
            public Predicate toPredicate(Root<History> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return filterDTO.toPredicate(criteriaBuilder, root.get(History_.NUMTRADES), Double.valueOf(filterDTO.getValue()));
            }
        };
    }

    public static Specification<History> value(FilterDTO filterDTO) {
        return new Specification<History>() {
            @Override
            public Predicate toPredicate(Root<History> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return filterDTO.toPredicate(criteriaBuilder, root.get(History_.VALUE), Double.valueOf(filterDTO.getValue()));
            }
        };
    }
}
