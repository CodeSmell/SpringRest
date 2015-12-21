package codesmell.hello.service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hello {
	@JsonProperty("id")
	private final String helloId;
	@JsonProperty("name")
	private final String helloName;
	
	public Hello(@JsonProperty("id")String id, @JsonProperty("name")String name){
		this.helloId = id;
		this.helloName = name;
	}
	
	public String getHelloId() {
		return helloId;
	}
	public String getHelloName() {
		return helloName;
	}
	
	
}
