package info.elexis;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.model.SystemScope;
import org.mitre.oauth2.repository.OAuth2ClientRepository;
import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.oauth2.service.SystemScopeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Initialize the OpenID database to match the requirements of Elexis-Server;
 * provide the required files to elexis-server
 */
public class OpenIdForElexisServerInitializingBean implements InitializingBean {

	// Use as system property to enable unit-test mode, registers unit-test client
	public static final String OPENID_UNIT_TEST = "openid.unit-test";
	public static final String ELEXIS_SERVER_UNITTEST_CLIENT = "es-unittest-client";
	// --
	
	public static final String ELEXIS_SERVER_INTROSPECTION_CLIENT_ID = "es-introspection-client";
	public static final String ESADMIN_SCOPE = "esadmin";
	public static final String FHIR_SCOPE = "fhir";

	private final Logger log = LoggerFactory.getLogger(OpenIdForElexisServerInitializingBean.class);

	@Autowired
	private OAuth2ClientRepository clientRepository;

	@Autowired
	private ClientDetailsEntityService clientService;

	@Autowired
	private SystemScopeService systemScopeService;

	private ClientDetailsEntityBuilder clientDetailsEntityBuilder = new ClientDetailsEntityBuilder();

	@Override
	public void afterPropertiesSet() throws Exception {

		assertElexisServerSystemScopes();
		generateOrReplaceElexisServerIntrospectionClient();

		addOrRemoveUnitTestClients(isTestMode());

	}

	/**
	 * assert the existence of the required elexis-server system scopes
	 */
	private void assertElexisServerSystemScopes() {

		SystemScope esadminScope = systemScopeService.getByValue(ESADMIN_SCOPE);
		if (esadminScope == null) {
			esadminScope = new SystemScope(ESADMIN_SCOPE);
			esadminScope.setDefaultScope(false);
			esadminScope.setDescription("Elexis-Server Administration");
			esadminScope.setIcon("star");
			systemScopeService.save(esadminScope);
		}

		SystemScope fhirScope = systemScopeService.getByValue(FHIR_SCOPE);
		if (fhirScope == null) {
			fhirScope = new SystemScope(FHIR_SCOPE);
			fhirScope.setDefaultScope(false);
			fhirScope.setDescription("FHIR Access");
			fhirScope.setIcon("fire");
			systemScopeService.save(fhirScope);
		}
	}

	/**
	 * Generate an introspection client used by the shiro filter in elexis-server to
	 * verify the presented tokens. Store the clientId, clientSecret in
	 * <code>~/elexis-server/es-introspection-client.auth</code>
	 */
	private void generateOrReplaceElexisServerIntrospectionClient() {

		ClientDetailsEntity esIntrospectionClient = clientRepository
				.getClientByClientId(ELEXIS_SERVER_INTROSPECTION_CLIENT_ID);

		if (esIntrospectionClient == null) {
			log.warn("Adding elexis-server protected-resource introspection client.");

			esIntrospectionClient = clientDetailsEntityBuilder.buildIntrospectionClient();
			esIntrospectionClient = clientService.generateClientSecret(esIntrospectionClient);

			// https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/issues/889
			// introspection only allowed for scopes that are granted to the introspection client
			esIntrospectionClient.setScope(new HashSet<String>(Arrays.asList(ESADMIN_SCOPE,FHIR_SCOPE)));
			
			esIntrospectionClient = clientService.saveNewClient(esIntrospectionClient);

			Path introspectionClientAuthPath = ElexisServer.getElexisServerHomeDirectory()
					.resolve("es-introspection-client.auth");
			List<String> lines = Arrays.asList("# Written " + new Date(),
					ELEXIS_SERVER_INTROSPECTION_CLIENT_ID + ":" + esIntrospectionClient.getClientSecret());
			try {
				Files.write(introspectionClientAuthPath, lines, Charset.forName("UTF-8"));
			} catch (IOException e) {
				log.error("Error writing file [{}]", introspectionClientAuthPath, e);
			}
		}
	}

	public static boolean isTestMode() {
		String testMode = System.getProperty(OPENID_UNIT_TEST);
		return Boolean.TRUE.equals(Boolean.parseBoolean(testMode));
	}

	private void addOrRemoveUnitTestClients(boolean testMode) {
		ClientDetailsEntity esUnitTestClient = clientRepository.getClientByClientId(ELEXIS_SERVER_UNITTEST_CLIENT);

		if (testMode) {
			if (esUnitTestClient == null) {
				log.warn("Adding unit-test client [{}]", ELEXIS_SERVER_UNITTEST_CLIENT);
				esUnitTestClient = clientDetailsEntityBuilder.buildUnitTestClient();
				esUnitTestClient.setClientSecret(ELEXIS_SERVER_UNITTEST_CLIENT);
				clientService.saveNewClient(esUnitTestClient);
			}
		} else {
			if (esUnitTestClient != null) {
				log.warn("Deleting unit-test client [{}]", ELEXIS_SERVER_UNITTEST_CLIENT);
				clientService.deleteClient(esUnitTestClient);
			}
		}
	}

}
