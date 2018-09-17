# Elexis overlay for MITREid Connect

This overlay creates a MITREid Connect web-application configured for usage
with an Elexis database.

The webapp requires the file `elexis-connection.xml` to exist in the user home's `elexis-server` directory.

## Overlay modifications

* Use the Elexis-Database configured via `elexis-connection.xml` as user repository.
* Use H2 as OpenID Database, and persist the database to `~/elexis-server/openid`
* Enable *Resource Owner Credentials* aka *password* flow
* Programmatically initialize the OpenID database
  * Insert the required systems scopes
  * Add an introspection client for Elexis-Server
* On startup with `-Dopenid.unit-test=true` add a unit-test client supporting password grant type
* Read the mainContact to show in the web-interface from the Elexis database
* Implement 2-factor-authentication using TOTP
* Hardcode base tag in header.tag to /openid/
* Dynamic issuer resolution according to hostname

## TODO

* Is the user allowed to request a specific scope? https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/issues/351
* Remove unit-test-client on shutdown

## Relevant links / discussions

* https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/wiki/API
* https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/issues/1309
* https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/issues/1213
* https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/issues/1220
* http://www.baeldung.com/spring-security-two-factor-authentication-with-soft-token

## Building

Run `mvn package` to generate the overlay `openid.war`

## Testing locally

From https://github.com/iipc/openwayback-sample-overlay

* **mvn jetty:run-war** to start an instance of Jetty for testing
