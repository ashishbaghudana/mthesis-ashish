package org.rostlab.relna.corpus;

import java.util.ArrayList;
import java.util.HashMap;

public class Document {
	public final String documentId;
	private ArrayList<Section> sections;
	
	public Document(String documentId) {
		this.documentId = documentId;
		this.sections = new ArrayList<Section>();
	}
	
	public Document(String documentId, ArrayList<Section> sections) {
		this(documentId);
		this.sections = sections;
	}
	
	public void addSection(Section section) {
		this.sections.add(section);
	}
	
	public Section getSection(int index) {
		return this.sections.get(index);
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> getProteinTokens() {
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> proteinTokens = new HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>>();
		for (Section section : sections) {
			proteinTokens.put(section.sectionId, section.getProteinTokens());
		}
		return proteinTokens;
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> getDNATokens() {
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> DNATokens = new HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>>();
		for (Section section : sections) {
			DNATokens.put(section.sectionId, section.getDNATokens());
		}
		return DNATokens;
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> getRNATokens() {
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> RNATokens = new HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>>();
		for (Section section : sections) {
			RNATokens.put(section.sectionId, section.getRNATokens());
		}
		return RNATokens;
	}
	
	public int size() {
		return this.sections.size();
	}
	
	public ArrayList<Section> getSections() {
		return this.sections;
	}
}
