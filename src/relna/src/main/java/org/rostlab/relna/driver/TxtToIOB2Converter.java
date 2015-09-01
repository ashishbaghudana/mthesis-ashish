package org.rostlab.relna.driver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.rostlab.relna.external.GimliConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtToIOB2Converter {
	private static Logger logger = LoggerFactory.getLogger(TxtToIOB2Converter.class);
	
	public static void main(String [] args) {
		
		DefaultParser parser = new DefaultParser();
		Options options = new Options();
		options.addOption("h", "help", false, "Print usage information.");
		options.addOption("c", "corpus", true, "File with the corpus.");
		options.addOption("o", "output", true, "File to save the final output.");
		
		CommandLine commandLine = null;
		
		String corpus = null;
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
		
		if (commandLine.hasOption("c")) {
			corpus = commandLine.getOptionValue("c");
		} 
		else {
			System.err.println("Please specify the corpus to convert.");
			return;
		}
		
		if (commandLine.hasOption("o")) {
			output = commandLine.getOptionValue("o");
		}
		else {
			System.err.println("Please specify output file.");
			return;
		}
		
		GimliConverter gimliConverter = new GimliConverter(corpus, output);
		gimliConverter.convert();
	}
}
