package org.rostlab.relna.parser;

import org.rostlab.relna.corpus.*;
import org.rostlab.relna.annotator.RelationshipExtractor;
import org.rostlab.relna.config.Constants;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

public class GimliParser implements Parser {
	
	public Document parse(InputStream is) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String s;
		int sentenceNumber=0;
		int startOffset=0;
		int endOffset=-1;
		
//		Read the document ID: Medline
		s = br.readLine();
		Document doc = new Document(s);
		
		s = br.readLine();
		
//		Add a section for the title
		Section title = new Section(Constants.TITLE);
		Sentence sentence = new Sentence(sentenceNumber);
		String [] line;
		
//		Read the first sentence of the document, which is the title
		while (!(s=br.readLine()).equals(".\tO")) {
			line = s.split("\t");
			startOffset = endOffset + 1;
			endOffset = startOffset + line[0].length();
			Token token = new Token(startOffset, endOffset-1, line[0], line[1]);
			sentence.addToken(token);
		}
		line = s.split("\t");
		startOffset = endOffset + 1;
		endOffset = startOffset + line[0].length();

		Token token = new Token(startOffset, endOffset-1, line[0], line[1]);
		sentence.addToken(token);
		
		title.addSentence(sentence);
		
		doc.addSection(title);
		
		Section paperAbstract = new Section(Constants.ABSTRACT);
		
		while ((s=br.readLine())!=null) {
			
			sentence = new Sentence(sentenceNumber);
			
			s = br.readLine();
			
			if (s!=null) {
				while(!(s.equals(".\tO"))) {
					System.out.println(s);
					line = s.split("\t");
					startOffset = endOffset + 1;
					endOffset = startOffset + line[0].length();
					token = new Token(startOffset, endOffset-1, line[0], line[1]);
					sentence.addToken(token);
					s = br.readLine();
				}
				
				System.out.println(s);
				
				line = s.split("\t");
				startOffset = endOffset + 1;
				endOffset = startOffset + line[0].length();
				token = new Token(startOffset, endOffset-1, line[0], line[1]);
				sentence.addToken(token);
				sentenceNumber++;
				paperAbstract.addSentence(sentence);
			}
		}
		
		doc.addSection(paperAbstract);
		
		return doc;
	}
	
	public static void main(String [] args) {
		GimliParser gimli = new GimliParser();
		Document doc = null;
		try {
			FileInputStream file = new FileInputStream(new File("/home/ashish/workspace/BiomedicalTextMiningPipieline/src/main/resources/sample/abstract/output.txt"));
			doc = gimli.parse(file);
			System.out.println(doc.getSection(0));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		RelationshipExtractor relationships = new RelationshipExtractor(doc);
		HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> rel = relationships.getProteinDNARelationships();
		System.out.println(rel);
		
		rel = relationships.getProteinRNARelationships();
		System.out.println(rel);
	}
}
