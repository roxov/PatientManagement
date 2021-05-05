package fr.asterox.PatientManagement.service;

import fr.asterox.PatientManagement.bean.Patient;

/**
 * 
 * Microservice managing data of patients.
 *
 */
public interface IPatientService {
	public Patient addPatient(Patient patient);

	public Patient findById(Long patientId);

	public Patient updatePatient(Long patientId, Patient patient);
}
