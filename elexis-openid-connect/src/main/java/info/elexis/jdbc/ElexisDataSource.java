package info.elexis.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;

import com.zaxxer.hikari.HikariDataSource;

public class ElexisDataSource extends HikariDataSource {

	private final Logger log = LoggerFactory.getLogger(ElexisDataSource.class);

	public ElexisDataSource() {
		super();

		configureDataSource();
	}

	private void configureDataSource() throws BeanCreationException {
		String userHomeProp = System.getProperty("user.home");
		Path dbConnectionConfigPath = Paths.get(userHomeProp, "elexis-server", "elexis-connection.xml");
		if (dbConnectionConfigPath.toFile().canRead()) {
			try (InputStream is = Files.newInputStream(dbConnectionConfigPath, StandardOpenOption.READ)) {
				DBConnection dbc = DBConnection.unmarshall(is);
				switch (dbc.rdbmsType) {
				case MySQL:
					setDriverClassName("com.mysql.jdbc.Driver");
					break;
				case PostgreSQL:
					setDriverClassName("org.postgresql.Driver");
					break;
				case H2:
					setDriverClassName("org.h2.Driver");
					break;
				default:
					throw new BeanCreationException("Invalid rdbmsType [" + dbc.rdbmsType + "]");
				}

				setJdbcUrl(dbc.connectionString);
				setUsername(dbc.username);
				setPassword(dbc.password);
				log.info("Initialized elexis connection from [{}]", dbConnectionConfigPath.toAbsolutePath());
			} catch (IOException | JAXBException e) {
				log.warn("Error opening [{}]", dbConnectionConfigPath.toAbsolutePath(), e);
			}
			return;
		}

		throw new BeanCreationException(
				"Could not read db connection configuration from [" + dbConnectionConfigPath.toAbsolutePath() + "]");
	}
}
