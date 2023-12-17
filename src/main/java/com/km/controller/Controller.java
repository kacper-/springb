package com.km.controller;

import com.km.model.DBMsg;
import com.km.repository.DBMsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private final DBMsgRepository dbMsgRepository;

    public Controller(DBMsgRepository countryRepository) {
        this.dbMsgRepository = countryRepository;
    }

    @GetMapping("/stats")
    public String stats() {
        int trueCount = dbMsgRepository.countByStatus(true);
        int falseCount = dbMsgRepository.countByStatus(false);
        return String.format("true count = %d<br>false count = %d", trueCount, falseCount);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DBMsg> all() {
        return dbMsgRepository.findAll();
    }
}