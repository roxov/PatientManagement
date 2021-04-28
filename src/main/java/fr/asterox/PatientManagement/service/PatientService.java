package fr.asterox.PatientManagement.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.asterox.PatientManagement.bean.Patient;
import fr.asterox.PatientManagement.consumer.IPatientRepository;

@Service
public class PatientService {

	@Autowired
	IPatientRepository patientRepository;

	private static final Logger LOGGER = LogManager.getLogger(PatientService.class);

	public Optional<Patient> findById(Long patientId) {
		Optional<Patient> patient = patientRepository.findById(patientId);

		if (patient == null) {
			LOGGER.error("This patient does not exist.");
			return Optional.empty();
		}

		LOGGER.info("Creating UserAccount");
		return patient;
	}
}
