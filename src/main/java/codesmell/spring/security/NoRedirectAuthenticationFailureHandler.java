package codesmell.spring.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class NoRedirectAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	private boolean sendError = false;
	/**
	 * don't redirect 
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {

		if (sendError) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
		}
		else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
