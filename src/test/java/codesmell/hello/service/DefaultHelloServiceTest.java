package codesmell.hello.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import codesmell.hello.dao.HelloDao;
import codesmell.rest.error.InvalidException;
import codesmell.util.ReflectionUtil;

public class DefaultHelloServiceTest {

	HelloService svc;
	
	@Mock
	HelloDao mockDao;
	
	@Before
	public void init(){
		mockDao = Mockito.mock(HelloDao.class);
		
		svc = new DefaultHelloService();
		ReflectionUtil.doInjectWithNoContainer(svc, HelloDao.class, mockDao);
		
		Mockito.reset(mockDao);
	}

	@Test
	public void test_get_hello_exception() {
		String helloId = "2112";
		
		try {
			
			Mockito.when(mockDao.selectHello(helloId)).thenThrow(new RuntimeException("testing"));
			
			svc.retrieveHello(helloId);
			fail("expected RuntimeException");
		}
		catch(RuntimeException e){
			assertEquals("testing", e.getMessage());
		}
	}
	
	
	@Test
	public void test_get_hello_null() {
		String helloId = null;
		
		try {
			svc.retrieveHello(helloId);
			fail("expected InvalidException");
		}
		catch(InvalidException e){
			assertEquals("hello id is required", e.getMessage());
		}
	}
	
	@Test
	public void test_get_hello_empty() {
		String helloId = "";
		
		try {
			svc.retrieveHello(helloId);
			fail("expected InvalidException");
		}
		catch(InvalidException e){
			assertEquals("hello id is required", e.getMessage());
		}
	}
	
	@Test
	public void test_get_hello_valid() {
		String helloId = "2112";
		
		Mockito.when(mockDao.selectHello(helloId)).thenReturn(new Hello(helloId, "mike"));
		
		Hello hello = svc.retrieveHello(helloId);
		assertNotNull(hello);
		assertEquals("2112", hello.getHelloId());
		assertEquals("mike", hello.getHelloName());
		
		Mockito.reset(mockDao);
	}
	
	
	@Test
	public void test_add_hello_exception() {
		try {
			Hello hello = new Hello(null, "mike");

			// mock the insert
			Answer<Hello> a = new Answer<Hello>() {
				@Override
				public Hello answer(InvocationOnMock invocation) throws Throwable {
					throw new RuntimeException("testing");
				}
			};

			Mockito.doAnswer(a).when(mockDao).insertHello(hello);

			svc.addHello(hello);
			fail("expected RuntimeException");
		}
		catch (RuntimeException e) {
			assertEquals("testing", e.getMessage());
		}

	}
	
	@Test
	public void test_getList_hello_exception() {
		String date = "12172015";
		
		try {
			
			Mockito.when(mockDao.selectHelloList(date)).thenThrow(new RuntimeException("testing"));
			
			svc.retrieveHelloList(date);
			fail("expected RuntimeException");
		}
		catch(RuntimeException e){
			assertEquals("testing", e.getMessage());
		}
	}
	
	@Test
	public void test_getList_hello_null_params() {
		String date = null;
		
		try {
			svc.retrieveHelloList(date);
			fail("expected InvalidException");
		}
		catch(InvalidException e){
			assertEquals("date is required and should be formatted as MMDDYYYY", e.getMessage());
		}
	}
	
	@Test
	public void test_getList_hello_empty_params() {
		String date = null;
		
		try {
			svc.retrieveHelloList(date);
			fail("expected InvalidException");
		}
		catch(InvalidException e){
			assertEquals("date is required and should be formatted as MMDDYYYY", e.getMessage());
		}
	}
	
	@Test
	public void test_getList_hello_date_X() {
		String date = "X";
		
		try {
			svc.retrieveHelloList(date);
			fail("expected InvalidException");
		}
		catch(InvalidException e){
			assertEquals("date is required and should be formatted as MMDDYYYY", e.getMessage());
		}
	}
	
	@Test
	public void test_getList_hello_not_found_nullList() {
		String date = "12042015";
		
		Mockito.when(mockDao.selectHelloList(date)).thenReturn(null);
			
		List<Hello> list = svc.retrieveHelloList(date);
		assertNull(list);
	}
	
	@Test
	public void test_getList_hello_not_found_emptyList() {
		String date = "12042015";
		
		Mockito.when(mockDao.selectHelloList(date)).thenReturn(new ArrayList<Hello>());

		List<Hello> list = svc.retrieveHelloList(date);

		assertNotNull(list);
		assertEquals(0, list.size());
	}
	
	@Test
	public void test_getList_hello_valid() {
		String date = "12042015";
		
		Mockito.when(mockDao.selectHelloList(date)).thenReturn(this.getHelloList());

		List<Hello> list = svc.retrieveHelloList(date);
		assertNotNull(list);
		assertEquals(2, list.size());
		
		assertNotNull(list.get(0));
		assertEquals("feedback loops", list.get(0).getHelloName());
		
		assertNotNull(list.get(1));
		assertEquals("write clean code", list.get(1).getHelloName());

		Mockito.reset(mockDao);
	}

	@Test
	public void test_add_Hello_null() {
		try{
			Hello hello = null;
			svc.addHello(hello);
			fail("expected InvalidException");
		}
		catch(InvalidException e){
			assertEquals("Hello is invalid",e.getMessage());
		}
	}
	
	@Test
	public void test_add_Hello_empty() {
		try{
			Hello hello = new Hello(null,null);
			svc.addHello(hello);
			fail("expected InvalidException");
		}
		catch(InvalidException e){
			assertEquals("Hello is invalid",e.getMessage());
		}
	}
	
	@Test
	public void test_add_Hello_valid() {
		Hello helloParam = new Hello(null, "mike");
		
		// mock the insert
		Answer<Hello> a = new Answer<Hello>() {
			@Override
			public Hello answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();

				Hello hello = (Hello) args[0];
				ReflectionUtil.doSet(hello, "helloId", String.class, "5150");
				return hello;
			}
		};

		Mockito.doAnswer(a).when(mockDao).insertHello(helloParam);

		svc.addHello(helloParam);
		assertNotNull(helloParam.getHelloId());
		assertEquals("5150", helloParam.getHelloId());
		assertEquals("mike", helloParam.getHelloName());

	}

	@Test
	public void test_get_hello_reflect() throws Exception{
		String helloId = "2112";
		
		Mockito.when(mockDao.selectHello(helloId)).thenReturn(new Hello(helloId, "mike"));
		
		// just testing Reflection Utils
		Hello hello = ReflectionUtil.callMethod(svc, "retrieveHello", new Class[]{String.class}, new Object[]{helloId}, Hello.class);
		
		assertNotNull(hello);
		assertEquals("2112", hello.getHelloId());
		assertEquals("mike", hello.getHelloName());
		
		Mockito.reset(mockDao);
	}
	
	public static List<Hello> getHelloList(){
		List<Hello> list = new ArrayList<Hello>();
		list.add(new Hello("1", "feedback loops"));
		list.add(new Hello("2", "write clean code"));
		return list;
	}
	
}
