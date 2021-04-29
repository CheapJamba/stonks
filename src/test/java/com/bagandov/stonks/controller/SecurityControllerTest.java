package com.bagandov.stonks.controller;

import com.bagandov.stonks.TestUtils;
import com.bagandov.stonks.condition.FilterDTO;
import com.bagandov.stonks.condition.PageableDTO;
import com.bagandov.stonks.condition.SortDTO;
import com.bagandov.stonks.condition.SortFilterPageableDTO;
import com.bagandov.stonks.model.Security;
import com.bagandov.stonks.service.SecurityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SecurityController.class)
class SecurityControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SecurityService securityService;

    @Test
    void getSecurities() throws Exception {
        SortFilterPageableDTO dto = new SortFilterPageableDTO(
                new SortDTO("emitentId", SortDTO.SortDirection.DESC),
                new HashMap<String, FilterDTO>(),
                new PageableDTO(3, 0)
        );

        List<Security> securities = TestUtils.createSecurityList(11, 12, 13);
        Page<Security> page = new PageImpl<Security>(securities);
        Mockito.when(securityService.findAll(ArgumentMatchers.any(SortFilterPageableDTO.class))).thenReturn(page);

        System.out.println(TestUtils.toJson(dto));

        RequestBuilder request = MockMvcRequestBuilders.get("/securities")
                .content(TestUtils.toJson(dto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].secid").value("secid 11"))
                .andExpect(jsonPath("$.content[1].secid").value("secid 12"))
                .andExpect(jsonPath("$.content[2].secid").value("secid 13"));
    }

    @Test
    void getSecurityById() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/securities/secid");
        Mockito.when(securityService.findOneBySecid("secid")).thenReturn(TestUtils.createSecurity(11));
        mvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.secid").value("secid 11"))
                .andExpect(jsonPath("$.shortname").value("shortname 11"))
                .andExpect(jsonPath("$.regnumber").value("regnumber 11"))
                .andExpect(jsonPath("$.histories", hasSize(1)));

    }
}