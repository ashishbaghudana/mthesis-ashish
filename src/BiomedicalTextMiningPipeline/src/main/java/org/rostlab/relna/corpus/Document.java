package org.rostlab.relna.corpus;

import java.util.ArrayList;
import java.util.HashMap;

public class Document {
	public final String documentId;
	private ArrayList<Section> sections;
	private HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> proteinDNARelationships;
	private HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> proteinRNARelationships;
	
	public Document(String documentId) {
		this.documentId = documentId;
		this.sections = new ArrayList<Section>();
		this.proteinDNARelationships = null;
		this.proteinRNARelationships = null;
	}
	
	public Document(String documentId, ArrayList<Section> sections) {
		this(documentId);
		this.sections = sections;
		this.proteinDNARelationships = null;
		this.proteinRNARelationships = null;
	}
	
	public void addSection(Section section) {
		this.sections.add(section);
	}
	
	public Section getSection(int index) {
		return this.sections.get(index);
	}
	
	public void setProteinDNARelationships(HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> map) {
		this.proteinDNARelationships = map;
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> getProteinDNARelationships() {
		return this.proteinDNARelationships;
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> getProteinRNARelationships() {
		return this.proteinRNARelationships;
	}
	
	public void setProteinRNARelationships(HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> map) {
		this.proteinRNARelationships = map;
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> getProteinTokens() {
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> proteinTokens = new HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>>();
		for (Section section : sections) {
			if (!section.getProteinTokens().isEmpty())
				proteinTokens.put(section.sectionId, section.getProteinTokens());
		}
		return proteinTokens;
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> getDNATokens() {
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> DNATokens = new HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>>();
		for (Section section : sections) {
			if (!section.getDNATokens().isEmpty())
				DNATokens.put(section.sectionId, section.getDNATokens());
		}
		return DNATokens;
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> getRNATokens() {
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> RNATokens = new HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>>();
		for (Section section : sections) {
			if (!section.getRNATokens().isEmpty())
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
