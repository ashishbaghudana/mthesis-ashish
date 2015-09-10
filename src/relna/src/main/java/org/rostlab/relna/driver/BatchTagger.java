package org.rostlab.relna.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.rostlab.relna.annotator.NamedEntityRecognizer;
import org.rostlab.relna.annotator.RelationshipExtractor;
import org.rostlab.relna.corpus.Document;
import org.rostlab.relna.corpus.Phrase;
import org.rostlab.relna.external.GimliConverter;
import org.rostlab.relna.parser.GimliParser;
import org.rostlab.relna.writer.JSONFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchTagger {
	
	private static Logger logger = LoggerFactory.getLogger(NameTaggerRecognizer.class);
	
	public static void main(String [] args) {
		DefaultParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption("h", "help", false, "Print usage information.");
		options.addOption("d", "directory", true, "Directory with corpora to process.");
		
		CommandLine commandLine = null;
		
		String directory = null;
		File gdep = null;
		String output = null;
		
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			logger.error("There was an error parsing the command line arguments.", e);
		}
		
		if (commandLine.hasOption("h")) {
			System.out.println("Help for this module.");
			return;
		}
		
		if (commandLine.hasOption("d")) {
			directory = commandLine.getOptionValue("d");
		} 
		else {
			System.err.println("Please specify the directory.");
			return;
		}
		
		File folder = new File(directory);
		File [] fileList = folder.listFiles();		
		
		for (File file : fileList) {
			GimliConverter gimliConverter = new GimliConverter(file.getAbsolutePath(), FilenameUtils.removeExtension(file.getAbsolutePath())+".iob2");
			gimliConverter.convert();
			gdep = new File(folder, "gdep.gz");
			if (gdep.exists())
				gdep.delete();
			System.out.println(gdep.getAbsolutePath());
			output = FilenameUtils.removeExtension(file.getAbsolutePath())+"_annotated.iob2";
			NamedEntityRecognizer ner = new NamedEntityRecognizer(FilenameUtils.removeExtension(file.getAbsolutePath())+".iob2", gdep.getAbsolutePath(), output);
			ner.tag();
			
			GimliParser gimli = new GimliParser();
			Document doc = null;
			try {
				FileInputStream iobfile = new FileInputStream(new File(output));
				doc = gimli.parse(iobfile);
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
}