package dev.jsampietri.jsonParser.analyser;

import dev.jsampietri.jsonParser.exception.EOSException;
import dev.jsampietri.jsonParser.exception.MalformedJSONException;
import dev.jsampietri.jsonParser.util.StringReader;
import dev.jsampietri.jsonParser.util.Token;
import dev.jsampietri.jsonParser.util.TokenType;

public class LexicAnalyser {
	private StringReader reader;
	private StringBuilder lexeme;
	private Token t;
	private boolean eos = false;

	public LexicAnalyser(String strJson) {
		this.reader = new StringReader(strJson);
	}

	public Token getNextToken() throws MalformedJSONException{
		this.t = new Token();
		this.eos = false;
		while (!this.eos) {
			this.lexeme = new StringBuilder();
			char c;

			try {
				c = this.reader.getNextChar();
			} catch (EOSException eosEx) {
				return validateEOS();
			}

			this.lexeme.append(c);
			this.t.setInitialPosition(this.reader.getCurrentPosition());

			if (Character.isWhitespace(c)) {
				this.t.setTokenType(TokenType.WHITESPACE);
				while (Character.isWhitespace(c)) {
					try {
						c = this.reader.getNextChar();
					} catch (EOSException eosEx) {
						return validateEOS();
					}
				}
				this.reader.resetLastChar();
				this.t.setLexeme(this.lexeme.toString());
				eos = true;
			} else if (c == '-' || Character.isDigit(c)) {
				try {
					readNumber();
					eos = true;
				} catch (EOSException eosEx) {
					return validateEOS();
				}
			} else {
				switch (c) {
				case '"':
					try {
						readString();
						eos = true;
					} catch (EOSException eosEx) {
						return validateEOS();
					}
					break;
				case '[':
					this.t.setLexeme(this.lexeme.toString());
					this.t.setTokenType(TokenType.L_SQUARE_BRAC);
					eos = true;
					break;
				case ']':
					this.t.setLexeme(this.lexeme.toString());
					this.t.setTokenType(TokenType.R_SQUARE_BRAC);
					eos = true;
					break;
				case '{':
					this.t.setLexeme(this.lexeme.toString());
					this.t.setTokenType(TokenType.L_CURLY_BRAC);
					eos = true;
					break;
				case '}':
					this.t.setLexeme(this.lexeme.toString());
					this.t.setTokenType(TokenType.R_CURLY_BRAC);
					eos = true;
					break;
				case ':':
					this.t.setLexeme(this.lexeme.toString());
					this.t.setTokenType(TokenType.COLON);
					eos = true;
					break;
				case ',':
					this.t.setLexeme(this.lexeme.toString());
					this.t.setTokenType(TokenType.COMMA);
					eos = true;
					break;
				default:
					try {
						readLogicalOperator(c);
						eos = true;
					} catch (EOSException eosEx) {
						return validateEOS();
					}
				}
			}
		}
		return t;
	}

	private void readNumber() throws EOSException {
		boolean readNext = true;
		this.t.setTokenType(TokenType.NUMBER);
		char c;
		do {
			c = this.reader.getNextChar();
			if (Character.isDigit(c)) {
				this.lexeme.append(c);
			} else if (c == '.' || c == 'E' || c == 'e' || c == '-' || c == '+') {
				this.t.setTokenType(TokenType.DECIMAL);
				this.lexeme.append(c);
			} else {
				this.reader.resetLastChar();
				readNext = false;
			}
		} while (readNext);
		this.t.setLexeme(lexeme.toString());
	}

	private void readString() throws EOSException {
		boolean readNext = true;
		this.t.setTokenType(TokenType.STRING);
		char c;
		do {
			c = this.reader.getNextChar();
			if (c == '\\') {
				this.lexeme.append(c);
				c = this.reader.getNextChar();
				this.lexeme.append(c);
			} else if (c != '"') {
				this.lexeme.append(c);
			} else {
				this.lexeme.append(c);
				readNext = false;
			}
		} while (readNext);
		this.t.setLexeme(lexeme.substring(1, this.lexeme.length() - 1));
	}
	
	private void readLogicalOperator(char first) throws EOSException, MalformedJSONException {
		if (first == 't') {
			char c = this.reader.getNextChar();
			if (c != 'r') {
				throw new MalformedJSONException();
			} else {
				this.lexeme.append(c);
			}
			c = this.reader.getNextChar();
			if (c != 'u') {
				throw new MalformedJSONException();
			} else {
				this.lexeme.append(c);
			}
			c = this.reader.getNextChar();
			if (c != 'e') {
				throw new MalformedJSONException();
			} else {
				this.lexeme.append(c);
			}
			this.t.setTokenType(TokenType.BOOLEAN);
		} else if (first == 'f') {
			char c = this.reader.getNextChar();
			if (c != 'a') {
				throw new MalformedJSONException();
			} else {
				this.lexeme.append(c);
			}
			c = this.reader.getNextChar();
			if (c != 'l') {
				throw new MalformedJSONException();
			} else {
				this.lexeme.append(c);
			}
			c = this.reader.getNextChar();
			if (c != 's') {
				throw new MalformedJSONException();
			} else {
				this.lexeme.append(c);
			}
			c = this.reader.getNextChar();
			if (c != 'e') {
				throw new MalformedJSONException();
			} else {
				this.lexeme.append(c);
			}
			this.t.setTokenType(TokenType.BOOLEAN);
		} else if (first == 'n') {
			char c = this.reader.getNextChar();
			if (c != 'u') {
				throw new MalformedJSONException();
			} else {
				this.lexeme.append(c);
			}
			c = this.reader.getNextChar();
			if (c != 'l') {
				throw new MalformedJSONException();
			} else {
				this.lexeme.append(c);
			}
			c = this.reader.getNextChar();
			if (c != 'l') {
				throw new MalformedJSONException();
			} else {
				this.lexeme.append(c);
			}
			this.t.setTokenType(TokenType.NULL);
		} else {
			throw new MalformedJSONException();
		}
		this.t.setLexeme(lexeme.toString());
	}

	private Token validateEOS()
	{
		if (this.lexeme.length() > 0)
		{
			this.t.setLexeme(this.lexeme.toString());
			return this.t;
		} else {
			return new Token(TokenType.EOS, "");
		}
	}
	public void returnToToken(Token t)
	{
		this.reader.returnToPosition(t.getInitialPosition() - 1);
	}
}
