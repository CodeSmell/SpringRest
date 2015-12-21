package codesmell.user.rest;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import test.spring.security.SpringSecUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:TestAppContext.xml"})
@WebAppConfiguration
public class UserControllerTest {

	@Autowired
	WebApplicationContext wac;
	
	MockMvc mockMvc;

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	public void test_user_auth_false() {
		try {

			mockMvc.perform(MockMvcRequestBuilders.get("/user"))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.loggedIn").value(false));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void test_user_auth_true() {
		try {

			MockHttpSession session = SpringSecUtil.getAuthenticationSession();

			mockMvc.perform(MockMvcRequestBuilders.get("/user").session(session))
					.andDo(MockMvcResultHandlers.print())
					.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("mikeb"))
					.andExpect(MockMvcResultMatchers.jsonPath("$.loggedIn").value(true));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	

}
