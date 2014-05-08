package com.github.aiderpmsi.pims.grouper.customtags;

import java.util.ArrayList;
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

public class Intersect extends Action {

	private static final long serialVersionUID = 6123109652537111804L;

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
			
			// TRANSFORM THIS VALUE TO A LIST OF STRINGS
			List<Object> valueContentList;
			
			
			// VALUE IS NULL
			if (valueContent == null) {
				// WE WON'T HAVE ANY VALUE
				resultHash = new HashSet<>(0);
				break;
			}
			// VALUE IS A STRING
			else if (valueContent instanceof String) {
				valueContentList = new ArrayList<>(1);
				valueContentList.add((String) valueContent);
			}
			
			// VALUE IS A LIST
			else if (valueContent instanceof List<?>) {
				valueContentList = (List<Object>) valueContent;
			}
			else {
				throw new ModelException(value + " is not a known type for Intersect");
			}
			
			// IF RESULT IS NOT DEFINED, CREATE IT, ELSE MAKE THE INTERSECTION
			if (resultHash == null) {
				resultHash = new HashSet<>();
				for (Object valueList : valueContentList) {
					if (valueList instanceof String)
						resultHash.add((String) valueList);
				}
			} else {
				resultHash.retainAll(valueContentList);
			}
			
			// IF RESULT HAS NO ITEM, LEAVE
			if (resultHash.size() == 0)
				break;
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
