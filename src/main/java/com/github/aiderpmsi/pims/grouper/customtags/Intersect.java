package com.github.aiderpmsi.pims.grouper.customtags;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

public class Intersect extends Action {

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
		HashSet<String> resultHash = null;
		
		for (String value : valuesArray) {
			// GETS THE OBJECT WITH VALUE
			Object valueContent = scInstance.getContext(getParentTransitionTarget()).get(value.trim());
			
			// LIST WE HAVE TO CHECK AGAINST
			Collection<String> toRetain = null;
			
			// VALUE IS NULL
			if (valueContent == null) {
				// DO NOTHING
			}
			
			// VALUE IS A STRING
			else if (valueContent instanceof String) {
				if (resultHash == null) {
					resultHash = new HashSet<>(1);
					resultHash.add((String) valueContent);
					continue;
				} else {
					toRetain = new LinkedList<>();
					toRetain.add((String) valueContent);
				}
			}
			
			// VALUE IS A COLLECTION
			else if (valueContent instanceof Collection<?>) {
				if (resultHash == null) {
					resultHash = new HashSet<>();
					for (Object valueObject : (Collection<Object>) valueContent) {
						if (valueObject instanceof String) {
							resultHash.add((String) valueObject);
						}
					}
					continue;
				} else {
					toRetain = new LinkedList<>();
					for (Object valueObject : (Collection<Object>) valueContent) {
						if (valueObject instanceof String) {
							toRetain.add((String) valueObject);
						}
					}
				}
			} else {
				throw new ModelException(value + " is not a known type for Intersect");
			}
			
			// MAKE THE INTERSECTION
			resultHash.retainAll(toRetain);
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
