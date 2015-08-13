package org.rostlab.relna.annotator;

import pt.ua.tm.gimli.reader.JNLPBAReader;
import pt.ua.tm.gimli.writer.JNLPBAWriter;
import pt.ua.tm.gimli.annotator.Annotator;
import pt.ua.tm.gimli.config.Constants.LabelFormat;
import pt.ua.tm.gimli.config.Constants.Parsing;
import pt.ua.tm.gimli.config.ModelConfig;
import pt.ua.tm.gimli.corpus.Corpus;
import pt.ua.tm.gimli.exception.GimliException;
import pt.ua.tm.gimli.model.CRFModel;
import pt.ua.tm.gimli.processing.*;
import org.rostlab.relna.config.Constants;

public class NamedEntityRecognizer {
	private String corpus;
	private String gdep;
	private String output;
	private JNLPBAReader reader;
	private Corpus c;
	
	public NamedEntityRecognizer(String corpus, String gdep, String output) {
		this.corpus = corpus;
		this.gdep = gdep;
		this.output = output;
		this.reader = new JNLPBAReader(this.corpus, this.gdep);
		this.c = null;
	}
	
	private void convert() {
		try {
			c = reader.read(LabelFormat.BIO);
		} catch (GimliException e) {
			System.err.println("There was an error reading the corpus");
		}
	}
	
	private void annotate() {
		ModelConfig [] mc = new ModelConfig[Constants.FEATURES.length];
		for (int i=0; i<Constants.FEATURES.length; i++) {
			mc[i] = new ModelConfig(Constants.FEATURES[i]);
		}
		
		Parsing[] parsing = {Parsing.BW, Parsing.BW, Parsing.FW};
		
		CRFModel [] crfModels = new CRFModel[Constants.MODELS.length];
		try {
			for (int i=0; i<Constants.MODELS.length; i++) {
				crfModels[i] = new CRFModel(mc[i], parsing[i], Constants.MODELS[i]);
			}
		} catch (GimliException e) {
			System.err.println("There was a problem loading the model(s)");
			return;
		}
		Annotator a = new Annotator(c);
		a.annotate(crfModels);
		
		Parentheses.processRemoving(c);
		Abbreviation.process(c);
	}
	
	private void write() {
		JNLPBAWriter writer = new JNLPBAWriter();
		try {
			writer.write(c, output);
		} catch (GimliException e) {
			System.err.println("There was a problem writing the corpus to file");
		}
	}
	
	public void tag() {
		this.convert();
		this.annotate();
		this.write();
	}
	
	public static void main(String [] args) {
		String corpus = "src/main/resources/sample/abstract/new.iob2";
		String gdep = "src/main/resources/sample/abstract/gdep_new.gz";
		String output = "src/main/resources/sample/abstract/output_new.iob2";
		NamedEntityRecognizer ner = new NamedEntityRecognizer(corpus, gdep, output);
		ner.tag();
	}
}
