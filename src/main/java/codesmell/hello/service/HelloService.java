package codesmell.hello.service;

import java.util.List;

public interface HelloService {
	public void addHello(Hello h);
	public Hello retrieveHello(String helloIdParam);
	public List<Hello> retrieveHelloList(String dateMMDDYYYY);
}
