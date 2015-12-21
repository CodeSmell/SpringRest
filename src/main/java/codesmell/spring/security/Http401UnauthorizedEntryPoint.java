package codesmell.spring.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {
	private boolean sendError = false;
	
	/**
	 * Always returns a 401 error code to the client.
	 * modeled after {@link Http403ForbiddenEntryPoint}
	 */
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ae) 
			throws IOException, ServletException {

		if (sendError) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
		}
		else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

}