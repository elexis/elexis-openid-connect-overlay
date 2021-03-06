package org.mitre.openid.connect.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import info.elexis.model.Config;
import info.elexis.model.Contact;
import info.elexis.repository.ConfigRepository;
import info.elexis.repository.ContactRepository;

public class ElexisConfigurationPropertiesBean extends ConfigurationPropertiesBean {

	private static final Logger log;

	public static final String ISSUER;

	static {
		log = LoggerFactory.getLogger(ElexisConfigurationPropertiesBean.class);

		String openidIssuerPort = System.getProperty("openid.issuer.port");
		String _issuerPort = (openidIssuerPort != null) ? openidIssuerPort : "8480";

		String openidIssuer = System.getProperty("openid.issuer");
		String _issuer = null;

		if (openidIssuer == null) {
			try {
				InetAddress myHost = InetAddress.getLocalHost();
				_issuer = "https://" + myHost.getCanonicalHostName() + ":" + _issuerPort + "/openid/";
			} catch (UnknownHostException e) {
			}
		} else {
			_issuer = openidIssuer;
			if(!_issuer.startsWith("http")) {
				_issuer = "https://"+_issuer+":"+_issuerPort+"/openid/";
			}
		}

		ISSUER = _issuer;

		log.info("OpenId Issuer is {}", ISSUER);
	}

	private String installationTitleString = "Unknown main contact";
	private String installationBodyString = "Please set a mainContact in Elexis Database to see your info here.";

	@Autowired
	public ElexisConfigurationPropertiesBean(ConfigRepository configRepository, ContactRepository contactRepository) {
		Config mainContactId = configRepository.getByParam("mainContactId");
		if (mainContactId != null) {
			Contact mainContact = contactRepository.getById(mainContactId.getWert());
			if (mainContact != null) {
				setInstallationTitleString(StringEscapeUtils.escapeHtml4(mainContact.getDescription1()));
				setInstallationBodyString(StringEscapeUtils.escapeHtml4(mainContact.getComment()));
			} else {
				log.warn("mainContactId [{}] not resolvable", mainContactId.getWert());
			}
		} else {
			log.warn("Could not find config entry [mainContactId]");
		}

	}

	public String getInstallationTitleString() {
		return installationTitleString;
	}

	public void setInstallationTitleString(String installationTitleString) {
		this.installationTitleString = installationTitleString;
	}

	public String getInstallationBodyString() {
		return installationBodyString;
	}

	public void setInstallationBodyString(String installationBodyString) {
		this.installationBodyString = installationBodyString;
	}

	@Override
	public String getIssuer() {
		return ISSUER;
	}

}
