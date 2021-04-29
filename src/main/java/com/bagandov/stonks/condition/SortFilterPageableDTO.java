package com.bagandov.stonks.condition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class SortFilterPageableDTO {
    private SortDTO sortDTO;

    @NotNull(message = "filterDTOs shouldn't be null. Pass an empty map for no filtration.")
    @Valid
    private Map<String, FilterDTO> filterDTOs;

    PageableDTO pageableDTO;

    public Pageable toPageable() {
        Sort sort = Sort.unsorted();
        if (sortDTO != null) {
            sort = Sort.by(Sort.Direction.fromString(sortDTO.getDirection().name()), sortDTO.getProperty());
        }

        return PageRequest.of(pageableDTO.getPageNumber(), pageableDTO.getPageSize(), sort);
    }
}
