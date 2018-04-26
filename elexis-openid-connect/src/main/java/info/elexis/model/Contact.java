package info.elexis.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.ReadOnly;

@Entity
@Table(name = "KONTAKT")
@Cache(type = CacheType.NONE)
@ReadOnly
public class Contact extends AbstractDBObjectIdDeleted implements Serializable {
	protected static final long serialVersionUID = 1L;

	@Lob()
	protected String anschrift;

	@Lob()
	@Column(name = "bemerkung")
	protected String comment;

	@Column(length = 255, name = "bezeichnung1")
	protected String description1;

	@Column(length = 255, name = "bezeichnung2")
	protected String description2;

	/**
	 * Contains the following values in the respective instantiations of contact
	 * isIstPatient(): ? isIstPerson(): if medic: area of expertise isIstMandant():
	 * username/mandant short name isIstAnwender(): username/mandant short name
	 * isIstOrganisation(): contact person isIstLabor(): ?
	 */
	@Column(length = 255, name = "bezeichnung3")
	protected String description3;

	@Column(length = 255)
	protected String email;

	@Column(length = 30)
	protected String fax;

	@Column(name = "geburtsdatum", length = 8)
	protected LocalDate dob;

	@Column(length = 10)
	protected String gruppe;

	@Column(name = "istPerson")
	protected boolean person;

	@Column(name = "istPatient")
	protected boolean patient;

	@Column(name = "istAnwender")
	protected boolean user;

	@Column(name = "istMandant")
	protected boolean mandator;

	@Column(name = "istOrganisation")
	protected boolean organisation;

	@Column(name = "istLabor")
	protected boolean laboratory;

	@Column(length = 30, name = "natelNr")
	protected String mobile;

	@Column(length = 255, name = "ort")
	protected String city;

	/**
	 * Contains according to contact-type manifestation:<br>
	 * isPatient: patientNr<br>
	 * isOrganization /<br>
	 * isPerson: ID
	 */
	@Column(length = 40, name = "patientNr")
	protected String code;

	@Column(length = 6, name = "plz")
	protected String zip;

	@Column(length = 255, name = "strasse")
	protected String street;

	@Column(length = 30, name = "telefon1")
	protected String phone1;

	@Column(length = 30, name = "telefon2")
	protected String phone2;

	@Column(length = 255)
	protected String titel;

	@Column(length = 255)
	protected String titelSuffix;

	@Column(length = 255)
	protected String website;

	// ---------------------------------------------
	public Contact() {
	}

	public String getLabel() {
		return getDescription1() + "," + getDescription2() + "," + getDescription3();
	}

	public String getAnschrift() {
		return anschrift;
	}

	public void setAnschrift(String anschrift) {
		this.anschrift = anschrift;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getGruppe() {
		return gruppe;
	}

	public void setGruppe(String gruppe) {
		this.gruppe = gruppe;
	}

	public boolean isUser() {
		return user;
	}

	public void setUser(boolean user) {
		this.user = user;
	}

	public boolean isLaboratory() {
		return laboratory;
	}

	public void setLaboratory(boolean laboratory) {
		this.laboratory = laboratory;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getTitelSuffix() {
		return titelSuffix;
	}

	public void setTitelSuffix(String titelSuffix) {
		this.titelSuffix = titelSuffix;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getDescription1() {
		return description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public String getDescription3() {
		return description3;
	}

	public void setDescription3(String description3) {
		this.description3 = description3;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public boolean isPerson() {
		return person;
	}

	public void setPerson(boolean person) {
		this.person = person;
	}

	public boolean isPatient() {
		return patient;
	}

	public void setPatient(boolean patient) {
		this.patient = patient;
	}

	public boolean isMandator() {
		return mandator;
	}

	public void setMandator(boolean mandator) {
		this.mandator = mandator;
	}

	public boolean isOrganisation() {
		return organisation;
	}

	public void setOrganisation(boolean organisation) {
		this.organisation = organisation;
	}

}
