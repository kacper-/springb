package com.km.controller;

import com.km.model.Country;
import com.km.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

	@Autowired
	private final CountryRepository countryRepository;

	public Controller(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}

	@GetMapping("/time")
	public String time() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
	}

	@GetMapping("/countries")
	public List<Country> countries() {
		return countryRepository.findAll();
	}
}