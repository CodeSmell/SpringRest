package test.spring.security;

import org.springframework.security.core.GrantedAuthority;

@SuppressWarnings("serial")
public class MockGrantedAuthority implements GrantedAuthority {
	@Override
	public String getAuthority() {
		return "TEST";
	}
}

