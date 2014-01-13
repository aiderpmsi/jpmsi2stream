package com.github.aiderpmsi.pims.utils;

import java.io.Reader;
import java.net.URL;
import org.apache.commons.scxml.SCXMLExecutor;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

public class Parser {

	// Stream to read
	private Reader inStream = null;

	// Content handler
	private ContentHandler contentHandler = null;

	// scxml location
	private String scxmlLocation = "classpath:pims.xml";

	public Parser(Reader inStream, ContentHandler contentHandler) {
		this.inStream = inStream;
		this.contentHandler = contentHandler;
	}

	public void parse() {

		try {
			URL scxmlURL = new URL(null, scxmlLocation, new ClasspathHandler(
					ClassLoader.getSystemClassLoader())); // If scxml can't be
															// opened, runtime
															// exception

			ExecutorFactory machineFactory = new ExecutorFactory()
					.setScxmlDocument(scxmlURL)
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
