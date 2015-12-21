package codesmell.hello.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import codesmell.hello.dao.HelloDao;
import codesmell.rest.error.InvalidException;

@Service(value="helloService")
public class DefaultHelloService implements HelloService{

	@Autowired
	HelloDao dao;
	
	@Override
	public void addHello(Hello h) {
		if (this.validateHello(h)){
			dao.insertHello(h);
		}
		else {
			throw new InvalidException("Hello is invalid");
		}
	}

	@Override
	public Hello retrieveHello(String helloIdParam) {
		Hello h = null;
		if (!StringUtils.isEmpty(helloIdParam)){
			h = dao.selectHello(helloIdParam);	
		}
		else {
			throw new InvalidException("hello id is required");
		}
		return h;
		
	}
	
	@Override
	public List<Hello> retrieveHelloList(String dateMMDDYYYY) {
		List<Hello> list = null;
		if (this.validateDate(dateMMDDYYYY)){
			list = dao.selectHelloList(dateMMDDYYYY);	
		}
		else {
			throw new InvalidException("date is required and should be formatted as MMDDYYYY");
		}
		return list;
	}
	
	protected boolean validateHello(Hello h){
		boolean isValid = false;
		
		if (h!=null){
			if (!StringUtils.isEmpty(h.getHelloName())){
				isValid = true;
			}
		}
		
		return isValid;
	}

	protected boolean validateDate(String dateMMDDYYYY){
		boolean isValid = false;
		try {
			if (!StringUtils.isEmpty(dateMMDDYYYY)){
				SimpleDateFormat sdf = new SimpleDateFormat("MMddYYYY");
				if (sdf.parse(dateMMDDYYYY)!=null){
					isValid = true;
				}
			}
		}
		catch (ParseException e) {
			isValid=false;
		}
		return isValid;
	}

}
