package org.mitre.openid.connect.config;

import org.springframework.beans.factory.annotation.Autowired;

import info.elexis.model.Config;
import info.elexis.model.Contact;
import info.elexis.repository.ConfigRepository;
import info.elexis.repository.ContactRepository;

public class ElexisConfigurationPropertiesBean extends ConfigurationPropertiesBean {

	@Autowired
	private ConfigRepository configRepository;

	@Autowired
	private ContactRepository contactRepository;

	private String installationTitleString = "Unknown main contact";
	private String installationBodyString = "\nPlease set a mainContact in Elexis Database to see your info here.";

	public String getInstallationTitleString() {
		Contact mainContact = getMainContact();
		if (mainContact != null) {
			return mainContact.getDescription1();
		}
		return installationTitleString;
	}

	private Contact getMainContact() {
		Config mainContactId = configRepository.getByParam("mainContactId");
		if (mainContactId != null) {
			return contactRepository.getById(mainContactId.getWert());
		}
		return null;
	}

	public void setInstallationTitleString(String installationTitleString) {
		this.installationTitleString = installationTitleString;
	}

	public String getInstallationBodyString() {
		Contact mainContact = getMainContact();
		if (mainContact != null) {
			return mainContact.getStreet() + ", " + mainContact.getZip() + " " + mainContact.getCity();
		}
		return installationBodyString;
	}

	public void setInstallationBodyString(String installationBodyString) {
		this.installationBodyString = installationBodyString;
	}

}
