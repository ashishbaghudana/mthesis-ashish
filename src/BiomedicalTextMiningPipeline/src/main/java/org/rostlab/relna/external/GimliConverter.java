package org.rostlab.relna.external;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.rostlab.relna.tokenizer.WordTokenizer;;

public class GimliConverter {
	
	private String inputFile;
	private String outputFile;
	private String id;
	private String [] title;
	private String [] paperAbstract;
	private WordTokenizer wordTokenizer;
	
	public GimliConverter (String inputFile, String outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.wordTokenizer = new WordTokenizer();
	}
	
	private void readFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.inputFile));
			id = br.readLine();
			title = this.wordTokenizer.tokenize(br.readLine());
			paperAbstract = this.wordTokenizer.tokenize(br.readLine());
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeFile() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.outputFile));
			bw.write(id);
			bw.write("\n");
			for (String token : title) {
				bw.write(token);
			}
			for (String token : paperAbstract) {
				bw.write(token);
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void convert() {
		this.readFile();
		this.writeFile();
	}

}