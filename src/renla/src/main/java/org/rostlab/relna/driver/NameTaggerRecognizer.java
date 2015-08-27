package org.rostlab.relna.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.rostlab.relna.annotator.*;
import org.rostlab.relna.corpus.Document;
import org.rostlab.relna.corpus.Phrase;
import org.rostlab.relna.parser.GimliParser;
import org.rostlab.relna.external.GimliConverter;
import org.rostlab.relna.writer.JSONFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NameTaggerRecognizer {
	
	private static Logger logger = LoggerFactory.getLogger(NameTaggerRecognizer.class);
	
	public static void main(String [] args) {
		DefaultParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption("h", "help", false, "Print usage information.");
		options.addOption("c", "corpus", true, "File with the corpus.");
	    options.addOption("g", "gdep", true, "File to load/save the GDep output.");
		options.addOption("o", "output", true, "File to save the final output.");
		options.addOption("f", "format", true, "Input file format - txt or iob2");
		
		CommandLine commandLine = null;
		
		String corpus = null;
		String gdep = null;
		String output = null;
		String format = null;
		
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			logger.error("There was an error parsing the command line arguments.", e);
		}
		
		if (commandLine.hasOption("h")) {
			System.out.println("Help for this module.");
			return;
		}
		
		if (commandLine.hasOption("c")) {
			corpus = commandLine.getOptionValue("c");
		} 
		else {
			System.err.println("Please specify the corpus.");
			return;
		}
		
		if (commandLine.hasOption("g")) {
			gdep = commandLine.getOptionValue("g");
		} 
		else {
			System.err.println("Please specify the file to load/save GDep.");
			return;
		}
		
		if (commandLine.hasOption("f")) {
			format = commandLine.getOptionValue("f");
		}
		else {
			System.err.println("Please specify the input file format.");
			return;
		}
		
		if (commandLine.hasOption("o")) {
			output = commandLine.getOptionValue("o");
		} 
		else {
			System.err.println("Please specify the file to save the output.");
			return;
		}
		
		if (format.equals("iob2")) {
			NamedEntityRecognizer ner = new NamedEntityRecognizer(corpus, gdep, output);		
			ner.tag();
		}
		else {
			GimliConverter gimliConverter = new GimliConverter(corpus, FilenameUtils.removeExtension(corpus)+".iob2");
			gimliConverter.convert();
			NamedEntityRecognizer ner = new NamedEntityRecognizer(FilenameUtils.removeExtension(corpus)+".iob2", gdep, output);
			ner.tag();
		}
		
		GimliParser gimli = new GimliParser();
		Document doc = null;
		try {
			FileInputStream file = new FileInputStream(new File(output));
			doc = gimli.parse(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		RelationshipExtractor relationships = new RelationshipExtractor(doc);
		HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> rel = relationships.getProteinDNARelationships();
		doc.setProteinDNARelationships(rel);
		System.out.println(rel);
		rel = relationships.getProteinRNARelationships();
		doc.setProteinRNARelationships(rel);
		System.out.println(rel);
		
		JSONFileWriter jsonWriter = new JSONFileWriter(doc);
		jsonWriter.write(output.substring(0, output.length()-4).concat("json"));
	}
	
}
