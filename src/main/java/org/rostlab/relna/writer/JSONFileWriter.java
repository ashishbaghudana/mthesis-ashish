package org.rostlab.relna.writer;

import org.rostlab.relna.corpus.*;
import org.rostlab.relna.config.Constants;
import org.json.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONFileWriter implements IWriter {
	
	private Document document;
	private JSONObject json;

	public void write(String file) {
		try {
			FileWriter fw = new FileWriter(new File(file));
			fw.write(json.toString(4));
			fw.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(Writer writer) {
		try {
			writer.write(json.toString(4));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public JSONFileWriter(Document doc) {
		this.document = doc;
		this.json = new JSONObject();
		try {
			this.createJSON();
		} catch (JSONException j) {
			j.printStackTrace();
		}
	}
	
	private void createJSON() throws JSONException {
		json.accumulate("anncomplete", false);
		json.accumulate("id", document.documentId);
		json.accumulate("title", document.getSection(Constants.TITLE));
		json.accumulate("abstract", document.getSection(Constants.ABSTRACT));
		
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> proteinTokens = document.getProteinTokens();
		for (int key : proteinTokens.keySet()) {
			HashMap<Integer, ArrayList<Phrase>> sectionProteinTokens = proteinTokens.get(key);
			for (int sentenceKey : sectionProteinTokens.keySet()) {
				ArrayList<Phrase> sentenceProteinTokens = sectionProteinTokens.get(sentenceKey);
				for(Phrase phrase : sentenceProteinTokens) {
					JSONObject phraseJSON = new JSONObject();
					phraseJSON.accumulate("tokenStart", phrase.getStartOffset());
					phraseJSON.accumulate("tokenEnd", phrase.getEndOffset());
					phraseJSON.accumulate("token", phrase.toString());
					phraseJSON.accumulate("tokenType", "protein");
					phraseJSON.accumulate("sentenceId", sentenceKey);
					phraseJSON.accumulate("sectionId", key);
					json.accumulate("proteinEntities", phraseJSON);
				}
			}
		}
		
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> DNATokens = document.getDNATokens();
		for (int key : DNATokens.keySet()) {
			HashMap<Integer, ArrayList<Phrase>> sectionDNATokens = DNATokens.get(key);
			for (int sentenceKey : sectionDNATokens.keySet()) {
				ArrayList<Phrase> sentenceDNATokens = sectionDNATokens.get(sentenceKey);
				for(Phrase phrase : sentenceDNATokens) {
					JSONObject phraseJSON = new JSONObject();
					phraseJSON.accumulate("tokenStart", phrase.getStartOffset());
					phraseJSON.accumulate("tokenEnd", phrase.getEndOffset());
					phraseJSON.accumulate("token", phrase.toString());
					phraseJSON.accumulate("tokenType", "protein");
					phraseJSON.accumulate("sentenceId", sentenceKey);
					phraseJSON.accumulate("sectionId", key);
					json.accumulate("DNAEntities", phraseJSON);
				}
			}
		}
		
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> RNATokens = document.getRNATokens();
		for (int key : RNATokens.keySet()) {
			HashMap<Integer, ArrayList<Phrase>> sectionRNATokens = RNATokens.get(key);
			for (int sentenceKey : sectionRNATokens.keySet()) {
				ArrayList<Phrase> sentenceRNATokens = sectionRNATokens.get(sentenceKey);
				for(Phrase phrase : sentenceRNATokens) {
					JSONObject phraseJSON = new JSONObject();
					phraseJSON.accumulate("tokenStart", phrase.getStartOffset());
					phraseJSON.accumulate("tokenEnd", phrase.getEndOffset());
					phraseJSON.accumulate("token", phrase.toString());
					phraseJSON.accumulate("tokenType", "protein");
					phraseJSON.accumulate("sentenceId", sentenceKey);
					phraseJSON.accumulate("sectionId", key);
					json.accumulate("RNAEntities", phraseJSON);
				}
			}
		}
		
		JSONObject relationships = new JSONObject();
		HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> proteinDNARelations = document.getProteinDNARelationships();
		for (int key : proteinDNARelations.keySet()) {
			HashMap<Integer, ArrayList<ArrayList<Phrase>>> sectionProtDNARel = proteinDNARelations.get(key);
			for (int sectionKey : sectionProtDNARel.keySet()) {
				ArrayList<ArrayList<Phrase>> sentenceProtDNARel = sectionProtDNARel.get(sectionKey);
				for (ArrayList<Phrase> phrase : sentenceProtDNARel) {
					JSONObject relation = new JSONObject();
					relation.accumulate("sectionId", key);
					relation.accumulate("sentenceId", sectionKey);
					relation.accumulate("proteinTokenStart", phrase.get(0).getStartOffset());
					relation.accumulate("proteinTokenEnd", phrase.get(0).getEndOffset());
					relation.accumulate("DNATokenStart", phrase.get(1).getStartOffset());
					relation.accumulate("DNATokenEnd", phrase.get(1).getEndOffset());
					relation.accumulate("proteinToken", phrase.get(0).toString());
					relation.accumulate("DNAToken", phrase.get(1).toString());
					relationships.accumulate("ProteinDNARelationships", relation);
				}
			}
		}
		if (relationships.length()!=0)
			json.accumulate("relationships", relationships);
		
		relationships = new JSONObject();
		HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> proteinRNARelations = document.getProteinRNARelationships();
		for (int key : proteinRNARelations.keySet()) {
			HashMap<Integer, ArrayList<ArrayList<Phrase>>> sectionProtRNARel = proteinRNARelations.get(key);
			for (int sectionKey : sectionProtRNARel.keySet()) {
				ArrayList<ArrayList<Phrase>> sentenceProtRNARel = sectionProtRNARel.get(sectionKey);
				for (ArrayList<Phrase> phrase : sentenceProtRNARel) {
					JSONObject relation = new JSONObject();
					relation.accumulate("sectionId", key);
					relation.accumulate("sentenceId", sectionKey);
					relation.accumulate("proteinTokenStart", phrase.get(0).getStartOffset());
					relation.accumulate("proteinTokenEnd", phrase.get(0).getEndOffset());
					relation.accumulate("RNATokenStart", phrase.get(1).getStartOffset());
					relation.accumulate("RNATokenEnd", phrase.get(1).getEndOffset());
					relation.accumulate("proteinToken", phrase.get(0).toString());
					relation.accumulate("RNAToken", phrase.get(1).toString());
					relationships.accumulate("ProteinRNARelationships", relation);
				}
			}
		}
		if (relationships.length()!=0)
			json.accumulate("relationships", relationships);
	}

}

