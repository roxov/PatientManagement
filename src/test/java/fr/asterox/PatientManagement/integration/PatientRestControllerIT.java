package fr.asterox.PatientManagement.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import fr.asterox.PatientManagement.bean.Patient;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PatientRestControllerIT {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	DataBaseConfig dataBaseConfig;

	@BeforeEach
	public void setUp() {
		dataBaseConfig.clearDataBase();
	}

	@WithMockUser
	@Test
	void givenAPatient_whenPostPatient_thenReturns200AndPatient() throws Exception {
		Patient testNonePatient = new Patient("Test", "TestNone", new Date(1966 - 12 - 31), "1 Brookside St",
				"100-222-3333", Patient.Sex.F);
		mockMvc.perform(post("/rest/patient").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(testNonePatient))).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.givenName").value("Test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.address").value("1 Brookside St"));
	}

	@WithMockUser
	@Test
	public void givenAPatientId_whenGetPatient_thenReturnOkAndPatient() throws Exception {
		// GIVEN
		Patient patient = new Patient("coco", "lerigolo", new Date(1985 - 04 - 31), "44 rue des zouaves",
				"100-111-3333", Patient.Sex.M);
		String jsonResponse = mockMvc
				.perform(post("/rest/patient").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(patient)))
				.andReturn().getResponse().getContentAsString();
		Long patientId = ((Integer) JsonPath.parse(jsonResponse).read("$.patientId")).longValue();

		// WHEN THEN
		mockMvc.perform(MockMvcRequestBuilders.get("/rest/patient/{id}", patientId).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.givenName").value("coco"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.sex").value("M"));
	}

	@WithMockUser
	@Test
	void givenAPatient_whenPutPatient_thenReturns200AndUpdatedPatient() throws Exception {
		// GIVEN
		Patient newPatient = new Patient("Test", "TestNone", new Date(1966 - 12 - 31), "1 Brookside St", "100-222-3333",
				Patient.Sex.F);
		String jsonResponse = mockMvc
				.perform(post("/rest/patient").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newPatient)))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		Long newPatientId = ((Integer) JsonPath.parse(jsonResponse).read("$.patientId")).longValue();

		Patient updatedPatient = new Patient(newPatientId, "Test2", "TestNone", new Date(1966 - 12 - 31), "22 Rico St",
				"100-222-3333", Patient.Sex.F);

		// WHEN THEN
		mockMvc.perform(put("/rest/patient/{id}", newPatientId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedPatient))).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.givenName").value("Test2"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.address").value("22 Rico St"));
	}

}
