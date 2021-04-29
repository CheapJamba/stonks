package com.bagandov.stonks.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class SortDTO implements Serializable {
    private String property;

    private SortDirection direction;

    public enum SortDirection {
        ASC, DESC
    }
}
