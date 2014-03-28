package com.github.aiderpmsi.pims.utils;

import java.io.IOException;

import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.model.ModelException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.XMLFilterImpl;

public class Parser extends XMLFilterImpl {

	// scxml location
	private static final String scxmlLocation = "/pims.xml";

	/**
	 * starting state
	 */
	private String startState = null;
	
	@Override
	public void parse(InputSource input) throws SAXException, IOException {

		try {
			// THIS WORKS ONLY ON CHARACTER STREAMS
			if (input.getCharacterStream() == null)
				throw new IOException("No CharacterStream in input");

			ExecutorFactory machineFactory = new ExecutorFactory()
					.setScxmlSource(
							getClass().getResourceAsStream(scxmlLocation))
					// IF SCXML CANT BE OPENED, SEND RUNTIME EXCEPTION
					.setErrorHandler(new DefaultHandler2())
					.setMemoryBufferedReader(new MemoryBufferedReader(input.getCharacterStream()))
					.setContentHandler(getContentHandler());

			SCXMLExecutor machine = machineFactory.createMachine();

			getContentHandler().startDocument();
			getContentHandler().startElement("", "root", "root", new Attributes2Impl());

			// RUN THE STATE MACHINE
			if (startState != null)
				machine.getStateMachine().setInitial(getStartState());
			machine.go();
			
			// GET THE LAST STATE : IF WE HAVE HEADER NOT FOUND OR LINE NOT KNOWN,
			// CREATE AN ERROR
			machine.getCurrentStatus();
			
			getContentHandler().endElement("", "root", "root");
			getContentHandler().endDocument();

		} catch (ModelException e) {
			throw new IOException(e);
		}
	}

	public String getStartState() {
		return startState;
	}

	public void setStartState(String startState) {
		this.startState = startState;
	}

}
