package org.rostlab.relna.annotator;

import org.rostlab.relna.corpus.*;
import java.util.HashMap;
import java.util.ArrayList;

public class RelationshipExtractor {
	
	private Document document;
	
	public RelationshipExtractor(Document doc) {
		this.document = doc;
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> getProteinDNARelationships() {
		HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> proteinDNA = new HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>>();
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> DNATokens = this.document.getDNATokens();
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> proteinTokens = this.document.getProteinTokens();
		
		for (int i=0; i<this.document.size(); i++){
			HashMap<Integer, ArrayList<ArrayList<Phrase>>> map = new HashMap<Integer, ArrayList<ArrayList<Phrase>>>();
			for (int j=0; j<this.document.getSection(i).size(); j++) {
				if (this.hasProteinTokens(proteinTokens.get(i).get(j)) && this.hasDNATokens(DNATokens.get(i).get(j))) {
					ArrayList<ArrayList<Phrase>> phrases = new ArrayList<ArrayList<Phrase>>();
					for (Phrase proteinPhrase : proteinTokens.get(i).get(j)) {
						for (Phrase DNAPhrase : DNATokens.get(i).get(j)) {
							ArrayList<Phrase> smallPhrase = new ArrayList<Phrase>();
							smallPhrase.add(proteinPhrase);
							smallPhrase.add(DNAPhrase);
							phrases.add(smallPhrase);
						}
					}
					map.put(j, phrases);
				}
			}
			proteinDNA.put(i, map);
		}
		
		return proteinDNA;
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> getProteinRNARelationships() {
		HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> proteinRNA = new HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>>();
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> RNATokens = this.document.getRNATokens();
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> proteinTokens = this.document.getProteinTokens();
		
		for (int i=0; i<this.document.size(); i++){
			HashMap<Integer, ArrayList<ArrayList<Phrase>>> map = new HashMap<Integer, ArrayList<ArrayList<Phrase>>>();
			for (int j=0; j<this.document.getSection(i).size(); j++) {
				if (this.hasProteinTokens(proteinTokens.get(i).get(j)) && this.hasRNATokens(RNATokens.get(i).get(j))) {
					ArrayList<ArrayList<Phrase>> phrases = new ArrayList<ArrayList<Phrase>>();
					for (Phrase proteinPhrase : proteinTokens.get(i).get(j)) {
						for (Phrase DNAPhrase : RNATokens.get(i).get(j)) {
							ArrayList<Phrase> smallPhrase = new ArrayList<Phrase>();
							smallPhrase.add(proteinPhrase);
							smallPhrase.add(DNAPhrase);
							phrases.add(smallPhrase);
						}
					}
					map.put(j, phrases);
				}
			}
			proteinRNA.put(i, map);
		}
		
		return proteinRNA;
	}
	
	private boolean hasProteinTokens(ArrayList<Phrase> phrases) {
		if (phrases!=null)
			return true;
		return false;
	}
	
	private boolean hasDNATokens(ArrayList<Phrase> phrases) {
		if (phrases!=null)
			return true;
		return false;
	}
	
	private boolean hasRNATokens(ArrayList<Phrase> phrases) {
		if (phrases!=null)
			return true;
		return false;
	}

}
