package info.elexis.security.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import info.elexis.security.web.authentication.OtpWebAuthenticationDetails;

@Component
public class OtpWebAuthenticationDetailsSource
		implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

	@Override
	public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
		return new OtpWebAuthenticationDetails(context);
	}

}
