package com.github.aiderpmsi.pims.customtags;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ErrorInvocator extends Action {

	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = 8231797397462666473L;
	
	private String error;
	
	private String numLineVar;

	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, @SuppressWarnings("rawtypes") Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {
		
		// GETS THE ERROR HANDLER
		ErrorHandler errH = 
				((ErrorHandler) scInstance.getRootContext().get("_errorhandler"));
		
		// ADDS THE ERROR AND LINE INVOCATOR
		try {
			errH.error(new SAXParseException(error, "scxml", "scxml", new Integer(scInstance.getRootContext().get("numline").toString()), 0));
		} catch (SAXException e) {
			throw new SCXMLExpressionException(e);
		}
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getNumLineVar() {
		return numLineVar;
	}

	public void setNumLineVar(String numLineVar) {
		this.numLineVar = numLineVar;
	}

}
