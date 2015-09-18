package org.rostlab.relna;

import org.junit.Test;
import org.rostlab.relna.annotator.RelationshipExtractor;
import org.rostlab.relna.corpus.Document;
import org.rostlab.relna.corpus.Phrase;
import org.rostlab.relna.parser.GimliParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;

public class RelationshipExtractorTest {
	
	@Test
	public void testRelationshipExtractor() {
		GimliParser gimli = new GimliParser();
		Document doc = null;
		try {
			FileInputStream file = new FileInputStream(new File("src/test/resources/gimli/gimliOutput.iob2"));
			doc = gimli.parse(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		RelationshipExtractor relationships = new RelationshipExtractor(doc);
		HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> rel = relationships.getProteinDNARelationships();
		Assert.assertEquals("sigmaB", rel.get(0).get(0).get(0).get(0).toString());
		Assert.assertEquals("katX gene", rel.get(0).get(0).get(0).get(1).toString());
		rel = relationships.getProteinRNARelationships();
		Assert.assertEquals(0, rel.size());
	}
	
}
