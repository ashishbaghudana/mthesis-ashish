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
			this.json.write(fw);
			fw.close();
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(Writer writer) {
		this.json.write(writer);
	}
	
	public JSONFileWriter(Document doc) {
		this.document = doc;
		this.json = new JSONObject();
		this.createJSON();
	}
	
	private void createJSON() {
		json.accumulate("anncomplete", false);
		json.accumulate("id", document.documentId);
		json.accumulate("title", document.getSection(Constants.TITLE));
		json.accumulate("abstract", document.getSection(Constants.ABSTRACT));
		
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> proteinTokens = document.getProteinTokens();
		for (int key : proteinTokens.keySet()) {
			JSONObject sectionJSON = new JSONObject();
			HashMap<Integer, ArrayList<Phrase>> sectionProteinTokens = proteinTokens.get(key);
			for (int sentenceKey : sectionProteinTokens.keySet()) {
				JSONObject sentenceJSON = new JSONObject();
				ArrayList<Phrase> sentenceProteinTokens = sectionProteinTokens.get(sentenceKey);
				for(Phrase phrase : sentenceProteinTokens) {
					JSONObject phraseJSON = new JSONObject();
					phraseJSON.accumulate("tokenStart", phrase.getStartOffset());
					phraseJSON.accumulate("tokenEnd", phrase.getEndOffset());
					phraseJSON.accumulate("token", phrase.toString());
					phraseJSON.accumulate("tokenType", "protein");
					sentenceJSON.accumulate("proteinEntities", phraseJSON);
				}
				sentenceJSON.accumulate("sentenceId", sentenceKey);
				sectionJSON.accumulate("proteinEntities", sentenceJSON);
			}
			sectionJSON.accumulate("sectionId", key);
			json.accumulate("proteinEntities", sectionJSON);
		}
		
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> DNATokens = document.getDNATokens();
		for (int key : DNATokens.keySet()) {
			JSONObject sectionJSON = new JSONObject();
			HashMap<Integer, ArrayList<Phrase>> sectionDNATokens = DNATokens.get(key);
			for (int sentenceKey : sectionDNATokens.keySet()) {
				JSONObject sentenceJSON = new JSONObject();
				ArrayList<Phrase> sentenceDNATokens = sectionDNATokens.get(sentenceKey);
				for(Phrase phrase : sentenceDNATokens) {
					JSONObject phraseJSON = new JSONObject();
					phraseJSON.accumulate("tokenStart", phrase.getStartOffset());
					phraseJSON.accumulate("tokenEnd", phrase.getEndOffset());
					phraseJSON.accumulate("token", phrase.toString());
					phraseJSON.accumulate("tokenType", "protein");
					sentenceJSON.accumulate("DNAEntities", phraseJSON);
				}
				sentenceJSON.accumulate("sentenceId", sentenceKey);
				sectionJSON.accumulate("DNAEntities", sentenceJSON);
			}
			sectionJSON.accumulate("sectionId", key);
			json.accumulate("DNAEntities", sectionJSON);
		}
		
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> RNATokens = document.getRNATokens();
		for (int key : RNATokens.keySet()) {
			JSONObject sectionJSON = new JSONObject();
			HashMap<Integer, ArrayList<Phrase>> sectionRNATokens = RNATokens.get(key);
			for (int sentenceKey : sectionRNATokens.keySet()) {
				JSONObject sentenceJSON = new JSONObject();
				ArrayList<Phrase> sentenceRNATokens = sectionRNATokens.get(sentenceKey);
				for(Phrase phrase : sentenceRNATokens) {
					JSONObject phraseJSON = new JSONObject();
					phraseJSON.accumulate("tokenStart", phrase.getStartOffset());
					phraseJSON.accumulate("tokenEnd", phrase.getEndOffset());
					phraseJSON.accumulate("token", phrase.toString());
					phraseJSON.accumulate("tokenType", "protein");
					sentenceJSON.accumulate("RNAEntities", phraseJSON);
				}
				sentenceJSON.accumulate("sentenceId", sentenceKey);
				sectionJSON.accumulate("RNAEntities", sentenceJSON);
			}
			sectionJSON.accumulate("sectionId", key);
			json.accumulate("RNAEntities", sectionJSON);
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
					relation.accumulate("DNATokenStart", phrase.get(1).getEndOffset());
					relation.accumulate("proteinToken", phrase.get(0).toString());
					relation.accumulate("DNAToken", phrase.get(1).toString());
					relationships.accumulate("ProteinDNARelationships", relation);
				}
			}
		}
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
					relation.accumulate("RNATokenStart", phrase.get(1).getEndOffset());
					relation.accumulate("proteinToken", phrase.get(0).toString());
					relation.accumulate("RNAToken", phrase.get(1).toString());
					relationships.accumulate("ProteinRNARelationships", relation);
				}
			}
		}
		json.accumulate("relationships", relationships);
		
	}

}

