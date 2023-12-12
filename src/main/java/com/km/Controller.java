package com.km;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@GetMapping("/time")
	public String index() {
		return "OK";
	}

}