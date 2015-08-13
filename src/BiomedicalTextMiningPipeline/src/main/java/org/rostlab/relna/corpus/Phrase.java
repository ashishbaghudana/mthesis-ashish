package org.rostlab.relna.corpus;

import java.util.ArrayList;

public class Phrase {
	private int startOffset;
	private int endOffset;
	private ArrayList<Token> words;
	
	public Phrase(int startOffset) {
		this.startOffset = startOffset;
		this.words = new ArrayList<Token>();
	}
	
	public Phrase(Token word) {
		this.startOffset = word.startOffset;
		this.words = new ArrayList<Token>();
		this.words.add(word);
	}
	
	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}
	
	public void addToken(Token word) {
		this.words.add(word);
	}
	
	public int getStartOffset() {
		return this.startOffset;
	}
	
	public int getEndOffset() {
		return this.endOffset;
	}
	
	public String toString() {
		String s = "";
		for (Token word : words) {
			s = s.concat(word.tokenText+" ");
		}
		return s.trim();
	}
}
