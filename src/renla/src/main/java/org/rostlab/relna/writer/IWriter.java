package org.rostlab.relna.writer;

import java.io.Writer;

public interface IWriter {
	public void write(String file);
	public void write(Writer writer);
}
