package org.rostlab.relna.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
import java.util.UUID;

public class NameTaggerRecognizer {
	
	private static Logger logger = LoggerFactory.getLogger(NameTaggerRecognizer.class);
	
	public static void main(String [] args) {
		DefaultParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption("h", "help", false, "Print usage information.");
		options.addOption("o", "output", true, "File to save the final output.");
		
		Option input = new Option("i", "input", true, "File with the corpus.");
		Option readerOption = new Option("r", "reader format", true, "Input file format - txt or iob2");
		Option writerOption = new Option("w", "writer format", true, "Output file format - anndoc or json");
		Option string = new Option("s", "string text", true, "Command line input text");
		
		input.setRequired(false);
		readerOption.setRequired(false);
		string.setRequired(false);
		writerOption.setRequired(true);
		
		options.addOption(input);
		options.addOption(readerOption);
		options.addOption(writerOption);
		options.addOption(string);
		
		CommandLine commandLine = null;
		
		String inputCorpus = null;
		String gdep = null;
		String gimliOutput = null;
		String output = null;
		String readerFormat = null;
		String writerFormat = null;
		String commandLineText = null;
		
		File tempDir = new File("tmp");
		tempDir.mkdirs();
		
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			logger.error("There was an error parsing the command line arguments.", e);
		}
		
		if (commandLine.hasOption("h")) {
			System.out.println("Help for this module.");
			return;
		}
		
		if (commandLine.hasOption("r") && !commandLine.hasOption("i")) {
			readerFormat = commandLine.getOptionValue("r");
			if (readerFormat.equals("string")) {
				if (commandLine.hasOption("s")) {
					commandLineText = commandLine.getOptionValue("s");
				}
				else {
					System.err.println("Please specify sentence to tag from command line");
				}
			}
			else {
				System.err.println("Please specify valid parameters, input corpus is not given.");
			}
		}
		else if (commandLine.hasOption("r") && commandLine.hasOption("i")) {
			readerFormat = commandLine.getOptionValue("r");
			inputCorpus = commandLine.getOptionValue("i");
		}
		else if (commandLine.hasOption("i") && !commandLine.hasOption("r")) {
			inputCorpus = commandLine.getOptionValue("i");
			readerFormat = FilenameUtils.getExtension(inputCorpus);
		}
		else {
			System.err.println("Please specify at least one of the parameters -i or -r");
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
		
		gimliOutput = tempDir.getAbsolutePath() + File.separator + FilenameUtils.removeExtension(FilenameUtils.getBaseName(output)) + ".iob2";
		
		if (readerFormat.equals("iob2")) {
			gdep = tempDir.getAbsolutePath() + File.separator + "gdep.gz";
			
			File gdep_file = new File(gdep);
			if (gdep_file.exists())
				gdep_file.delete();
			NamedEntityRecognizer ner = new NamedEntityRecognizer(inputCorpus, gdep, gimliOutput);
			ner.tag();
		}
		else if (readerFormat.equals("txt")) {
			File tempFile = new File(tempDir.getAbsolutePath()+File.separator+"tempCorpus"+Long.toString(System.nanoTime())+".iob2");
			GimliConverter gimliConverter = new GimliConverter(inputCorpus, tempFile.getAbsolutePath());
			gimliConverter.convert();
			
			gdep = tempDir.getAbsolutePath() + File.separator + "gdep.gz";
			
			File gdep_file = new File(gdep);
			if (gdep_file.exists())
				gdep_file.delete();
			
			NamedEntityRecognizer ner = new NamedEntityRecognizer(tempFile.getAbsolutePath(), gdep, gimliOutput);
			ner.tag();
		}
		else if (readerFormat.equals("string")) {
			File tempFile;
			File tempCorpus;
			try {				
				tempCorpus = new File(tempDir.getAbsolutePath()+File.separator+"corpus"+Long.toString(System.nanoTime())+".txt");				
				FileWriter fw = new FileWriter(tempCorpus);
				
				fw.write("###MEDLINE:"+UUID.randomUUID().toString().replace("-", "")+"\n");
				fw.write("Title.\n");
				fw.write(commandLineText);
				
				fw.close();
				
				tempFile = new File(tempDir.getAbsolutePath()+File.separator+"tempCorpus"+Long.toString(System.nanoTime())+".iob2");
				GimliConverter gimliConverter = new GimliConverter(tempCorpus.getAbsolutePath(), tempFile.getAbsolutePath());
				gimliConverter.convert();
				
				gdep = tempDir.getAbsolutePath() + File.separator + "gdep.gz";
				
				File gdep_file = new File(gdep);
				if (gdep_file.exists())
					gdep_file.delete();
				
				NamedEntityRecognizer ner = new NamedEntityRecognizer(tempFile.getAbsolutePath(), gdep, gimliOutput);
				ner.tag();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			System.err.println("Please specify valid reader format - iob2, txt or string");
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
		
		RelationshipExtractor relationships = new DictionaryBasedRelationshipExtractor(doc);
		HashMap<Integer, HashMap<Integer, ArrayList<ArrayList<Phrase>>>> rel = relationships.getProteinDNARelationships();
		doc.setProteinDNARelationships(rel);
		rel = relationships.getProteinRNARelationships();
		doc.setProteinRNARelationships(rel);
		doc.prettyPrintRelationships();
	
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