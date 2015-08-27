package org.rostlab.relna.tokenizer;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceDetectorME;

public class SentenceTokenizer implements Tokenizer {
	
	private SentenceModel model;
	private InputStream modelIn;
	private SentenceDetectorME sentenceDetector;
	
	public SentenceTokenizer(File inputModel) {
		try {
			modelIn = new FileInputStream(inputModel);
			model = new SentenceModel(modelIn);
			sentenceDetector = new SentenceDetectorME(model);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn!=null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String[] tokenize(String s) {
		return sentenceDetector.sentDetect(s);
	}
}
