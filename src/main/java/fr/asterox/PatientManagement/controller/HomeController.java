package fr.asterox.PatientManagement.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	private static final Logger LOGGER = LogManager.getLogger(HomeController.class);

	@RequestMapping("/")
	public String home(Model model) {
		LOGGER.info("Getting home page");
		return "home";
	}
}
