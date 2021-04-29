package com.bagandov.stonks.service;

import com.bagandov.stonks.condition.*;
import com.bagandov.stonks.model.DTO.SecurityUpdateDTO;
import com.bagandov.stonks.model.History;
import com.bagandov.stonks.model.Security;
import com.bagandov.stonks.model.SecurityJoinHistory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SecurityServiceImplTest {

    @Autowired
    SecurityService securityService;

    @Test
    @Order(1)
    void findAll_WhenGivenSortOrderDesc_ReturnsCorrectlySortedPage() {

        SortFilterPageableDTO sortFilterPageableDTO = new SortFilterPageableDTO(
                new SortDTO("emitentId", SortDTO.SortDirection.DESC),
                new HashMap<String, FilterDTO>(),
                new PageableDTO(3, 0));
        Page<Security> page = securityService.findAll(sortFilterPageableDTO);

        assertEquals(10, page.getContent().get(0).getEmitentId());
        assertEquals(9, page.getContent().get(1).getEmitentId());
        assertEquals(8, page.getContent().get(2).getEmitentId());

        sortFilterPageableDTO.getPageableDTO().setPageNumber(2);
        page = securityService.findAll(sortFilterPageableDTO);
        assertEquals(4, page.getContent().get(0).getEmitentId());
        assertEquals(3, page.getContent().get(1).getEmitentId());
        assertEquals(2, page.getContent().get(2).getEmitentId());
    }

    @Test
    @Order(2)
    void findAll_WhenGivenPageable_ReturnsPageOfCorrectSize() {

        SortFilterPageableDTO sortFilterPageableDTO = new SortFilterPageableDTO(
                null,
                new HashMap<String, FilterDTO>(),
                new PageableDTO(5, 0));
        Page<Security> page = securityService.findAll(sortFilterPageableDTO);

        assertEquals(5, page.getContent().size());
    }

    @Test
    @Order(3)
    void findAll_WhenFilterForRegnumber_ReturnsCorrectlyFilteredPage() {

        FilterDTO filterDTO = new FilterDTO(FilterDTO.ValueType.STRING, "regnumber 3", FilterDTO.Rule.EQUALS);
        HashMap<String, FilterDTO> filterDTOs = new HashMap<>();
        filterDTOs.put("regnumber", filterDTO);
        SortFilterPageableDTO sortFilterPageableDTO = new SortFilterPageableDTO(
                null,
                filterDTOs,
                new PageableDTO(10, 0));

        Page<Security> page = securityService.findAll(sortFilterPageableDTO);
        assertEquals(1, page.getContent().size());
        assertEquals("regnumber 3", page.getContent().get(0).getRegnumber());

        filterDTO.setRule(FilterDTO.Rule.MORE);
        page = securityService.findAll(sortFilterPageableDTO);
        assertEquals(6, page.getContent().size());
        assertEquals("regnumber 4", page.getContent().get(0).getRegnumber());
        assertEquals("regnumber 9", page.getContent().get(5).getRegnumber());

        filterDTO.setRule(FilterDTO.Rule.EQUALSORMORE);
        page = securityService.findAll(sortFilterPageableDTO);
        assertEquals(7, page.getContent().size());
        assertEquals("regnumber 3", page.getContent().get(0).getRegnumber());
        assertEquals("regnumber 4", page.getContent().get(1).getRegnumber());
        assertEquals("regnumber 9", page.getContent().get(6).getRegnumber());

        filterDTO.setRule(FilterDTO.Rule.LESS);
        page = securityService.findAll(sortFilterPageableDTO);
        assertEquals(3, page.getContent().size());
        assertEquals("regnumber 1", page.getContent().get(0).getRegnumber());
        assertEquals("regnumber 2", page.getContent().get(1).getRegnumber());
        assertEquals("regnumber 10", page.getContent().get(2).getRegnumber());

        filterDTO.setRule(FilterDTO.Rule.EQUALSORLESS);
        page = securityService.findAll(sortFilterPageableDTO);
        assertEquals(4, page.getContent().size());
        assertEquals("regnumber 1", page.getContent().get(0).getRegnumber());
        assertEquals("regnumber 2", page.getContent().get(1).getRegnumber());
        assertEquals("regnumber 3", page.getContent().get(2).getRegnumber());
        assertEquals("regnumber 10", page.getContent().get(3).getRegnumber());
    }

    @Test
    @Order(4)
    void findOneBySecid_WhenGivenSecid_ReturnsSecurityWithMatchingSecid() {
        Security security = securityService.findOneBySecid("secid 1");
        assertEquals("secid 1", security.getSecid());
        assertEquals("shortname 1", security.getShortname());
        assertEquals("regnumber 1", security.getRegnumber());
        assertEquals("name 1", security.getName());
        assertEquals(5, security.getEmitentId());
        assertEquals("emitentTitle 1", security.getEmitentTitle());
        assertEquals("emitentInn 1", security.getEmitentInn());

        List<History> histories = security.getHistories();
        assertEquals(5, histories.size());
        histories.forEach(history -> assertEquals("secid 1", history.getSecid()));
    }

    @Test
    @Order(5)
    void findOneBySecid_WhenGivenSecidThatDoesntExist_ThrowsException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> securityService.findOneBySecid("fake secid"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(6)
    void getSecidsExistingInRange_WhenGivenListOfIds_ReturnsListOfOnlyThoseThatArePresentInDatabase() {
        List<String> secids = new ArrayList<>();
        secids.add("secid 1");
        secids.add("secid 2");
        secids.add("secid 3");
        secids.add("fake secid");
        secids.add("mega fake secid");
        secids.add("secid 20");
        List<String> secidsFromService = securityService.getSecidsExistingInRange(secids);
        assertEquals(3, secidsFromService.size());
        assertEquals("secid 1", secidsFromService.get(0));
        assertEquals("secid 2", secidsFromService.get(1));
        assertEquals("secid 3", secidsFromService.get(2));
    }

    @Test
    @Order(7)
    void getJoinTable_WhenGivenSortFilterDTO_ReturnsSortedAndFilteredSecurityJoinHistoryList() {
        FilterDTO filterDTO = new FilterDTO(FilterDTO.ValueType.DATE, "2016-01-10", FilterDTO.Rule.LESS);
        HashMap<String, FilterDTO> filterDTOs = new HashMap<>();
        filterDTOs.put("tradedate", filterDTO);
        SortFilterDTO sortFilterDTO = new SortFilterDTO(
                new SortDTO("numtrades", SortDTO.SortDirection.DESC),
                filterDTOs);
        List<SecurityJoinHistory> list = securityService.getJoinTable(sortFilterDTO);
        assertEquals(9, list.size());
        for (int i = 1; i < 9; i++) {
            assertTrue(list.get(i).getNumtrades() <= list.get(i - 1).getNumtrades());
        }
    }

    @Test
    @Order(8)
    void saveOne_WhenGivenSecurity_SavesToDatabase() {
        Security security = createSecurity(12);

        securityService.saveOne(security);
        Security securityFromService = securityService.findOneBySecid("secid 12");
        assertEquals(12, securityFromService.getEmitentId());
        assertEquals(1, securityFromService.getHistories().size());
        assertEquals("secid 12", securityFromService.getHistories().get(0).getSecid());
    }

    @Test
    @Order(9)
    void update_WhenGivenUpdateDTO_UpdatesAccordingly() {
        SecurityUpdateDTO updateDTO = new SecurityUpdateDTO();
        updateDTO.setSecid("secid 2");
        updateDTO.setEmitentTitle("emitentTitle was updated");
        updateDTO.setName("name was updated");
        Security security = securityService.update(updateDTO);
        assertEquals("secid 2", security.getSecid());
        assertEquals("emitentTitle was updated", security.getEmitentTitle());
        assertEquals("name was updated", security.getName());
        assertEquals("shortname 2", security.getShortname());
        assertEquals("regnumber 2", security.getRegnumber());
    }

    @Test
    @Order(10)
    void deleteOneBySecid_WhenGivenSecid_DeletesSecurityWithMatchingSecid() {
        Security security = securityService.findOneBySecid("secid 3");
        assertEquals("secid 3", security.getSecid());
        securityService.deleteOneBySecid("secid 3");
        assertThrows(ResponseStatusException.class, () -> securityService.findOneBySecid("secid 3"));
    }

    @Test
    @Order(11)
    void deleteOneBySecid_WhenGivenSecidThatDoesntExist_ThrowsException() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> securityService.findOneBySecid("fake secid"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @Order(12)
    void saveAll_WhenGivenListOfSecurities_SavesAll() {
        List<Security> securities = new ArrayList<Security>();
        securities.add(createSecurity(13));
        securities.add(createSecurity(14));

        securityService.saveAll(securities);

        SortFilterPageableDTO sortFilterPageableDTO = new SortFilterPageableDTO(
                new SortDTO("emitentId", SortDTO.SortDirection.DESC),
                new HashMap<>(),
                new PageableDTO(2, 0));

        Page<Security> securitiesFromService = securityService.findAll(sortFilterPageableDTO);
        assertEquals(14, securitiesFromService.getContent().get(0).getEmitentId());
        assertEquals(1, securitiesFromService.getContent().get(0).getHistories().size());
        assertEquals("secid 14", securitiesFromService.getContent().get(0).getHistories().get(0).getSecid());

        assertEquals(13, securitiesFromService.getContent().get(1).getEmitentId());
        assertEquals(1, securitiesFromService.getContent().get(1).getHistories().size());
        assertEquals("secid 13", securitiesFromService.getContent().get(1).getHistories().get(0).getSecid());
    }

    private Security createSecurity(int number) {
        Security security = new Security();
        security.setSecid("secid " + number);
        security.setShortname("shortname " + number);
        security.setName("name " + number);
        security.setRegnumber("regnumber " + number);
        security.setEmitentInn("emitentInn " + number);
        security.setEmitentTitle("emitentTitle " + number);
        security.setEmitentId(number);

        List<History> histories = new ArrayList<>();
        History history = new History();
        history.setSecid("secid " + number);
        history.setBoardid("boardid " + number);
        history.setTradedate(new Date(2016 - 1900, Calendar.JANUARY, number > 30 ? 30 : number));
        history.setNumtrades((double) number);
        history.setValue((double) (number * 100));
        history.setOpen((double) (number * 100) + 1.1);
        history.setLow((double) (number * 100) + 2.2);
        history.setHigh((double) (number * 100) + 3.3);
        history.setClose((double) (number * 100) + 4.4);
        histories.add(history);

        security.setHistories(histories);

        return security;
    }
}