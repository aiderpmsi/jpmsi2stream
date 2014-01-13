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

public class Print extends Action {

	private static final long serialVersionUID = -142007029759321069L;

	private String content;
	
	private Boolean newline;

	public Print() {
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
				
		try {
			contentHandler.characters(content.toCharArray(), 0, content.length());
			if (getNewline())
				contentHandler.characters("\n".toCharArray(), 0, 1);
		} catch (SAXException e) {
			throw new ModelException(e);
		}
		
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getNewline() {
		return newline;
	}

	public void setNewline(Boolean newline) {
		this.newline = newline;
	}

}
