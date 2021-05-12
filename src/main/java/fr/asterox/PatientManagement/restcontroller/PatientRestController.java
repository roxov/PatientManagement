package fr.asterox.PatientManagement.restcontroller;

import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.asterox.PatientManagement.bean.Patient;
import fr.asterox.PatientManagement.service.PatientService;

@RestController
@RequestMapping("rest/patient")
public class PatientRestController {

	private static final Logger LOGGER = LogManager.getLogger(PatientRestController.class);

	@Autowired
	private PatientService patientService;

	@PostMapping
	public Patient addPatient(@RequestBody Patient patient) {
		LOGGER.info("Adding new user");
		return patientService.addPatient(patient);
	}

	@GetMapping(value = "/{id}")
	public Patient getPatientById(@PathVariable("id") @NotNull(message = "patientId is compulsory") Long patientId) {
		LOGGER.info("Getting patient identified by id");
		return patientService.findById(patientId);
	}

	@PutMapping(value = "/{id}")
	public Patient updatePatient(@PathVariable("id") @NotNull(message = "patientId is compulsory") Long patientId,
			@RequestBody Patient patient) {
		if (patient.getPatientId() == null) {
			throw new IllegalArgumentException("The id is mandatory");
		}
		LOGGER.info("Updating patient");
		return patientService.updatePatient(patientId, patient);
	}

	@GetMapping(value = "/exist/{id}")
	public boolean askExistenceOfPatient(
			@PathVariable("id") @NotNull(message = "patientId is compulsory") Long patientId) {
		LOGGER.info("Asking for existence of patient with id : " + patientId);
		return patientService.askExistenceOfPatient(patientId);
	}

	@DeleteMapping(value = "/{id}")
	public String deletePatientById(@PathVariable("id") @NotNull(message = "patient id is compulsory") Long patientId) {
		LOGGER.info("Deleting patient identified by id : " + patientId);
		patientService.deletePatient(patientId);
		return "The patient with id :" + patientId + " is deleted.";
	}

}
