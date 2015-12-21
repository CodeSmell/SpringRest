package codesmell.hello.dao;

import java.util.List;

import codesmell.hello.service.Hello;

/**
 * 
 * simple interface for data access
 *
 */
public interface HelloDao {
	public void insertHello(Hello h);
	public Hello selectHello(String helloId);
	public List<Hello> selectHelloList(String dateMMDDYYYY);
}
