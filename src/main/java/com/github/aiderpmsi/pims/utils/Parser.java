package com.github.aiderpmsi.pims.utils;

import java.io.Reader;

import org.apache.commons.scxml.SCXMLExecutor;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

public class Parser {

	// Stream to read
	private Reader inStream = null;

	// Content handler
	private ContentHandler contentHandler = null;

	// scxml location
	private String scxmlLocation = "/pims.xml";

	public Parser(Reader inStream, ContentHandler contentHandler) {
		this.inStream = inStream;
		this.contentHandler = contentHandler;
	}

	public void parse() {

		try {
			ExecutorFactory machineFactory = new ExecutorFactory()
					.setScxmlSource(
							getClass().getResourceAsStream(scxmlLocation))
					// If scxml can't be opened, runtime exception
					.setErrorHandler(new DefaultHandler())
					.setMemoryBufferedReader(new MemoryBufferedReader(inStream))
					.setContentHandler(contentHandler);

			SCXMLExecutor machine = machineFactory.createMachine();
			machine.go(); // If exception, error in model or something else, it
							// is a runtime error
		} catch (Exception e) {
			throw new RuntimeException(e);
			
		}
	}

}
