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

public class ToRss extends Action {
	
	private static final long serialVersionUID = 1108040760319004481L;

	private String set, value;

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {

		// Gets the rss content
		RssContent rssContent =
				(RssContent) scInstance.getRootContext().get("_rssContent");

		// GETS THE DEMANDED KEYS IN RETRIEVE AND TRIM THEM
		String[] keys = set.split(",");
		for (int i = 0 ; i < keys.length ; i++) {
			keys[i] = keys[i].trim();
		}
		// GETS THE DEMANDED VALUES AND TRIM THEM
		String[] values = value.split(",");
		for (int i = 0 ; i < keys.length ; i++) {
			values[i] = values[i].trim();
		}

		if (values.length != keys.length)
			throw new ModelException("Number of sets and values are different : set = " + set + " ; values = " + value);
		
		// FOR EACH KEY, SET THE CORRESPONDING VALUE
		for (int i = 0 ; i < keys.length && i < values.length ; i++) {
			rssContent.getRssmain().put(keys[i], (String) scInstance.getContext(getParentTransitionTarget()).get(values[i]));
		}
	}

	public String getSet() {
		return set;
	}

	public void setSet(String set) {
		this.set = set;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	
}
