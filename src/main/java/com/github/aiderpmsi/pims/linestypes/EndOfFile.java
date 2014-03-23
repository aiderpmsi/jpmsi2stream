package com.github.aiderpmsi.pims.linestypes;

import java.io.IOException;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2Impl;

import com.github.aiderpmsi.pims.utils.MemoryBufferedReader;

public class EndOfFile extends PmsiLineType {

	public boolean isFound(MemoryBufferedReader br) throws IOException {
		if (br.getLine() == null)
			return true;
		else
			return false;
	}

	public void writeResults(ContentHandler contentHandler) throws IOException {
		try {
			contentHandler.startElement("", "eof", "eof", new Attributes2Impl());
			contentHandler.endElement("",  "eof",  "eof");
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}

	public void consume(MemoryBufferedReader br) throws IOException {
		// Nothing to do
	}

}
