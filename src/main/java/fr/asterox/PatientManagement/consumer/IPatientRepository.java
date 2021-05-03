package fr.asterox.PatientManagement.consumer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.asterox.PatientManagement.bean.Patient;

/**
 * Repository pattern for Patient entities.
 *
 */
public interface IPatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
	/**
	 * Find patients with same family name.
	 * 
	 * @param familyName
	 * @return List<Patient>
	 * 
	 */

	List<Patient> findAllByFamilyName(String familyName);
}
