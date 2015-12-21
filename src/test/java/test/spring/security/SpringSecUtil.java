package test.spring.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

public class SpringSecUtil {
	
	public static MockHttpSession getAuthenticationSession(){
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		authList.add(new MockGrantedAuthority());
		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("mikeb", "test", authList);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, new MockSecurityContext(principal));

		return session;
	}
}
