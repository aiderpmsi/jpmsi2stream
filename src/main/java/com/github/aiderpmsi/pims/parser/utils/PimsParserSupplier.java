package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;

public interface PimsParserSupplier {

	/**
	 * Reads a line, without removing it from the stream
	 * @return
	 * @throws IOException
	 */
	public String readLine() throws IOException;
	
	/**
	 * Removes a fixed number of elements from the stream
	 * @param nbElts
	 */
	public void consume(int nbElts) throws IOException;	
	
}
