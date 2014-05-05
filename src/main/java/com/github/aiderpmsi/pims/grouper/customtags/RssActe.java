package com.github.aiderpmsi.pims.grouper.customtags;

import java.text.MessageFormat;
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

	private String retrieve, pattern, destination;

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

		// GETS THE DEMANDED KEYS IN RETRIEVE AND TRIM THEM
		String[] keys = retrieve.split(",");
		for (int i = 0 ; i < keys.length ; i++) {
			keys[i] = keys[i].trim();
		}
		
		// GETS THE LIST OF DEMANDED ELEMENT
		List<String[]> valueslist = new ArrayList<>(rssContent.getRssacte().size());
		for (HashMap<String, String> element : rssContent.getRssacte()) {
			String[] values = new String[keys.length];
			for (int i = 0 ; i < keys.length ; i++) {
				// CONSIDER THE CASE WHERE THE KEY DOESN'T EXIST OR VALUE IS NULL
				String value = element.get(keys[i]);
				values[i] = (value == null ? "" : value.trim()); 
			}
			valueslist.add(values);
		}
		
		// REFORMAT THESE VALUES
		MessageFormat mf = new MessageFormat(pattern);
		List<String> formattedResults = new ArrayList<>(valueslist.size());
		for (String[] values : valueslist) {
			formattedResults.add(mf.format(values));
		}

		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(destination, formattedResults);
	}

	public String getRetrieve() {
		return retrieve;
	}

	public void setRetrieve(String retrieve) {
		this.retrieve = retrieve;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}


}
