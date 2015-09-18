package org.rostlab.relna;

import org.junit.Assert;
import org.junit.Test;
import org.rostlab.relna.external.GimliConverter;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

public class GimliConverterTest {
	
	@Test
	public void testGimliConverter() {
		String input = "/home/ashish/relna/src/test/resources/corpora/txt/corpus.txt";
		String original = "/home/ashish/relna/src/test/resources/gimli/convertedCorpus.iob2";
		String output = "/home/ashish/relna/src/test/resources/corpora/txt/output.iob2";
		GimliConverter gimli = new GimliConverter(input, output);
		gimli.convert();
		try {
			Assert.assertEquals(true, FileUtils.contentEquals(new File(original), new File(output)));
			System.out.println("Testing Gimli Converter");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			File f = new File(output);
			if (f.exists())
				f.delete();
		}		
	}
}
