package info.elexis.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import info.elexis.model.Contact;
import info.elexis.repository.ContactRepository;

@Repository
@Transactional(value = "defaultTransactionManager")
public class JpaContactRepositoryImpl implements ContactRepository {

	@PersistenceContext(unitName = "elexisPersistenceUnit")
	private EntityManager manager;

	@Override
	public Contact getById(String id) {
		return manager.find(Contact.class, id);
	}

}
