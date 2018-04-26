package info.elexis.security.authentication.dao;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import info.elexis.security.core.userdetails.OtpUser;
import info.elexis.security.web.authentication.OtpWebAuthenticationDetails;

public class OtpDaoAuthenticationProvider extends DaoAuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// one-time-password, currently a TOTP, but may also be a HOTP in the future
		String otpToken = ((OtpWebAuthenticationDetails) authentication.getDetails()).getOtpToken();

		// Determine username
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();

		OtpUser user = (OtpUser) getUserDetailsService().loadUserByUsername(username);
		if (user == null) {
			logger.debug("User '" + username + "' not found");

			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}

		Totp totp = new Totp(user.getTotpSecret());
		if (!isValidLong(otpToken) || !totp.verify(otpToken)) {
			throw new BadCredentialsException(
					messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}

		return super.authenticate(authentication);
	}

	private boolean isValidLong(String code) {
		try {
			Long.parseLong(code);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
