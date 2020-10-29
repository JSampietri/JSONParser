package dev.jsampietri.jsonParser.util;

import dev.jsampietri.jsonParser.exception.EOSException;

public class StringReader {
	private char[] charArrJson;
	private int currentPosition;
	public StringReader(String strJson)
	{
		this.charArrJson = strJson.toCharArray();
		this.currentPosition = 0;
	}
	
	public char getNextChar() throws EOSException
	{
		if(this.currentPosition == this.charArrJson.length)
		{
			throw new EOSException();
		} else 
		{
			char nextChar = this.charArrJson[this.currentPosition];
			this.currentPosition++;
			return nextChar;
		}
	}
	
	public void resetLastChar() {
		if (this.currentPosition > 0) {
			this.currentPosition--;
		}
	}
	
	public int getCurrentPosition() {
		return this.currentPosition;
	}

	public void returnToPosition(int i) {
		this.currentPosition = i;
	}
}
