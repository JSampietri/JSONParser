package dev.lab;

public class JSONParser {
	private Token t;
	private boolean eos;
	private LexicAnalyser la;
	private JSONObject jsonObj;
	public void parse(String jsonString) throws MalformedJSONException {
		this.la = new LexicAnalyser(jsonString);
		this.eos = false;

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
		
	}

	public void readObject(JSONObject jsonObj) throws MalformedJSONException {
		boolean readNext = true;
		getNextNonWSToken();
		if (this.t.getTokenType() == TokenType.R_CURLY_BRAC) {
			readNext = false;
		} else {
			this.la.returnToToken(this.t);
		}
		while (readNext) {
			getNextNonWSToken();
			if (this.t.getTokenType() != TokenType.STRING) {
				throw new MalformedJSONException();
			} else {
				jsonObj.key = this.t.getLexeme();
			}
			getNextNonWSToken();
			if (this.t.getTokenType() != TokenType.COLON) {
				throw new MalformedJSONException();
			}

			readValue(jsonObj);

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

	public void readArray(JSONObject jsonObj) throws MalformedJSONException {
		boolean readNext = true;
		getNextNonWSToken();
		if (this.t.getTokenType() == TokenType.R_SQUARE_BRAC) {
			readNext = false;
		} else {
			this.la.returnToToken(this.t);
		}
		while (readNext) {
			readValue(jsonObj);
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

	public void readValue(JSONObject jsonObj) throws MalformedJSONException{
		getNextNonWSToken();
		switch (this.t.getTokenType()) {
		case STRING:
			break;
		case NUMBER:
			break;
		case L_CURLY_BRAC:
			readObject(jsonObj);
			break;
		case L_SQUARE_BRAC:
			readArray(jsonObj);
			break;
		case LOGICAL_OP:
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
		System.out.print(this.t.getLexeme());
	}

}
