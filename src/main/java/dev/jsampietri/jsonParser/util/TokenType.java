package dev.jsampietri.jsonParser.util;
public enum TokenType {
	VALUE,
	OBJECT,
	ARRAY,
	STRING,
	NUMBER,
	DECIMAL,
	BOOLEAN,
	NULL,
	L_CURLY_BRAC,
	R_CURLY_BRAC,
	L_SQUARE_BRAC,
	R_SQUARE_BRAC,
	WHITESPACE,
	COMMA,
	COLON,
	EOS
}