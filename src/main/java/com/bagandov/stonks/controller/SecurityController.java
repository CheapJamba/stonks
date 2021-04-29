package com.bagandov.stonks.controller;

import com.bagandov.stonks.condition.SortFilterDTO;
import com.bagandov.stonks.condition.SortFilterPageableDTO;
import com.bagandov.stonks.model.DTO.SecurityUpdateDTO;
import com.bagandov.stonks.model.SecurityJoinHistory;
import com.bagandov.stonks.model.Security;
import com.bagandov.stonks.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("securities")
public class SecurityController {

    private SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping
    public Page<Security> getSecurities(@Valid @RequestBody SortFilterPageableDTO sortFilterPageableDTO) {
        return securityService.findAll(sortFilterPageableDTO);
    }

    @GetMapping("/{secid}")
    public Security getSecurityById(@PathVariable("secid") String secid) {
        return securityService.findOneBySecid(secid);
    }

    @PostMapping
    public Security addSecurity(@RequestBody Security securityToAdd) {
        if(!securityToAdd.isValid()) {
            throw new RuntimeException("Имя ценной бумаги должно состоять только из кириллицы, цифр и пробелов. " +
                    "Переданное имя: " + securityToAdd.getName());
        }
        securityService.saveOne(securityToAdd);
        return securityToAdd;
    }

    @DeleteMapping("/{secid}")
    public void deleteSecurityById(@PathVariable("secid") String secid) {
        securityService.deleteOneBySecid(secid);
    }

    @PutMapping
    public Security updateExistingSecurity(@RequestBody SecurityUpdateDTO updateDTO) {
        return securityService.update(updateDTO);
    }

    @GetMapping("join")
    public List<SecurityJoinHistory> getJoinTable(@Valid @RequestBody SortFilterDTO sortFilterDTO) {
        return securityService.getJoinTable(sortFilterDTO);
    }
}
