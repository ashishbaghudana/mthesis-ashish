package org.rostlab.relna;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.rostlab.relna.annotator.RelationshipExtractor;
import org.rostlab.relna.corpus.*;
import org.rostlab.relna.parser.GimliParser;
import org.rostlab.relna.writer.JSONFileWriter;

public class JSONFileWriterTest {
	
	@Test
	public void testJSONFileWriter() {
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
		doc.setProteinDNARelationships(rel);
		rel = relationships.getProteinRNARelationships();
		doc.setProteinRNARelationships(rel);
		JSONFileWriter jsonWriter = new JSONFileWriter(doc);
		jsonWriter.write("src/test/resources/corpora/txt/output.json");
		try {
			Assert.assertEquals(true, FileUtils.contentEquals(new File("src/test/resources/corpora/txt/expectedOutput.json"), new File("src/test/resources/corpora/txt/output.json")));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			File f = new File("src/test/resources/corpora/txt/output.json");
			if (f.exists()) {
				f.delete();
			}
		}
	}
}
