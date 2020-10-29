package dev.jsampietri.jsonParser.exception;

public class JSONObjectException extends Exception{

	private static final long serialVersionUID = 3315776708632892118L;
	
	private String message;

	public JSONObjectException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	};
	
}
