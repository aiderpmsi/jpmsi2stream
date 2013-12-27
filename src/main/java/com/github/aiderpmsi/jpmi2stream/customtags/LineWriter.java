package com.github.aiderpmsi.jpmi2stream.customtags;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;
import org.xml.sax.ContentHandler;

import aider.org.pmsi.parser.linestypes.PmsiLineType;

public class LineWriter extends Action {
	
	private static final long serialVersionUID = -8154241482062171428L;
	
	private String lineName;

	public LineWriter() {
		super();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {
		
		// Gets The content Handler
		ContentHandler contentHandler =
				(ContentHandler) scInstance.getRootContext().get("_contenthandler");

		// Gets the line definition
		PmsiLineType line = 
				(PmsiLineType) scInstance.getRootContext().get(lineName);
		
		// Writes the result in the content handler
		try {
			line.writeResults(contentHandler);
		} catch (IOException e) {
			throw new ModelException(e);
		}
		
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

}
