# Elexis overlay for MITREid Connect

This overlay creates a MITREid Connect web-application configured for usage
with an Elexis database.

The webapp requires the file `elexis-connection.xml` to exist in the user home's `elexis-server` directory.

## Overlay modifications

* Use the Elexis-Database configured via `elexis-connection.xml` as user repository.
* Use H2 as OpenID Database, and persist the database to `~/elexis-server/openid`
* Enable *Resource Owner Credentials* aka *password* flow

## Relevant links / discussions

* https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/wiki/API
* https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/issues/1309
* https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/issues/1213
* https://github.com/mitreid-connect/OpenID-Connect-Java-Spring-Server/issues/1220

## Building

Run `mvn package` to generate the overlay `openid.war`

## Testing locally

From https://github.com/iipc/openwayback-sample-overlay

* **mvn jetty:run-war** to start an instance of Jetty for testing
