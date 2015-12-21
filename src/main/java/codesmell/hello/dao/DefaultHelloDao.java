package codesmell.hello.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import codesmell.hello.service.Hello;
import codesmell.util.ReflectionUtil;
/**
 * 
 * simulate DB 
 *
 */
@Component
public class DefaultHelloDao implements HelloDao{

	@Override
	public void insertHello(Hello h) {
		// most DB frameworks will provide the generated PK id
		ReflectionUtil.doSet(h, "helloId", String.class, UUID.randomUUID().toString());
	}

	@Override
	public Hello selectHello(String helloId) {
		return new Hello(helloId, "Hello World");
	}

	@Override
	public List<Hello> selectHelloList(String dateMMDDYYYY) {
		List<Hello> list = new ArrayList<Hello>();
		list.add(new Hello("1", "Hello 1"));
		list.add(new Hello("2", "Hello 2"));
		return list;
	}

}
