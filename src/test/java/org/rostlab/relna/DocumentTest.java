package org.rostlab.relna;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.rostlab.relna.corpus.Document;
import org.rostlab.relna.corpus.Phrase;
import org.rostlab.relna.corpus.Section;
import org.rostlab.relna.corpus.Token;
import org.rostlab.relna.parser.GimliParser;

public class DocumentTest {
	
	private Document doc;
	
	private void readDocumentFromFile() {
		GimliParser gimli = new GimliParser();
		try {
			FileInputStream file = new FileInputStream(new File("src/test/resources/gimli/gimliOutput.iob2"));
			doc = gimli.parse(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDocument() {
		readDocumentFromFile();
		testGetDNATokens();
		testGetProteinTokens();
		testGetRNATokens();
		testGetTriggerWords();
		testGetSections();
		testGetSection();
		testGetSize();
	}
	
	private void testGetTriggerWords() {
		HashMap<Integer, HashMap<Integer, ArrayList<Token>>> triggerWords = doc.getTriggerWords();
		Assert.assertEquals("control", triggerWords.get(0).get(0).get(0).tokenText);
	}
	
	private void testGetSize() {
		Assert.assertEquals(2, doc.size());
	}
	
	private void testGetSection() {
		Section s = doc.getSection(0);
		Assert.assertEquals(0, s.sectionId);
	}
	
	private void testGetSections() {
		ArrayList<Section> sections = doc.getSections();
		Assert.assertEquals("The katX gene of Bacillus subtilis is under dual control of sigmaB and sigmaF .", sections.get(0).getSentence(0).toString());
	}
	
	private void testGetProteinTokens() {
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> proteinTokens = this.doc.getProteinTokens();
		Assert.assertEquals("sigmaB", proteinTokens.get(0).get(0).get(0).toString());
		Assert.assertEquals("sigmaF", proteinTokens.get(0).get(0).get(1).toString());
	}
	
	private void testGetDNATokens() {
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> DNATokens = this.doc.getDNATokens();
		Assert.assertEquals("katX gene", DNATokens.get(0).get(0).get(0).toString());
	}
	
	private void testGetRNATokens() {
		HashMap<Integer, HashMap<Integer, ArrayList<Phrase>>> RNATokens = this.doc.getRNATokens();
		Assert.assertEquals(0, RNATokens.size());
	}

}
