package com.github.aiderpmsi.pims.grouper.customtags;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

import com.github.aiderpmsi.pims.grouper.model.RssContent;

public class RssMain extends Action {
	
	private static final long serialVersionUID = 6879554289822402659L;

	private String get, varname, type;
	
	public RssMain() {
		super();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {

		// Gets the rss content
		RssContent rssContent =
				(RssContent) scInstance.getRootContext().get("_rssContent");

		// GETS THE DEMANDED ELEMENT
		String result = rssContent.getRssmain().get(get);

		// CONVERTS THE RESULT
		Object convResult;
		switch (type) {
		case "Integer" :
			try {
				convResult = (result == null || result.trim().length() == 0 ?
						0 : new Integer(result.trim()));
			} catch (NumberFormatException e) {
				throw new ModelException(result + " Is not of type " + type, e);
			}
			break;
		case "String" :
			convResult = result;
			break;
		default:
			throw new ModelException("RssMain unknown type : " + type);
		}
		
		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(varname, convResult);
	}

	public String getGet() {
		return get;
	}

	public void setGet(String get) {
		this.get = get;
	}

	public String getVarname() {
		return varname;
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
