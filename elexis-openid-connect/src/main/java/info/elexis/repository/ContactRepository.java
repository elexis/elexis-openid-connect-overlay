package info.elexis.repository;

import info.elexis.model.Contact;

public interface ContactRepository {

	public Contact getById(String id);
	
}
