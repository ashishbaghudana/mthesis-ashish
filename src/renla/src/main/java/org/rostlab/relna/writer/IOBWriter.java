package org.rostlab.relna.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.Writer;

import org.rostlab.relna.corpus.*;

public class IOBWriter implements IWriter {
	private Document document;
	private String newLine = "\n";
	
	public IOBWriter(Document document) {
		this.document = document;
	}

	public void write(String file) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file)));
			writeToFile(bw);
		} catch (IOException e) {
			System.err.println("Could not write document to file");
		}
	}

	public void write(Writer writer) {
		try {
			BufferedWriter bw = new BufferedWriter(writer);
			writeToFile(bw);
		} catch (IOException e) {
			System.err.println("Could not write document to file");
		}
	}
	
	private void writeToFile(BufferedWriter bw) throws IOException {
		bw.write(this.document.documentId);
		bw.write(newLine);
		for (int i=0; i<this.document.size(); i++) {
			for (int j=0; j<this.document.getSection(i).size(); j++) {
				Sentence sent = this.document.getSection(i).getSentence(j);
				for (Token word : sent.getWords()) {
					bw.write(word.tokenText+"\t"+word.getTokenTag()+"\n");
				}
				bw.write(newLine);
			}
		}
	}
}
