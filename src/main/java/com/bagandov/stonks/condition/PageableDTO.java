package com.bagandov.stonks.condition;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageableDTO {

    Integer pageSize;

    Integer pageNumber;
}
