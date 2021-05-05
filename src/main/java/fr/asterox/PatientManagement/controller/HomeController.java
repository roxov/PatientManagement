package fr.asterox.PatientManagement.controller;

import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	private static final Logger LOGGER = LogManager.getLogger(HomeController.class);

	@GetMapping("/")
	public ResponseEntity<Void> home() {
		LOGGER.info("Redirecting to home page of API Gateway");
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("http://localhost:8080/")).build();
	}

}
