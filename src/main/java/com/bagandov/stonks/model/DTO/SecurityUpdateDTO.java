package com.bagandov.stonks.model.DTO;

import com.bagandov.stonks.model.HistoryKey;
import com.bagandov.stonks.model.Security;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUpdateDTO {

    private String secid;

    private String shortname;

    private String regnumber;

    private String name;

    private Integer emitentId;

    private String emitentTitle;

    private String emitentInn;

    public void update(Security security) {
        if (shortname != null) {
            security.setShortname(shortname);
        }
        if (regnumber != null) {
            security.setRegnumber(regnumber);
        }
        if (name != null) {
            security.setName(name);
        }
        if (emitentId != null) {
            security.setEmitentId(emitentId);
        }
        if (emitentTitle != null) {
            security.setEmitentTitle(emitentTitle);
        }
        if (emitentInn != null) {
            security.setEmitentInn(emitentInn);
        }
    }

}
