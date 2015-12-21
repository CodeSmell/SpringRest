package codesmell.rest.error;

import java.util.ArrayList;
import java.util.List;

public final class InvalidException extends RuntimeException {
	
	private final List<String> errList = new ArrayList<String>();
	
	public InvalidException(){
		super();
	}
	
	public InvalidException(String msg){
		super(msg);
		if (msg!=null){
			errList.add(msg);
		}
	}

	public InvalidException(String msg, Throwable cause){
		super(msg, cause);
		if (msg!=null){
			errList.add(msg);
		}
	}
	
	public InvalidException(List<String> list){
		super(list.toString());
		errList.addAll(list);
	}
	
	public List<String> getErrorList(){
		return errList;
	}
}
