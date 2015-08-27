package org.rostlab.relna.parser;

import org.rostlab.relna.corpus.Document;
import java.io.InputStream;
import java.io.IOException;

public interface Parser {
	
	public Document parse(InputStream is) throws IOException;

}
