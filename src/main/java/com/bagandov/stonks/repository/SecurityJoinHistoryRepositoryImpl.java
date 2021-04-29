package com.bagandov.stonks.repository;

import com.bagandov.stonks.condition.FilterDTO;
import com.bagandov.stonks.condition.SortFilterDTO;
import com.bagandov.stonks.model.Security;
import com.bagandov.stonks.model.SecurityJoinHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bagandov.stonks.parser.ParsingUtils.stringToHistoryDate;

@Repository
public class SecurityJoinHistoryRepositoryImpl implements SecurityJoinHistoryRepository {

    EntityManager em;

    @Autowired
    SecurityJoinHistoryRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<SecurityJoinHistory> getJoinTable(SortFilterDTO sortFilterDTO) {
        StringBuilder queryBuilder = new StringBuilder("select new com.bagandov.stonks.model.SecurityJoinHistory(s.secid, s.regnumber, s.name, s.emitentTitle, h.boardid, h.tradedate, h.numtrades, h.open, h.close) " +
                "from Security s, History h where s.secid = h.secid");
        Map<String, FilterDTO> filterDTOs = sortFilterDTO.getFilterDTOs();
        Map<String, Object> parameterMap = new HashMap<>();
        if(filterDTOs.containsKey("emitentTitle")) {
            FilterDTO filterDTO = filterDTOs.get("emitentTitle");
            queryBuilder.append(" AND s.emitentTitle").append(filterDTO.getRule().getSign()).append(":emitentTitle");
            parameterMap.put("emitentTitle", filterDTO.getValue());
        }
        if(filterDTOs.containsKey("tradedate")) {
            FilterDTO filterDTO = filterDTOs.get("tradedate");
            queryBuilder.append(" AND h.tradedate").append(filterDTO.getRule().getSign()).append(":tradedate");
            parameterMap.put("tradedate", stringToHistoryDate(filterDTO.getValue()));
        }

        if (sortFilterDTO.getSortDTO() != null) {
            queryBuilder.append(" ORDER BY ").append(prefixProperty(sortFilterDTO.getSortDTO().getProperty())).append(" ")
                    .append(sortFilterDTO.getSortDTO().getDirection().name());
        }

        Query query = em.createQuery(queryBuilder.toString());
        for(String key : parameterMap.keySet()) {
            query.setParameter(key, parameterMap.get(key));
        }


        return (List<SecurityJoinHistory>) query.getResultList();
    }

    private String prefixProperty (String property) {
        if (property.equals("secid")) {
            return "s.secid";
        }
        return property;
    }
}
