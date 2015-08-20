package org.rostlab.relna.external;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GimliConverter {
	
	private String inputFile;
	private String outputFile;
	private String id;
	private String title;
	private String paperAbstract;
	
	public GimliConverter(String inputFile, String outputFile) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}
	
	private void readFile() {
		try {
			BufferedReader bw = new BufferedReader(new FileReader(this.inputFile));
			id = bw.readLine();
			title = bw.readLine();
			paperAbstract = bw.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void convert() {
		
	}

}
