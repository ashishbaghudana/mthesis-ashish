package org.rostlab.relna;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;
import org.junit.Assert;
import org.rostlab.relna.corpus.Document;
import org.rostlab.relna.parser.GimliParser;

public class SentenceTest {
	
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
	public void testSentence() {
		readDocumentFromFile();
		
	}

}
