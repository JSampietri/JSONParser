package dev.jsampietri.jsonParser;

import java.util.ArrayList;
import java.util.HashMap;

import dev.jsampietri.jsonParser.analyser.LexicAnalyser;
import dev.jsampietri.jsonParser.exception.JSONObjectException;
import dev.jsampietri.jsonParser.exception.MalformedJSONException;
import dev.jsampietri.jsonParser.util.Token;
import dev.jsampietri.jsonParser.util.TokenType;

public class JSONParser {
	private Token t;
	private LexicAnalyser la;
	private JSONObject jsonObj;
	public JSONObject parse(String jsonString) throws MalformedJSONException, JSONObjectException {
		this.la = new LexicAnalyser(jsonString);
		jsonObj = new JSONObject();
		getNextNonWSToken();

		switch (this.t.getTokenType()) {
		case L_CURLY_BRAC:
			readObject(jsonObj);
			break;
		case L_SQUARE_BRAC:
			readArray(jsonObj);
			break;
		default:
			throw new MalformedJSONException();
		}
		
		return jsonObj;
		
	}

	public void readObject(JSONObject jsonObj) throws MalformedJSONException, JSONObjectException {
		boolean readNext = true;
		getNextNonWSToken();
		if (this.t.getTokenType() == TokenType.R_CURLY_BRAC) {
			readNext = false;
		} else {
			this.la.returnToToken(this.t);
		}
		jsonObj.setType(JSONObjectType.OBJECT);
		jsonObj.setValue(new HashMap<String, JSONObject>());
		while (readNext) {
			getNextNonWSToken();
			if (this.t.getTokenType() != TokenType.STRING) {
				throw new MalformedJSONException();
			} 
			String key = this.t.getLexeme().toString();
			getNextNonWSToken();
			if (this.t.getTokenType() != TokenType.COLON) {
				throw new MalformedJSONException();
			}

			JSONObject value = new JSONObject();
			readValue(value); 
			jsonObj.getMapValues().put(key, value);
			
			getNextNonWSToken();
			if (this.t.getTokenType() == TokenType.COMMA) {
				readNext = true;
			} else if (this.t.getTokenType() == TokenType.R_CURLY_BRAC) {
				readNext = false;
			} else {
				throw new MalformedJSONException();
			}
		}
	}

	public void readArray(JSONObject jsonObj) throws MalformedJSONException, JSONObjectException {
		boolean readNext = true;
		getNextNonWSToken();
		if (this.t.getTokenType() == TokenType.R_SQUARE_BRAC) {
			readNext = false;
		} else {
			this.la.returnToToken(this.t);
		}
		
		jsonObj.setType(JSONObjectType.ARRAY);
		jsonObj.setValue(new ArrayList<JSONObject>());
		while (readNext) {
			
			JSONObject value = new JSONObject();
			readValue(value); 
			jsonObj.getArray().add(value);
			
			getNextNonWSToken();
			if (this.t.getTokenType() == TokenType.COMMA) {
				readNext = true;
			} else if (this.t.getTokenType() == TokenType.R_SQUARE_BRAC) {
				readNext = false;
			} else {
				throw new MalformedJSONException();
			}
		}
	}

	public void readValue(JSONObject jsonObj) throws MalformedJSONException, JSONObjectException{
		getNextNonWSToken();
		switch (this.t.getTokenType()) {
		case STRING:
			jsonObj.setType(JSONObjectType.STRING);
			jsonObj.setValue(this.t.getLexeme().toString());
			break;
		case DECIMAL:
			jsonObj.setType(JSONObjectType.DOUBLE);
			jsonObj.setValue(Double.parseDouble(this.t.getLexeme().toString()));
			break;
		case NUMBER:
			jsonObj.setType(JSONObjectType.LONG);
			jsonObj.setValue(Long.parseLong(this.t.getLexeme().toString()));
			break;
		case L_CURLY_BRAC:
			readObject(jsonObj);
			break;
		case L_SQUARE_BRAC:
			readArray(jsonObj);
			break;
		case BOOLEAN:
			jsonObj.setType(JSONObjectType.BOOLEAN);
			jsonObj.setValue(Boolean.parseBoolean(this.t.getLexeme().toString()));
			break;
		case NULL:
			jsonObj.setType(JSONObjectType.NULL);
			break;
		default:
			throw new MalformedJSONException();
		}
	}

	public void getNextNonWSToken() throws MalformedJSONException {
		this.t = this.la.getNextToken();
		while (this.t.getTokenType() == TokenType.WHITESPACE) {
			this.t = this.la.getNextToken();
		}
	}

}
