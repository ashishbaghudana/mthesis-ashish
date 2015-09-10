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
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.rostlab.relna.annotator.*;
import org.rostlab.relna.corpus.Document;
import org.rostlab.relna.corpus.Phrase;
import org.rostlab.relna.parser.GimliParser;
import org.rostlab.relna.external.GimliConverter;
import org.rostlab.relna.writer.AnndocWriter;
import org.rostlab.relna.writer.JSONFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.io.*;

public class NameTaggerRecognizer {
	
	private static Logger logger = LoggerFactory.getLogger(NameTaggerRecognizer.class);
	
	public static void main(String [] args) {
		DefaultParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption("h", "help", false, "Print usage information.");
		options.addOption("i", "input", true, "File with the corpus.");
		options.addOption("o", "output", true, "File to save the final output.");
		
		Option readerOption = new Option("r", "reader format", true, "Input file format - txt or iob2");
		Option writerOption = new Option("w", "writer format", true, "Output file format - anndoc or json");
		
		readerOption.setRequired(false);
		writerOption.setRequired(true);
		
		options.addOption(readerOption);
		options.addOption(writerOption);
		
		CommandLine commandLine = null;
		
		String inputCorpus = null;
		String gdep = null;
		String gimliOutput = null;
		String output = null;
		String readerFormat = null;
		String writerFormat = null;
		
		File tempDir = null;
		
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			logger.error("There was an error parsing the command line arguments.", e);
		}
		
		if (commandLine.hasOption("h")) {
			System.out.println("Help for this module.");
			return;
		}
		
		if (commandLine.hasOption("i")) {
			inputCorpus = commandLine.getOptionValue("i");
		} 
		else {
			System.err.println("Please specify the corpus.");
			return;
		}
		
		if (commandLine.hasOption("r")) {
			readerFormat = commandLine.getOptionValue("r");
		}
		else {
			readerFormat = FilenameUtils.getExtension(inputCorpus);
		}
		
		if (commandLine.hasOption("o")) {
			output = commandLine.getOptionValue("o");
		} 
		else {
			System.err.println("Please specify the file to save the output.");
			return;
		}
		
		if (commandLine.hasOption("w")) {
			writerFormat = commandLine.getOptionValue("w");
			if (!writerFormat.equals("json") && !writerFormat.equals("anndoc")) {
				System.err.println("Please specify valid output format - json or anndoc.");
				return;
			}
		}
		else {
			System.err.println("Please specify the output format.");
			return;
		}
		
		tempDir = Files.createTempDir();
		gimliOutput = tempDir.getAbsolutePath() + File.separator + FilenameUtils.removeExtension(FilenameUtils.getBaseName(output)) + ".iob2";
		
		if (readerFormat.equals("iob2")) {
			gdep = tempDir.getAbsolutePath() + File.pathSeparator + "gdep.gz";
			
			File gdep_file = new File(gdep);
			if (gdep_file.exists())
				gdep_file.delete();
			NamedEntityRecognizer ner = new NamedEntityRecognizer(inputCorpus, gdep, gimliOutput);
			ner.tag();
		}
		else {
			try {
				File tempFile = File.createTempFile(FilenameUtils.removeExtension(inputCorpus), ".iob2");
				GimliConverter gimliConverter = new GimliConverter(inputCorpus, tempFile.getAbsolutePath());
				gimliConverter.convert();
				
				gdep = tempDir.getAbsolutePath() + File.pathSeparator + "gdep.gz";
				
				File gdep_file = new File(gdep);
				if (gdep_file.exists())
					gdep_file.delete();
				
				NamedEntityRecognizer ner = new NamedEntityRecognizer(FilenameUtils.removeExtension(inputCorpus)+".iob2", gdep, gimliOutput);
				ner.tag();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		GimliParser gimli = new GimliParser();
		Document doc = null;
		try {
			FileInputStream file = new FileInputStream(new File(gimliOutput));
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
		
		if (writerFormat.equals("json")) {
			JSONFileWriter jsonWriter = new JSONFileWriter(doc);
			jsonWriter.write(output);
		}
		
		else if (writerFormat.equals("anndoc")) {
			AnndocWriter anndocWriter = new AnndocWriter(doc);
			anndocWriter.write(output);
		}
		
		for (File f : tempDir.listFiles())
			f.delete();
		tempDir.delete();
	}
}