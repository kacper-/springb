package com.km;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class Controller {

	@GetMapping("/time")
	public String index() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
	}

}