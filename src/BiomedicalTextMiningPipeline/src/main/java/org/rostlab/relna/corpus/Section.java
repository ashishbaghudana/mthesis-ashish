package org.rostlab.relna.corpus;

import java.util.ArrayList;
import java.util.HashMap;

public class Section {
	public final int sectionId;
	private String sectionTitle;
	public ArrayList<Sentence> sentences;
	
	public Section(int sectionId) {
		this.sectionId = sectionId;
		this.sentences = new ArrayList<Sentence>();
	}
	
	public Section(int sectionId, ArrayList<Sentence> sentences) {
		this(sectionId);
		this.sentences = sentences;
	}
	
	public Section(int sectionId, String sectionTitle, ArrayList<Sentence> sentences) {
		this(sectionId, sentences);
		this.sectionTitle = sectionTitle;
	}
	
	public String getSectionTitle() {
		return this.sectionTitle;
	}
	
	public String toString() {
		String s = "";
		for (Sentence sentence : sentences) {
			s = s.concat(sentence+" ");
		}
		return s.trim();
	}
	
	public void addSentence(Sentence sentence) {
		this.sentences.add(sentence);
	}
	
	public Sentence getSentence(int index) {
		return this.sentences.get(index);
	}
	
	public HashMap<Integer, ArrayList<Phrase>> getProteinTokens() {
		HashMap<Integer, ArrayList<Phrase>> proteinTokens = new HashMap<Integer, ArrayList<Phrase>>();
		for (Sentence sentence : sentences) {
			proteinTokens.put(sentence.sentenceId, sentence.getProteinTokens());
		}
		return proteinTokens;
	}
	
	public HashMap<Integer, ArrayList<Phrase>> getDNATokens() {
		HashMap<Integer, ArrayList<Phrase>> DNATokens = new HashMap<Integer, ArrayList<Phrase>>();
		for (Sentence sentence : sentences) {
			DNATokens.put(sentence.sentenceId, sentence.getDNATokens());
		}
		return DNATokens;
	}
	
	public HashMap<Integer, ArrayList<Phrase>> getRNATokens() {
		HashMap<Integer, ArrayList<Phrase>> RNATokens = new HashMap<Integer, ArrayList<Phrase>>();
		for (Sentence sentence : sentences) {
			RNATokens.put(sentence.sentenceId, sentence.getRNATokens());
		}
		return RNATokens;
	}
	
	public int size() {
		return this.sentences.size();
	}
}
