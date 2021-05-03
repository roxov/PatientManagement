package fr.asterox.PatientManagement.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.asterox.PatientManagement.bean.Patient;
import fr.asterox.PatientManagement.consumer.IPatientRepository;
import fr.asterox.PatientManagement.util.ExistingPatientException;
import fr.asterox.PatientManagement.util.NoPatientException;

@Service
public class PatientService implements IPatientService {

	@Autowired
	IPatientRepository patientRepository;

	private static final Logger LOGGER = LogManager.getLogger(PatientService.class);

	@Override
	public Patient addPatient(Patient patient) {
		List<Patient> patientsWithSameFamilyName = patientRepository.findAllByFamilyName(patient.getFamilyName());
		for (Patient p : patientsWithSameFamilyName) {
			if (p.getGivenName().equals(patient.getGivenName())) {
				LOGGER.info("The requested patient already exists.");
				throw new ExistingPatientException("The requested patient already exists.");
			}
		}

		LOGGER.info("Adding new patient");
		return patientRepository.save(patient);
	}

	@Override
	public Patient findById(Long patientId) {
		Optional<Patient> patient = patientRepository.findById(patientId);

		if (patient.isEmpty()) {
			LOGGER.info("The requested patient does not exist.");
			throw new NoPatientException("This patient does not exist.");
		}

		LOGGER.info("Creating UserAccount");
		return patient.get();
	}

	@Override
	public Patient updatePatient(Patient patient) {
		Optional<Patient> searchedPatient = patientRepository.findById(patient.getPatientId());

		if (searchedPatient.isEmpty()) {
			LOGGER.info("The requested patient does not exist.");
			throw new NoPatientException("This patient does not exist.");
		}
		LOGGER.info("Updating patient");
		return patientRepository.save(patient);
	}

	public void deletePatient(Long patientId) {
		Optional<Patient> patient = patientRepository.findById(patientId);

		if (patient.isEmpty()) {
			LOGGER.info("The requested patient does not exist.");
			throw new NoPatientException("This patient does not exist.");
		}

		LOGGER.info("Deleting patient");
		patientRepository.deleteById(patientId);
	}

	public IPatientRepository setPatientRepository(IPatientRepository patientRepository) {
		return this.patientRepository = patientRepository;
	}
}
