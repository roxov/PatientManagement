package fr.asterox.PatientManagement.controller;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.asterox.PatientManagement.bean.Patient;
import fr.asterox.PatientManagement.service.PatientService;

@RestController
@RequestMapping("patient")
public class PatientController {

	private static final Logger LOGGER = LogManager.getLogger(PatientController.class);

	@Autowired
	private PatientService patientService;

	@GetMapping(value = "/get")
	public Optional<Patient> getPatientById(
			@RequestParam @NotNull(message = "patientId is compulsory") Long patientId) {

		LOGGER.info("Getting patient identified by id");
		return patientService.findById(patientId);
	}
}
