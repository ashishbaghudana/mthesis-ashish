package org.rostlab.relna.annotator;

import java.util.ArrayList;
import java.util.HashMap;

import org.rostlab.relna.corpus.Document;
import org.rostlab.relna.corpus.Phrase;

public class DictionaryBasedRelationshipExtractor extends RelationshipExtractor {

	public DictionaryBasedRelationshipExtractor(Document doc) {
		super(doc);
	}
	
	public HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> getProteinDNARelationships() {
		HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> proteinDNA = new HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>>();
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> DNATokens = this.document.getDNATokens();
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> proteinTokens = this.document.getProteinTokens();
		for (int i=0; i<this.document.size(); i++){
			HashMap<Integer, ArrayList<ArrayList<Phrase>>> map = new HashMap<Integer, ArrayList<ArrayList<Phrase>>>();
			for (int j=0; j<this.document.getSection(i).size(); j++) {
				if (proteinTokens.containsKey(i) && DNATokens.containsKey(i) && proteinTokens.get(i).containsKey(j) && DNATokens.get(i).containsKey(j) && this.hasProteinTokens(proteinTokens.get(i).get(j)) && this.hasDNATokens(DNATokens.get(i).get(j))) {
					ArrayList<ArrayList<Phrase>> phrases = new ArrayList<ArrayList<Phrase>>();
					for (Phrase proteinPhrase : proteinTokens.get(i).get(j)) {
						for (Phrase DNAPhrase : DNATokens.get(i).get(j)) {
							ArrayList<Phrase> smallPhrase = new ArrayList<Phrase>();
							smallPhrase.add(proteinPhrase);
							smallPhrase.add(DNAPhrase);
							phrases.add(smallPhrase);
						}
					}
					if (!phrases.isEmpty()) {
						if (!this.document.getSection(i).getSentence(j).getTriggerWords().isEmpty())
							map.put(j, phrases);
					}
						
				}
			}
			if (!map.isEmpty())
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
				if (proteinTokens.containsKey(i) && RNATokens.containsKey(i) && proteinTokens.get(i).containsKey(j) && RNATokens.get(i).containsKey(j) &&this.hasProteinTokens(proteinTokens.get(i).get(j)) && this.hasRNATokens(RNATokens.get(i).get(j))) {
					ArrayList<ArrayList<Phrase>> phrases = new ArrayList<ArrayList<Phrase>>();
					for (Phrase proteinPhrase : proteinTokens.get(i).get(j)) {
						for (Phrase DNAPhrase : RNATokens.get(i).get(j)) {
							ArrayList<Phrase> smallPhrase = new ArrayList<Phrase>();
							smallPhrase.add(proteinPhrase);
							smallPhrase.add(DNAPhrase);
							phrases.add(smallPhrase);
						}
					}
					if (!phrases.isEmpty()) {
						if (!this.document.getSection(i).getSentence(j).getTriggerWords().isEmpty())
							map.put(j, phrases);
					}
				}
			}
			if (!map.isEmpty())
				proteinRNA.put(i, map);
		}
		
		return proteinRNA;
	}

}
