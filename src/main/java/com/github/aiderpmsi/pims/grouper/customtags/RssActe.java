package com.github.aiderpmsi.pims.grouper.customtags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

import com.github.aiderpmsi.pims.grouper.model.RssContent;

public class RssActe extends Action {
	
	private static final long serialVersionUID = -7371586408853556556L;

	private String get, varname;

	public RssActe() {
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

		// GETS THE LIST OF DEMANDED ELEMENT
		List<String> results = new ArrayList<>();
		for (HashMap<String, String> element : rssContent.getRssacte()) {
			if (element.containsKey(get))
				results.add(element.get(get));
		}

		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(varname, results);
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
}
