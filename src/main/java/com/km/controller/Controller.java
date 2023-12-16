package com.km.controller;

import com.km.model.DBMsg;
import com.km.repository.DBMsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
	public List<DBMsg> countries() {
		return dbMsgRepository.findAll();
	}
}