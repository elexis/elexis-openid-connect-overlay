package info.elexis.security.provisioning;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.h2.util.StringUtils;
import org.mitre.openid.connect.model.DefaultUserInfo;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

public class ElexisJdbcUserDetailsManager extends JdbcUserDetailsManager implements UserInfoRepository {

	private final Logger logger = LoggerFactory.getLogger(ElexisJdbcUserDetailsManager.class);

	private final String getKontaktByUsernameQuery = "SELECT U.ID, K.Bezeichnung1, K.Bezeichnung2, K.Geschlecht, K.Email FROM USER_ U, KONTAKT K WHERE U.KONTAKT_ID = K.ID AND U.ID = ?";

	public ElexisJdbcUserDetailsManager() {
		super();

		setUsersByUsernameQuery(
				"select id, concat(hashed_password,'|', salt), is_active, is_administrator from user_ where deleted = '0' and id=?");
		setAuthoritiesByUsernameQuery("select user_id, id from user_role_joint where user_id = ?");

	}

	protected List<UserDetails> loadUsersByUsername(String username) {
		return getJdbcTemplate().query(getUsersByUsernameQuery(), new String[] { username },
				new RowMapper<UserDetails>() {
					@Override
					public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
						String username = rs.getString(1);
						String password = rs.getString(2);
						boolean enabled = rs.getBoolean(3);
						boolean isAdmin = rs.getBoolean(4);
						List<GrantedAuthority> authorities = (isAdmin)
								? AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER")
								: AuthorityUtils.createAuthorityList("ROLE_USER");
						return new User(username, password, enabled, true, true, true, authorities);
					}

				});
	}

	
	/**
	 * we have to override this, as we already determine the ROLE_USER and ROLE_ADMIN while loading
	 * the user list.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetails> users = loadUsersByUsername(username);

		if (users.size() == 0) {
			this.logger.debug("Query returned no results for user '" + username + "'");

			throw new UsernameNotFoundException(this.messages.getMessage("JdbcDaoImpl.notFound",
					new Object[] { username }, "Username {0} not found"));
		}

		UserDetails user = users.get(0);

		Set<GrantedAuthority> dbAuthsSet = new HashSet<GrantedAuthority>();
		dbAuthsSet.addAll(user.getAuthorities());

		if (getEnableAuthorities()) {
			dbAuthsSet.addAll(loadUserAuthorities(user.getUsername()));
		}

		if (getEnableGroups()) {
			dbAuthsSet.addAll(loadGroupAuthorities(user.getUsername()));
		}

		List<GrantedAuthority> dbAuths = new ArrayList<GrantedAuthority>(dbAuthsSet);

		addCustomAuthorities(user.getUsername(), dbAuths);

		if (dbAuths.size() == 0) {
			this.logger.debug("User '" + username + "' has no authorities and will be treated as 'not found'");

			throw new UsernameNotFoundException(this.messages.getMessage("JdbcDaoImpl.noAuthority",
					new Object[] { username }, "User {0} has no GrantedAuthority"));
		}

		return createUserDetails(username, user, dbAuths);
	}

	@Override
	public UserInfo getByUsername(String username) {
		return getJdbcTemplate().query(getKontaktByUsernameQuery, new String[] { username },
				new ResultSetExtractor<UserInfo>() {

					@Override
					public UserInfo extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							UserInfo ui = new DefaultUserInfo();
							String userId = rs.getString(1);
							String familyName = rs.getString(2);
							String givenName = rs.getString(3);
							String sex = rs.getString(4);
							String email = rs.getString(5);

							ui.setPreferredUsername(userId);
							ui.setSub(userId);

							ui.setFamilyName(familyName);
							ui.setGivenName(givenName);
							if (!StringUtils.isNullOrEmpty(sex)) {
								ui.setGender(sex);
							}
							if (!StringUtils.isNullOrEmpty(email)) {
								ui.setEmail(email);
								ui.setEmailVerified(false);
							}
							return ui;
						}

						logger.warn("Could not getByUsername [{}]", username);
						return null;
					}
				});
	}

	@Override
	public UserInfo getByEmailAddress(String email) {
		logger.error("Not implemented!");
		return null;
	}
}
