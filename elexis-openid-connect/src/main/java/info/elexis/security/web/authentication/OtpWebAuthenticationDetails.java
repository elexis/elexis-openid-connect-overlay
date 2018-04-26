package info.elexis.security.web.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class OtpWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = -8103480893368404888L;

	private String otpToken;

	public OtpWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		otpToken = request.getParameter("otp_token");
	}

	public String getOtpToken() {
		return otpToken;
	}
}
