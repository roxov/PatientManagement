package fr.asterox.PatientManagement.consumer;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.asterox.PatientManagement.bean.Patient;

/**
 * Repository pattern for Patient entities.
 *
 */
public interface IPatientRepository extends JpaRepository<Patient, Long> {

}
