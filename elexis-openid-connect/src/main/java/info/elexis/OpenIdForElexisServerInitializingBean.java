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
import org.mitre.oauth2.model.ClientDetailsEntity.AuthMethod;
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
			esadminScope.setRestricted(true);
			esadminScope.setDescription("Elexis-Server Administration");
			esadminScope.setIcon("star");
			systemScopeService.save(esadminScope);
		}

		SystemScope fhirScope = systemScopeService.getByValue(FHIR_SCOPE);
		if (fhirScope == null) {
			fhirScope = new SystemScope(FHIR_SCOPE);
			fhirScope.setDefaultScope(false);
			fhirScope.setRestricted(true);
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

			esIntrospectionClient = new ClientDetailsEntity();
			esIntrospectionClient.setClientId(ELEXIS_SERVER_INTROSPECTION_CLIENT_ID);
			esIntrospectionClient = clientService.generateClientSecret(esIntrospectionClient);
			esIntrospectionClient.setTokenEndpointAuthMethod(AuthMethod.SECRET_BASIC);
			esIntrospectionClient.setClientName("Elexis-Server Internal Client");
			esIntrospectionClient.setClientDescription(
					"Elexis-Server protected-resource introspection client. Required for elexis-server operation. Do not remove.");
			esIntrospectionClient.setClientUri("http://www.elexis.info");
			esIntrospectionClient.setSoftwareId("elexis-server");
			esIntrospectionClient.setLogoUri("http://elexis.info/images/splash.png");

			// no grant types are allowed
			esIntrospectionClient.setGrantTypes(new HashSet<String>());
			esIntrospectionClient.setResponseTypes(new HashSet<String>());
			esIntrospectionClient.setRedirectUris(new HashSet<String>());

			// don't issue tokens to this client
			esIntrospectionClient.setAccessTokenValiditySeconds(0);
			esIntrospectionClient.setIdTokenValiditySeconds(0);
			esIntrospectionClient.setRefreshTokenValiditySeconds(0);

			// clear out unused fields
			esIntrospectionClient.setDefaultACRvalues(new HashSet<String>());
			esIntrospectionClient.setDefaultMaxAge(null);
			esIntrospectionClient.setIdTokenEncryptedResponseAlg(null);
			esIntrospectionClient.setIdTokenEncryptedResponseEnc(null);
			esIntrospectionClient.setIdTokenSignedResponseAlg(null);
			esIntrospectionClient.setInitiateLoginUri(null);
			esIntrospectionClient.setPostLogoutRedirectUris(null);
			esIntrospectionClient.setRequestObjectSigningAlg(null);
			esIntrospectionClient.setRequireAuthTime(null);
			esIntrospectionClient.setReuseRefreshToken(false);
			esIntrospectionClient.setSectorIdentifierUri(null);
			esIntrospectionClient.setSubjectType(null);
			esIntrospectionClient.setUserInfoEncryptedResponseAlg(null);
			esIntrospectionClient.setUserInfoEncryptedResponseEnc(null);
			esIntrospectionClient.setUserInfoSignedResponseAlg(null);

			// this client has access to the introspection endpoint
			esIntrospectionClient.setAllowIntrospection(true);
			esIntrospectionClient.setDynamicallyRegistered(false);

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

	public static final String BOOLEAN_FLAG = "es.test";

	public static boolean isTestMode() {
		String testMode = System.getProperty(BOOLEAN_FLAG);
		return Boolean.TRUE.toString().equalsIgnoreCase(testMode);
	}

	private void addOrRemoveUnitTestClients(boolean testMode) {
		// TODO Auto-generated method stub
	}

}
