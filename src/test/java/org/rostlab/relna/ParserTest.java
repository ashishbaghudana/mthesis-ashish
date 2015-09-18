package org.rostlab.relna;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Test;
import org.junit.Assert;
import org.rostlab.relna.corpus.Document;
import org.rostlab.relna.parser.GimliParser;

public class ParserTest {
	
	@Test
	public void testGimliParser() {
		GimliParser gimli = new GimliParser();
		Document doc = null;
		try {
			FileInputStream file = new FileInputStream(new File("src/test/resources/gimli/gimliOutput.iob2"));
			doc = gimli.parse(file);
			System.out.println(doc.getSection(0));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Assert.assertEquals("###MEDLINE:10503549", doc.documentId);
		Assert.assertEquals(2, doc.size());
		Assert.assertEquals("The katX gene of Bacillus subtilis is under dual control of sigmaB and sigmaF .", doc.getSection(0).getSentence(0).toString());
		Assert.assertEquals("The gene katX , which encodes a catalase in Bacillus subtilis , is transcribed by EsigmaF in the pre- spore .", doc.getSection(1).getSentence(0).toString());
		Assert.assertEquals("Our results indicate that the level of KatX level in outgrowing spores depends mainly on EsigmaF , because sigB mutants show normal KatX activity in dormant and outgrowing spores .", doc.getSection(1).getSentence(4).toString());
	}
}
