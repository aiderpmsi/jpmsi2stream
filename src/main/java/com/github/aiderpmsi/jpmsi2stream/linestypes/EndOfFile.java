package com.github.aiderpmsi.jpmsi2stream.linestypes;

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.github.aiderpmsi.jpmi2stream.MemoryBufferedReader;

public class EndOfFile implements PmsiLineType {

	public boolean isFound(MemoryBufferedReader br) throws IOException {
		if (br.getLine() == null)
			return true;
		else
			return false;
	}

	public void writeResults(ContentHandler contentHandler) throws IOException {
		try {
			contentHandler.startElement("", "eof", "eof", null);
			contentHandler.endElement("",  "eof",  "eof");
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}

	public void consume(MemoryBufferedReader br) throws IOException {
		// Nothing to do
	}

}
