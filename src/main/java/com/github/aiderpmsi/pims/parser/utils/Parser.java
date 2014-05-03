package com.github.aiderpmsi.pims.parser.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.model.TransitionTarget;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2Impl;
import org.xml.sax.helpers.XMLFilterImpl;

public class Parser extends XMLFilterImpl {

	// scxml location
	private static final String scxmlLocation = "pims.xml";

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

			// SOURCE OF THE STATE MACHINE DEFINITION
			InputStream scxmlSource = this.getClass().getClassLoader().getResourceAsStream(scxmlLocation);
			// SOURCE OF THE PMSI FILE
			MemoryBufferedReader pmsiSource = new MemoryBufferedReader(input.getCharacterStream());

			ExecutorFactory machineFactory = new ExecutorFactory()
					.setScxmlSource(scxmlSource)
					.setMemoryBufferedReader(pmsiSource)
					.setContentHandler(getContentHandler())
					.setErrorHandler(getErrorHandler());

			SCXMLExecutor machine = machineFactory.createMachine();

			getContentHandler().startDocument();
			getContentHandler().startElement("", "root", "root", new Attributes2Impl());

			// RUN THE STATE MACHINE
			if (getStartState() != null)
				machine.getStateMachine().setInitialTarget((TransitionTarget) machine.getStateMachine().getTargets().get(getStartState()));
			machine.go();
			
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
