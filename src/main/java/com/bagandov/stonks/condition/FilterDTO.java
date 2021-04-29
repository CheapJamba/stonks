package com.bagandov.stonks.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class FilterDTO implements Serializable {

    ValueType valueType;

    @NotNull(message = "Filter value can't be null")
    String value;

    @NotNull(message = "Filter rule can't be null")
    Rule rule;

    public enum ValueType {
        STRING, DATE, INTEGER, DOUBLE
    }

    public enum Rule {
        EQUALS("="), MORE(">"), LESS("<"), EQUALSORMORE(">="), EQUALSORLESS("<=");
        private String sign;

        Rule(String sign) {
            this.sign = sign;
        }
        public String getSign() {
            return sign;
        }
    }

    public <T extends Comparable<T>> Predicate toPredicate(CriteriaBuilder criteriaBuilder, javax.persistence.criteria.Path<T> path, T value) {
        switch (rule) {
            case EQUALS:
                return criteriaBuilder.equal(path, value);
            case LESS:
                return criteriaBuilder.lessThan(path, value);
            case EQUALSORLESS:
                return criteriaBuilder.lessThanOrEqualTo(path, value);
            case MORE:
                return criteriaBuilder.greaterThan(path, value);
            case EQUALSORMORE:
                return criteriaBuilder.greaterThanOrEqualTo(path, value);
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't use provided filter");
    }

}
