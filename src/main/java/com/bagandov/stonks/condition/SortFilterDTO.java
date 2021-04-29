package com.bagandov.stonks.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class SortFilterDTO implements Serializable {
    private SortDTO sortDTO;

    @NotNull(message = "filterDTOs shouldn't be null. Pass an empty map for no filtration.")
    private Map<String, FilterDTO> filterDTOs;
}
