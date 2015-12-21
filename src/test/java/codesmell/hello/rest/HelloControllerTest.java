package codesmell.hello.rest;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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

import codesmell.hello.service.DefaultHelloServiceTest;
import codesmell.hello.service.Hello;
import codesmell.hello.service.HelloService;
import codesmell.rest.error.InvalidException;
import codesmell.util.ReflectionUtil;
import test.spring.security.SpringSecUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:TestAppContext.xml" })
@WebAppConfiguration
public class HelloControllerTest {

	public static final String REST_HELLO = "/hello";
	public static final String REST_HELLO_LIST = "/hello/all";
	
	public static final String PARAM_DATE = "date";

	@Autowired
	WebApplicationContext wac;

	MockMvc mockMvc;

	@Autowired
	HelloService mockSvc;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(SecurityMockMvcConfigurers.springSecurity()).build();
		
		Mockito.reset(mockSvc);
	}
	
	@Test
	public void test_get_hello_exception() throws Exception {
		String helloId = "2112";

		MockHttpSession session = SpringSecUtil.getAuthenticationSession();
		Mockito.when(mockSvc.retrieveHello(helloId)).thenThrow(new RuntimeException("testing"));

		mockMvc.perform(MockMvcRequestBuilders.get(REST_HELLO + "/" + helloId).session(session))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].error").value("We can't process the request right now"));
	}
	
	@Test
	public void test_get_hello_not_found() throws Exception {
		String helloId = "2112";

		MockHttpSession session = SpringSecUtil.getAuthenticationSession();
		Mockito.when(mockSvc.retrieveHello(helloId)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get(REST_HELLO + "/" + helloId).session(session))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isGone())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].error").value("no data found"));
	}

	@Test
	public void test_get_hello() throws Exception {
		String helloId = "2112";

		MockHttpSession session = SpringSecUtil.getAuthenticationSession();
		Mockito.when(mockSvc.retrieveHello(helloId)).thenReturn(new Hello(helloId, "mike"));

		mockMvc.perform(MockMvcRequestBuilders.get(REST_HELLO + "/" + helloId).session(session))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("2112"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("mike"));
	}
	
	@Test
	public void test_getList_hello_exception() throws Exception {
		String date = "12182015";
		
		MockHttpSession session = SpringSecUtil.getAuthenticationSession();
		Mockito.when(mockSvc.retrieveHelloList(date)).thenThrow(new RuntimeException("testing"));

		mockMvc.perform(MockMvcRequestBuilders.get(REST_HELLO_LIST)
					.param(PARAM_DATE, date)
					.session(session))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].error").value("We can't process the request right now"));
	}
	
	@Test
	public void test_getList_hello_no_param() throws Exception {
		MockHttpSession session = SpringSecUtil.getAuthenticationSession();
		Mockito.when(mockSvc.retrieveHelloList(null)).thenThrow(new InvalidException("testing"));
		
		mockMvc.perform(MockMvcRequestBuilders.get(REST_HELLO_LIST)
				.session(session))
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].error").value("testing"));
	}
	
	@Test
	public void test_getList_hello_bad_param() throws Exception {
		String date = "2015";
		
		MockHttpSession session = SpringSecUtil.getAuthenticationSession();
		Mockito.when(mockSvc.retrieveHelloList(date)).thenThrow(new InvalidException("testing"));
		
		mockMvc.perform(MockMvcRequestBuilders.get(REST_HELLO_LIST)
					.param(PARAM_DATE, date)
					.session(session))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].error").value("testing"));
	}

	@Test
	public void test_getList_hello_not_found_null() throws Exception {
		String date = "12182015";
		
		MockHttpSession session = SpringSecUtil.getAuthenticationSession();
		Mockito.when(mockSvc.retrieveHelloList(date)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get(REST_HELLO_LIST)
					.param(PARAM_DATE, date)
					.session(session))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isGone())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].error").value("no data found"));
	}
	
	@Test
	public void test_getList_hello_not_found_empty() throws Exception {
		String date = "12182015";
		
		MockHttpSession session = SpringSecUtil.getAuthenticationSession();
		Mockito.when(mockSvc.retrieveHelloList(date)).thenReturn(new ArrayList<Hello>());

		mockMvc.perform(MockMvcRequestBuilders.get(REST_HELLO_LIST)
					.param(PARAM_DATE, date)
					.session(session))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isGone())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].error").value("no data found"));
	}	
	
	@Test
	public void test_getList_hello() throws Exception {
		String date = "12182015";
		
		MockHttpSession session = SpringSecUtil.getAuthenticationSession();
		Mockito.when(mockSvc.retrieveHelloList(date)).thenReturn(DefaultHelloServiceTest.getHelloList());

		mockMvc.perform(MockMvcRequestBuilders.get(REST_HELLO_LIST)
					.param(PARAM_DATE, date)
					.session(session))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("feedback loops"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("2"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("write clean code"));
	}
	
	@Test
	public void test_insert_hello_exception() throws Exception{
		String json = this.getHelloJson();

		MockHttpSession session = SpringSecUtil.getAuthenticationSession();

		// mock the insert
		Answer<Hello> a = new Answer<Hello>() {
			@Override
			public Hello answer(InvocationOnMock invocation) throws Throwable {
				throw new RuntimeException("testing");
			}
		};

		Mockito.doAnswer(a).when(mockSvc).addHello(Mockito.any(Hello.class));

		mockMvc.perform(
				MockMvcRequestBuilders.post(REST_HELLO)
					.session(session)
					.contentType(MediaType.APPLICATION_JSON)
					.content(json))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].error").value("We can't process the request right now"));
	}
	
	@Test
	public void test_insert_hello_invalid_json() throws Exception{
		String json = this.getHelloJson();
		json = json.substring(0, json.length() - 2);

		MockHttpSession session = SpringSecUtil.getAuthenticationSession();

		mockMvc.perform(
				MockMvcRequestBuilders.post(REST_HELLO)
					.session(session)
					.contentType(MediaType.APPLICATION_JSON)
					.content(json))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].error").value("missing or invalid JSON"));
	}
	
	@Test
	public void test_insert_hello_missing_json() throws Exception{
		MockHttpSession session = SpringSecUtil.getAuthenticationSession();

		mockMvc.perform(
				MockMvcRequestBuilders.post(REST_HELLO)
					.session(session)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].error").value("missing or invalid JSON"));
	}
	
	@Test
	public void test_insert_hello() throws Exception{
		String json = this.getHelloJson();

		MockHttpSession session = SpringSecUtil.getAuthenticationSession();

		// mock the insert
		Answer<Hello> a = new Answer<Hello>() {
			@Override
			public Hello answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();

				Hello hello = (Hello) args[0];
				ReflectionUtil.doSet(hello, "helloId", String.class, "42");
				return hello;
			}
		};

		Mockito.doAnswer(a).when(mockSvc).addHello(Mockito.any(Hello.class));

		mockMvc.perform(
				MockMvcRequestBuilders.post(REST_HELLO)
					.session(session)
					.contentType(MediaType.APPLICATION_JSON)
					.content(json))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("42"));
	}
	
	protected String getHelloJson(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\r\n");
		sb.append("\"id\" : \"1\"");
		sb.append(",");
		sb.append("\r\n");
		sb.append("\"name\" : \"mikeb\"");
		sb.append("\r\n");
		sb.append("}");

		System.out.println(sb.toString());

		return sb.toString();
	}
}
