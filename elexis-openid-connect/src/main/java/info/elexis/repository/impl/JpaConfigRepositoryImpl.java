package info.elexis.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import info.elexis.model.Config;
import info.elexis.repository.ConfigRepository;

@Repository
@Transactional(value = "defaultTransactionManager")
public class JpaConfigRepositoryImpl implements ConfigRepository {

	@PersistenceContext(unitName = "elexisPersistenceUnit")
	private EntityManager manager;

	@Override
	public Config getByParam(String param) {
		return manager.find(Config.class, param);
	}

}
