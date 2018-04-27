package org.mitre.openid.connect.config;

import org.springframework.beans.factory.annotation.Autowired;

import info.elexis.model.Config;
import info.elexis.model.Contact;
import info.elexis.repository.ConfigRepository;
import info.elexis.repository.ContactRepository;

public class ElexisConfigurationPropertiesBean extends ConfigurationPropertiesBean {

	private String installationTitleString = "Unknown main contact";
	private String installationBodyString = "\nPlease set a mainContact in Elexis Database to see your info here.";

	@Autowired
	public ElexisConfigurationPropertiesBean(ConfigRepository configRepository, ContactRepository contactRepository) {
		Config mainContactId = configRepository.getByParam("mainContactId");
		if (mainContactId != null) {
			Contact mainContact = contactRepository.getById(mainContactId.getWert());
			if (mainContactId != null) {
				setInstallationTitleString(mainContact.getDescription1());
				setInstallationBodyString(
						mainContact.getStreet() + ", " + mainContact.getZip() + " " + mainContact.getCity());
			}
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

}
