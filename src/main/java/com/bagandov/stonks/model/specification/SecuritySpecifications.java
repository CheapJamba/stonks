package com.bagandov.stonks.model.specification;

import com.bagandov.stonks.condition.FilterDTO;
import com.bagandov.stonks.model.Security;
import com.bagandov.stonks.model.Security_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SecuritySpecifications {

    public static Specification<Security> emitentTitle(FilterDTO filterDTO) {
        return new Specification<Security>() {
            @Override
            public Predicate toPredicate(Root<Security> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return filterDTO.toPredicate(criteriaBuilder, root.get(Security_.EMITENT_TITLE), filterDTO.getValue());
            }
        };
    }

    public static Specification<Security> regnumber(FilterDTO filterDTO) {
        return new Specification<Security>() {
            @Override
            public Predicate toPredicate(Root<Security> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return filterDTO.toPredicate(criteriaBuilder, root.get(Security_.REGNUMBER), filterDTO.getValue());
            }
        };
    }
}
