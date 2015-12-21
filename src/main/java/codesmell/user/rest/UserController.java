package codesmell.user.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
	@RequestMapping("/user")
	@ResponseBody
	public Map<String, Object> getUser(Authentication auth) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		
		if (auth != null && auth.isAuthenticated()) {
			jsonMap.put("loggedIn", true);

			String userName = this.getUserName(auth);
			jsonMap.put("userName", userName);
		} 
		else {
			jsonMap.put("loggedIn", false);
		}

		return jsonMap;
	}
	
	protected String getUserName(Authentication auth){
		String userName = null;
		if (auth!=null){
			Object principleObj = auth.getPrincipal();
			if (principleObj != null){
				if (principleObj instanceof UserDetails){
					UserDetails details = (UserDetails) auth.getPrincipal();
					userName = details.getUsername();
				}
				else if (principleObj instanceof String){
					userName = (String) principleObj;
				}
			}
		}
		return userName;
	}
}
