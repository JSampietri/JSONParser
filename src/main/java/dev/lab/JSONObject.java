package dev.lab;

import java.util.HashMap;

public class JSONObject {
	private ObjectType type;
	private HashMap <String, Object> value;
	
	public enum ObjectType {
		OBJECT,
		ARRAY,
		VALUE
	}
}
