package info.elexis.repository;

import info.elexis.model.Config;

public interface ConfigRepository {

	public Config getByParam(String param);

}
