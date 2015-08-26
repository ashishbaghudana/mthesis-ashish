package org.rostlab.relna.corpus;

public class Token {
	public final int startOffset;
	public final int endOffset;
	public final String tokenText;
	private String tokenTag;
	
	public Token(int startOffset, int endOffset, String tokenText) {
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.tokenText = tokenText;
	}
	
	public Token(int startOffset, int endOffset, String tokenText, String tokenTag) {
		this(startOffset, endOffset, tokenText);
		this.tokenTag = tokenTag;
	}
	
	public String getTokenTag() {
		return tokenTag;
	}
	
	public String toString() {
		return this.tokenText;
	}
	
	public void setTokenTag(String tag) {
		this.tokenTag = tag;
	}
	
	public Phrase toPhrase() {
		Phrase phrase = new Phrase(this);
		return phrase;
	}
}
