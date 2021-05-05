package fr.asterox.PatientManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.asterox.PatientManagement.bean.Patient;
import fr.asterox.PatientManagement.repository.IPatientRepository;
import fr.asterox.PatientManagement.util.NoPatientException;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
	@Mock
	private IPatientRepository patientRepository;

	@InjectMocks
	PatientService patientService;

	@BeforeEach
	public void setUp() {
		patientService = new PatientService();
		patientService.setPatientRepository(patientRepository);
	}

	@Test
	public void givenATestNonePatient_whenAddPatient_thenReturnCreatedPatient() {
		// GIVEN
		Patient testNonePatient = new Patient("Test", "TestNone", new Date(1966 - 12 - 31), "1 Brookside St",
				"100-222-3333", Patient.Sex.F);
		when(patientRepository.save(testNonePatient)).thenReturn(testNonePatient);

		// WHEN
		Patient result = patientService.addPatient(testNonePatient);

		// THEN
		verify(patientRepository, Mockito.times(1)).save(any(Patient.class));
		assertEquals("TestNone", result.getFamilyName());
		assertEquals(new Date(1966 - 12 - 31), result.getBirthdate());
		assertEquals(Patient.Sex.F, result.getSex());
	}

	@Test
	public void givenAnExistingPatient_whenAddPatient_thenThrowIllegalArgumentException() {
		// GIVEN
		Patient borderlinePatient = new Patient("Test", "TestBorderline", new Date(1945 - 06 - 24), "2 High St",
				"200-333-4444", Patient.Sex.M);
		List<Patient> patientsWithSameFamilyName = List.of(borderlinePatient);
		when(patientRepository.findAllByFamilyName("TestBorderline")).thenReturn(patientsWithSameFamilyName);

		// WHEN
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			patientService.addPatient(borderlinePatient);
		});

		// THEN
		verify(patientRepository, Mockito.times(1)).findAllByFamilyName(any(String.class));
		assertEquals(exception.getMessage(), "The requested patient already exists.");
	}

	@Test
	public void givenAPatientWithAParent_whenAddPatient_thenAddPatient() {
		// GIVEN
		Patient borderlinePatient = new Patient("Test", "TestBorderline", new Date(1945 - 06 - 24), "2 High St",
				"200-333-4444", Patient.Sex.M);
		Patient borderlineParent = new Patient("Parent", "TestBorderline", new Date(1925 - 06 - 24), "2 High St",
				"200-333-4444", Patient.Sex.M);
		List<Patient> patientsWithSameFamilyName = List.of(borderlineParent);
		when(patientRepository.save(borderlinePatient)).thenReturn(borderlinePatient);
		when(patientRepository.findAllByFamilyName("TestBorderline")).thenReturn(patientsWithSameFamilyName);

		// WHEN
		Patient result = patientService.addPatient(borderlinePatient);

		// THEN
		verify(patientRepository, Mockito.times(1)).findAllByFamilyName(any(String.class));
		verify(patientRepository, Mockito.times(1)).save(any(Patient.class));
		assertEquals("TestBorderline", result.getFamilyName());
		assertEquals(new Date(1945 - 06 - 24), result.getBirthdate());
		assertEquals(Patient.Sex.M, result.getSex());
	}

	@Test
	public void givenAPatientId_whenGetPatient_thenReturnThePatient() {
		// GIVEN
		Patient patient = new Patient(1L, "coco", "lerigolo", new Date(), "44 rue des zouaves", "0444444444",
				Patient.Sex.M);
		when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

		// WHEN
		Patient result = patientService.findById(1L);

		// THEN
		verify(patientRepository, Mockito.times(1)).findById(1L);
		assertEquals("44 rue des zouaves", result.getAddress());
		assertEquals("lerigolo", result.getFamilyName());
		assertEquals(Patient.Sex.M, result.getSex());
	}

	@Test
	public void givenAnNonexistentPatientId_whenGetPatient_thenThrowNoPatientException() {
		// GIVEN
		when(patientRepository.findById(1L)).thenReturn(Optional.empty());

		// WHEN
		Exception exception = assertThrows(NoPatientException.class, () -> {
			patientService.findById(1L);
		});

		// THEN
		verify(patientRepository, Mockito.times(1)).findById(1L);
		assertEquals(exception.getMessage(), "This patient does not exist.");
	}

	@Test
	public void givenAPatient_whenUpdatePatient_thenReturnUpdatedPatient() {
		// GIVEN
		Patient patient = new Patient(1L, "coco", "lerigolo", new Date(), "44 rue des zouaves", "0444444444",
				Patient.Sex.M);
		when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
		when(patientRepository.save(patient)).thenReturn(patient);

		// WHEN
		Patient updatedPatient = patientService.updatePatient(1L, patient);

		// THEN
		verify(patientRepository, Mockito.times(1)).findById(any(Long.class));
		verify(patientRepository, Mockito.times(1)).save(any(Patient.class));
		assertEquals("coco", updatedPatient.getGivenName());
		assertEquals("lerigolo", updatedPatient.getFamilyName());
		assertEquals(Patient.Sex.M, updatedPatient.getSex());
	}

	@Test
	public void givenNonexistentPatient_whenUpdatePatient_thenThrowNoPatientException() {
		// GIVEN
		Patient patient = new Patient(1L, "coco", "lerigolo", new Date(), "44 rue des zouaves", "0444444444",
				Patient.Sex.M);
		when(patientRepository.findById(1L)).thenReturn(Optional.empty());

		// WHEN
		Exception exception = assertThrows(NoPatientException.class, () -> {
			patientService.updatePatient(1L, patient);
		});

		// THEN
		verify(patientRepository, Mockito.times(1)).findById(1L);
		assertEquals(exception.getMessage(), "This patient does not exist.");
	}

	@Test
	public void givenAPatient_whenDeletePatient_thenVerifyMethodCalled() {
		// GIVEN
		Patient patient = new Patient(1L, "coco", "lerigolo", new Date(), "44 rue des zouaves", "0444444444",
				Patient.Sex.M);
		when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
		doNothing().when(patientRepository).deleteById(1L);

		// WHEN
		patientService.deletePatient(1L);

		// THEN
		verify(patientRepository, Mockito.times(1)).findById(any(Long.class));
		verify(patientRepository, Mockito.times(1)).deleteById(any(Long.class));
	}

	@Test
	public void givenANonexistentPatient_whenDeletePatient_thenThrowNoPatientException() {
		// GIVEN
		when(patientRepository.findById(1L)).thenReturn(Optional.empty());

		// WHEN
		Exception exception = assertThrows(NoPatientException.class, () -> {
			patientService.deletePatient(1L);
		});

		// THEN
		verify(patientRepository, Mockito.times(1)).findById(1L);
		assertEquals(exception.getMessage(), "This patient does not exist.");
	}
}
