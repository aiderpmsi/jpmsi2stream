package com.github.aiderpmsi.pims.utils;

import java.io.IOException;
import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.model.ModelException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLFilterImpl;

public class Parser extends XMLFilterImpl {

	// scxml location
	private static final String scxmlLocation = "/pims.xml";

	@Override
	public void parse(InputSource input)  throws SAXException, IOException {

		try {
			ExecutorFactory machineFactory = new ExecutorFactory()
					.setScxmlSource(
							getClass().getResourceAsStream(scxmlLocation))
					// If scxml can't be opened, runtime exception
					.setErrorHandler(new DefaultHandler())
					.setMemoryBufferedReader(
							new MemoryBufferedReader(input.getCharacterStream()))
					.setContentHandler(getContentHandler());

			SCXMLExecutor machine = machineFactory.createMachine();
			machine.go(); // If exception, error in model or something else, it
							// is a runtime error
		} catch (ModelException e) {
			throw new IOException(e);
		}
	}

}
