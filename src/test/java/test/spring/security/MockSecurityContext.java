package test.spring.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

@SuppressWarnings("serial")
public class MockSecurityContext implements SecurityContext {
	private Authentication authentication;

	public MockSecurityContext(Authentication authentication) {
		this.authentication = authentication;
	}

	@Override
	public Authentication getAuthentication() {
		return this.authentication;
	}

	@Override
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
}