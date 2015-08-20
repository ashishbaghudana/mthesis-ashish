package org.rostlab.relna.annotator;

import pt.ua.tm.gimli.reader.JNLPBAReader;
import pt.ua.tm.gimli.writer.JNLPBAWriter;
import pt.ua.tm.gimli.annotator.Annotator;
import pt.ua.tm.gimli.config.Constants.EntityType;
import pt.ua.tm.gimli.config.Constants.LabelFormat;
import pt.ua.tm.gimli.config.Constants.Parsing;
import pt.ua.tm.gimli.config.ModelConfig;
import pt.ua.tm.gimli.corpus.Corpus;
import pt.ua.tm.gimli.exception.GimliException;
import pt.ua.tm.gimli.model.CRFModel;
import pt.ua.tm.gimli.processing.*;

import java.util.ArrayList;

import org.rostlab.relna.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamedEntityRecognizer {
	private String corpus;
	private String gdep;
	private String output;
	private String corpusOut;
	private JNLPBAReader reader;
	private Corpus c;
	
	private static Logger logger = LoggerFactory.getLogger(NamedEntityRecognizer.class);
	
	public NamedEntityRecognizer(String corpus, String gdep, String output) {
		this.corpus = corpus;
		this.gdep = gdep;
		this.output = output;
		this.reader = new JNLPBAReader(this.corpus, this.gdep);
		this.c = null;
		this.corpusOut = null;
	}
	
	private void convert() {
		try {
			c = reader.read(LabelFormat.BIO);
			corpusOut = output.substring(0, output.length()-4).concat("gz");
			c.writeToFile(corpusOut);
		} catch (GimliException e) {
			System.err.println("There was an error reading the corpus");
		}
	}
	
	private void annotate() {
		
		corpusOut = output.substring(0, output.length()-4).concat("gz");
		
		ModelConfig [] mc = new ModelConfig[Constants.FEATURES.length];
		for (int i=0; i<Constants.FEATURES.length; i++) {
			mc[i] = new ModelConfig(Constants.FEATURES[i]);
		}
		
		ArrayList<EntityType> uniqueEntities = new ArrayList<EntityType>();
		uniqueEntities.add(EntityType.protein);
		uniqueEntities.add(EntityType.DNA);
		uniqueEntities.add(EntityType.RNA);
		
		Parsing[] parsing = {Parsing.FW, Parsing.FW, Parsing.FW};
		
		Corpus [] corp = new Corpus [uniqueEntities.size()];
		try {
			for (int i = 0; i < uniqueEntities.size(); i++) {
				corp[i] = new Corpus(LabelFormat.BIO, uniqueEntities.get(i), corpusOut);
			}
		} catch (GimliException ex) {
			logger.error("There was a problem loading the corpus.", ex);
			return;
		}
		
		for (int j = 0; j < uniqueEntities.size(); j++) {
			
			CRFModel crfModel;
			try {
				crfModel = new CRFModel(mc[j], parsing[j], Constants.MODELS[j]);
			} catch (GimliException e) {
				logger.error("There was a problem loading a model.", e);
				return;
			}
			
            Annotator a = new Annotator(corp[j]);
            logger.info("Annotating the corpus using 1 model for {}...", uniqueEntities.get(j));
            a.annotate(crfModel);
        }
		
		for (int i = 0; i < corp.length; i++) {
            Parentheses.processRemoving(corp[i]);
            Abbreviation.process(corp[i]);
        }
		
		logger.info("Writing the corpus in the JNLPBA format...");
		
		JNLPBAWriter writer = new JNLPBAWriter();
		try {
			writer.write(corp, output);
		} catch (GimliException e) {
			logger.error("There was a problem writing the corpus to file", e);
		}
	}
	
	public void tag() {
		this.convert();
		this.annotate();
	}
}
