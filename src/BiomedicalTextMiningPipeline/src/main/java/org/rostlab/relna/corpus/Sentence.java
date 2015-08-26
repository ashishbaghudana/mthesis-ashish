package org.rostlab.relna.corpus;

import java.util.ArrayList;
import org.rostlab.relna.config.Constants;
import org.rostlab.relna.corpus.Token;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;
import opennlp.tools.stemmer.PorterStemmer;
import java.io.FileInputStream;
import java.io.IOException;
import org.rostlab.relna.config.TriggerWords;

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
	
	public ArrayList<Token> getTriggerWords() {
		FileInputStream modelIn = null;
		POSModel posModel = null;
		try {
			modelIn = new FileInputStream("src/main/java/resources/postag/en-pos-maxent.bin");
			posModel = new POSModel(modelIn);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		POSTaggerME posTagger = new POSTaggerME(posModel);
		PorterStemmer stemmer = new PorterStemmer();
		String [] tags = posTagger.tag(this.toArray());
		ArrayList<Token> triggerWords = new ArrayList<Token>();
		int i = 0;
		
		for (String tag : tags) {
			if (tag.startsWith("VB") && TriggerWords.triggers.contains(stemmer.stem(tag))) {
				triggerWords.add(this.words.get(i));
			}
			i++;
		}
		return triggerWords;
	}
	
	private String [] toArray() {
		String [] wordArray = new String[words.size()];
		int i = 0;
		for (Token token : words) {
			wordArray[i] = token.tokenText;
			i++;
		}
		return wordArray;
	}
}
