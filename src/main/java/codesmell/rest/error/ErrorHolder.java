package codesmell.rest.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorHolder {
	@JsonProperty(value = "error")
	private final String error;

	public ErrorHolder(String errorText) {
		this.error = errorText;
	}
}
