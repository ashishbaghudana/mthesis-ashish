package org.rostlab.relna.corpus;

import java.util.ArrayList;
import org.rostlab.relna.config.Constants;
import org.rostlab.relna.corpus.Token;

public class Sentence {
	public final int sentenceId;
	private ArrayList<Token> words;
	
	public Sentence(int id) {
		this.sentenceId = id;
		this.words = new ArrayList<Token>();
	}
	
	public Sentence(int id, ArrayList<Token> words) {
		this(id);
		this.words = words;
	}
	
	public ArrayList<Token> getWords() {
		return this.words;
	}
	
	public void addToken(Token token) {
		this.words.add(token);
	}
	
	public Token getToken(int index) {
		return this.words.get(index);
	}
	
	public String toString() {
		String s = "";
		for (Token word : this.words) {
			s = s.concat(word.tokenText+" ");
		}
		return s.trim();
	}
	
	public ArrayList<Phrase> getProteinTokens() {
		ArrayList<Phrase> proteinTokens = new ArrayList<Phrase>();
		Phrase phrase = null;
		for (Token word : words) {
			if (word.getTokenTag().equals(Constants.B_PROTEIN)) {
				if (phrase!=null) {
					proteinTokens.add(phrase);
				}
				phrase = new Phrase(word);
				phrase.setEndOffset(word.endOffset);
			}
			else if (word.getTokenTag().equals(Constants.I_PROTEIN)) {
				phrase.addToken(word);
				phrase.setEndOffset(word.endOffset);
			}
			else {
				if (phrase!=null)
					proteinTokens.add(phrase);
				phrase = null;
			}
		}
		return proteinTokens;
	}
	
	public ArrayList<Phrase> getRNATokens() {
		ArrayList<Phrase> RNATokens = new ArrayList<Phrase>();
		Phrase phrase = null;
		for (Token word : words) {
			if (word.getTokenTag().equals(Constants.B_RNA)) {
				if (phrase!=null) {
					RNATokens.add(phrase);
				}
				phrase = new Phrase(word);
				phrase.setEndOffset(word.endOffset);
			}
			else if (word.getTokenTag().equals(Constants.I_RNA)) {
				phrase.addToken(word);
				phrase.setEndOffset(word.endOffset);
			}
			else {
				if (phrase!=null)
					RNATokens.add(phrase);
				phrase = null;
			}
		}
		return RNATokens;
	}
	
	public ArrayList<Phrase> getDNATokens() {
		ArrayList<Phrase> DNATokens = new ArrayList<Phrase>();
		Phrase phrase = null;
		for (Token word : words) {
			if (word.getTokenTag().equals(Constants.B_DNA)) {
				if (phrase!=null) {
					DNATokens.add(phrase);
				}
				phrase = new Phrase(word);
				phrase.setEndOffset(word.endOffset);
			}
			else if (word.getTokenTag().equals(Constants.I_DNA)) {
				phrase.addToken(word);
				phrase.setEndOffset(word.endOffset);
			}
			else {
				if (phrase!=null)
					DNATokens.add(phrase);
				phrase = null;
			}
		}
		return DNATokens;
	}
}
