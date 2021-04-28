package fr.asterox.PatientManagement;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@EnableEncryptableProperties
@EnableFeignClients("fr.asterox")
@SpringBootApplication
public class PatientManagementApplication {
	private static final Logger LOGGER = LogManager.getLogger(PatientManagementApplication.class);

	public static void main(String[] args) throws IOException {
		LOGGER.info("Initializing PatientManagement");
		SpringApplication.run(PatientManagementApplication.class, args);
	}
}
