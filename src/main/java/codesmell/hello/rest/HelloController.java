package codesmell.hello.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import codesmell.hello.service.Hello;
import codesmell.hello.service.HelloService;
import codesmell.rest.error.NoDataFoundException;
/**
 * 
 * REST API
 *
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

	@Autowired
	HelloService svc;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> testAvailability() {
		return new ResponseEntity<String>("services are available...", HttpStatus.OK);
	}
	
	/**
	 * @param helloObj
	 * @return the id
	 */
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Hello newHello(@RequestBody Hello helloObj) {
		svc.addHello(helloObj);

		return helloObj;
	}
	
	/**
	 * @param helloId
	 * @return the {@link Hello} data
	 */
	@RequestMapping(value = "{helloId}", method = RequestMethod.GET)
	@ResponseBody
	public Hello getHello(@PathVariable(value = "helloId") String helloId) {
		Hello helloObj = null;
		
		helloObj = svc.retrieveHello(helloId);
		
		if (helloObj==null){
			throw new NoDataFoundException(helloId);
		}
		
		return helloObj;
	}
	
	@RequestMapping(value = "all", method = RequestMethod.GET)
	@ResponseBody
	public List<Hello> getHelloList(@RequestParam(value="date", required=false) String dateMMDDYYYY) {
		
		List<Hello> list = null;
			
		list = svc.retrieveHelloList(dateMMDDYYYY);
		
		if ((list==null)||(list.size()==0)){
			throw new NoDataFoundException(dateMMDDYYYY);
		}
		
		return list;
	}
	
}
