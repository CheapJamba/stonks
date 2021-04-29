package com.bagandov.stonks.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecurityJoinHistory implements Serializable {

    String secid;

    String regnumber;

    String name;

    String emitentTitle;

    String boardid;

    Date tradedate;

    Double numtrades;

    Double open;

    Double close;
}
