package info.elexis;

import java.util.HashSet;
import java.util.Set;

import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.model.ClientDetailsEntity.AuthMethod;

public class ClientDetailsEntityBuilder  {

	public ClientDetailsEntity buildIntrospectionClient() {
		ClientDetailsEntity esIntrospectionClient = new ClientDetailsEntity();
		esIntrospectionClient.setClientId(OpenIdForElexisServerInitializingBean.ELEXIS_SERVER_INTROSPECTION_CLIENT_ID);

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
		
		return esIntrospectionClient;
	}

	public ClientDetailsEntity buildUnitTestClient() {
		ClientDetailsEntity unitTestClient = new ClientDetailsEntity();
		unitTestClient.setClientId(OpenIdForElexisServerInitializingBean.ELEXIS_SERVER_UNITTEST_CLIENT);

		unitTestClient.setTokenEndpointAuthMethod(AuthMethod.SECRET_BASIC);
		unitTestClient.setClientName("Elexis-Server Unit-Test Client");
		unitTestClient.setClientDescription(
				"Elexis-Server Unit-Test Client. MUST NOT EXIST ON PRODUCTIVE SYSTEMS! IF YOU SEE THIS CLIENT, DELETE IT!");

		Set<String> grantTypes = new HashSet<>();
		grantTypes.add("password");
		unitTestClient.setGrantTypes(grantTypes);
		unitTestClient.setResponseTypes(new HashSet<String>());
		unitTestClient.setRedirectUris(new HashSet<String>());
		
		unitTestClient.setAccessTokenValiditySeconds(3600);

		// clear out unused fields
		unitTestClient.setDefaultACRvalues(new HashSet<String>());
		unitTestClient.setDefaultMaxAge(null);
		unitTestClient.setIdTokenEncryptedResponseAlg(null);
		unitTestClient.setIdTokenEncryptedResponseEnc(null);
		unitTestClient.setIdTokenSignedResponseAlg(null);
		unitTestClient.setInitiateLoginUri(null);
		unitTestClient.setPostLogoutRedirectUris(null);
		unitTestClient.setRequestObjectSigningAlg(null);
		unitTestClient.setRequireAuthTime(null);
		unitTestClient.setReuseRefreshToken(false);
		unitTestClient.setSectorIdentifierUri(null);
		unitTestClient.setSubjectType(null);
		unitTestClient.setUserInfoEncryptedResponseAlg(null);
		unitTestClient.setUserInfoEncryptedResponseEnc(null);
		unitTestClient.setUserInfoSignedResponseAlg(null);

		// this client has access to the introspection endpoint
		unitTestClient.setAllowIntrospection(false);
		unitTestClient.setDynamicallyRegistered(false);
		
		return unitTestClient;
	}

}
