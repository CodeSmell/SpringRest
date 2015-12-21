package codesmell.rest.error;

public class NoDataFoundException extends RuntimeException {
	public NoDataFoundException(String msg){
		super(msg);
	}
}
