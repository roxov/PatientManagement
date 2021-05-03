package fr.asterox.PatientManagement.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataBaseConfig {
	private static final Logger LOGGER = LogManager.getLogger(DataBaseConfig.class);
	private String driver;
	private String url;
	private String username;
	private String psw;

	@Autowired
	public DataBaseConfig(@Value("${spring.datasource.driver-class-name}") String driver,
			@Value("${spring.datasource.url}") String url, @Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String psw) {
		this.driver = driver;
		this.url = url;
		this.username = username;
		this.psw = psw;
	}

	public void clearDataBase() {
		Connection connection = null;
		try {
			connection = this.getConnection();
			connection.prepareStatement("truncate table patient").execute();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		LOGGER.info("Create DB connection");
		Class.forName(driver);
		return DriverManager.getConnection(url, username, psw);
	}
}
