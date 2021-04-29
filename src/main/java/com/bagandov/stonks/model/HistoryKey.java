package com.bagandov.stonks.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HistoryKey implements Serializable {

    private String boardid;

    private Date tradedate;

    private String secid;

}
