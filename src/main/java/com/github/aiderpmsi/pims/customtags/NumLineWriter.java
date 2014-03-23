package com.github.aiderpmsi.pims.customtags;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2Impl;

public class NumLineWriter extends Action {

	private static final long serialVersionUID = 7940029301826823425L;

	public NumLineWriter() {
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

		// Gets The line number
		String numline =
				scInstance.getRootContext().get("numline").toString();

		try {
			contentHandler.startElement("", "numline", "numline", new Attributes2Impl());
			contentHandler.characters(numline.toCharArray(), 0, numline.length());
			contentHandler.endElement("",  "numline", "numline");
		} catch (SAXException e) {
			throw new ModelException(e);
		}
		
	}
}
