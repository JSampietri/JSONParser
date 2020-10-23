package dev.lab;
public class Token {
	private TokenType tokenType;
	private String lexeme;
	private int initialPosition;
	
	public Token() {

	}
	public Token(TokenType tokenType, String lexeme) {
		this.tokenType = tokenType;
		this.lexeme = lexeme;
		
	}
	public Token(TokenType tokenType, String lexeme, int initialPosition) {
		this.tokenType = tokenType;
		this.lexeme = lexeme;
		this.initialPosition = initialPosition;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public void printToken() {
		System.out.println("(" + this.initialPosition + ")\t\t|" + this.tokenType + "\t\t|" + this.lexeme );
	}
	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}
	public String getLexeme() {
		return lexeme;
	}
	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}
	public int getInitialPosition() {
		return initialPosition;
	}
	public void setInitialPosition(int initialPosition) {
		this.initialPosition = initialPosition;
	}
}