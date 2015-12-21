package codesmell.rest.error;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
	private static final Logger log = Logger.getLogger(ExceptionHandlerControllerAdvice.class);
	
    @ExceptionHandler(value = MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorHolder> handleMultipartException(Exception e)
    {
    	log.error(e.getMessage(),e);
    	return this.createErrorResponse("there were problems with the multipart request");
    }

	@ExceptionHandler(value=HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
	public List<ErrorHolder> invalidJson() {
		return this.createErrorResponse("missing or invalid JSON");
	}
	
    @ExceptionHandler(value = NoDataFoundException.class)
    @ResponseStatus(HttpStatus.GONE)
    @ResponseBody
    public List<ErrorHolder> handleNoDataFoundException(Exception e)
    {
    	return this.createErrorResponse("no data found");
    }
    
    @ExceptionHandler(value = InvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<ErrorHolder> handleInvalidParametersException(InvalidException e)
    {
    	return this.createErrorResponse(e.getErrorList());
    }
    
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public List<ErrorHolder> handleException(Exception e)
    {
    	log.error(e.getMessage(),e);
    	return this.createErrorResponse(e);
    }
    
    protected List<ErrorHolder> createErrorResponse(Exception e) {
		String errText = "We can't process the request right now";
		List<String> errList = new ArrayList<String>();
		errList.add(errText);
		return createErrorResponse(errList);
	}

	protected List<ErrorHolder> createErrorResponse(String errText) {
		List<String> errList = new ArrayList<String>();
		errList.add(errText);
		return createErrorResponse(errList);
	}

	protected List<ErrorHolder> createErrorResponse(List<String> errList) {
		List<ErrorHolder> errHolderList = new ArrayList<ErrorHolder>();

		if (errList != null) {
			for (String errText : errList) {
				errHolderList.add(new ErrorHolder(errText));
			}
		}

		return errHolderList;
	}
	
}