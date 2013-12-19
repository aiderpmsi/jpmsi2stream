package com.github.aiderpmsi.jpmi2stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MemoryBufferedReader extends BufferedReader {

	private String readed = "";
	
	public MemoryBufferedReader(Reader in) {
		super(in);
	}

	public MemoryBufferedReader(Reader in, int sz) {
		super(in, sz);
	}

	public String getLine() throws IOException {
		if (readed.length() == 0) {
			readed = readLine();
		}
		return readed;
	}
	
	public void consume(int nbElts) {
		readed = readed.substring(nbElts);
	}
	
}
