package org.rostlab.relna.tokenizer;

public class WordTokenizer implements Tokenizer {
	
	public String [] tokenize(String text) {
		String [] tokenizedString = null;
		text = text.replace(" ", "@tok@");
		text = text.replace(".", "@tok@.");
		text = text.replace(",", "@tok@,");
		text = text.replace(":", "@tok@:");
		text = text.replace(";", "@tok@;");
		text = text.replace("(", "(@tok@");
		text = text.replace(")", "@tok@)");
		text = text.replace("-", "-@tok@");
		tokenizedString = text.split("@tok@");
		return tokenizedString;
	}
	
	public static void main(String [] args) {
		WordTokenizer tokenizer = new WordTokenizer();
		String [] tokenized = tokenizer.tokenize("IL2 gene is responsible-a and b: hello (cya).");
		for (String token : tokenized) {
			System.out.println(token);
		}
	}
}
