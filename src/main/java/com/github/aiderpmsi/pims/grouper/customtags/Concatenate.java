package com.github.aiderpmsi.pims.grouper.customtags;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

public class Concatenate extends Action {

	private static final long serialVersionUID = 3615764243955863213L;

	private String values, result;
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {
		
		// GETS ALL VALUES NAMES FROM VALUES
		String[] valuesArray = values.split(",");
		
		// SETS THE RESULT HASH
		HashSet<String> resultHash = new HashSet<>();
		
		for (String value : valuesArray) {
			// GETS THE OBJECT WITH VALUE
			Object valueContent = scInstance.getContext(getParentTransitionTarget()).get(value.trim());
			
			// VALUE IS NULL
			if (valueContent == null) {
				// DO NOTHING
			}
			
			// VALUE IS A STRING
			else if (valueContent instanceof String) {
				resultHash.add((String) valueContent);
			}
			
			// VALUE IS A LIST
			else if (valueContent instanceof List<?>) {
				for (Object valueObject : (List<Object>) valueContent) {
					if (valueObject instanceof String) {
						resultHash.add((String) valueObject);
					}
				}
			}
			else {
				throw new ModelException(value + " is not a known type for Intersect");
			}
		}
		
		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(result, resultHash);
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
