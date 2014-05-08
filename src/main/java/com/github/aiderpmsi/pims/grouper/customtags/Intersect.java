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
			
			// TRANSFORM THIS VALUE TO A HASHSET OF STRINGS
			HashSet<String> valueContentHash;
			
			// VALUE IS A STRING
			if (valueContent instanceof String) {
				valueContentHash = new HashSet<>(1);
				valueContentHash.add((String) valueContent);
			}
			// VALUE IS A HASHSET
			else if (valueContent instanceof HashSet<?>) {
				Boolean allString = true;
				// CHECK IF WE HAVE ONLY STRINGS OR NO
				for (Object element : (HashSet<?>) valueContent) {
					if (!(element instanceof String)) {
						allString = false;
						break;
					}
				}
				// COPY THE HASH IF WE HAVE ONLY STRINGS
				if (allString) {
					valueContentHash = (HashSet<String>) ((HashSet<String>) valueContent).clone();
				}
				// ELSE CREATE NEW HASH
				else {
					valueContentHash = new HashSet<String>();
					for (Object element : (HashSet<?>) valueContent) {
						if (element instanceof String)
							valueContentHash.add((String) element);
					}
				}
			}
			// VALUE IS A LIST
			else if (valueContent instanceof List<?>) {
				valueContentHash = new HashSet<>();
				for (Object element : (List<?>) valueContent) {
					if (element instanceof String)
						valueContentHash.add((String) element);
				}
			}
			else {
				throw new ModelException(value + " is not a known type for Intersect");
			}
			
			// IF RESULT IS NOT DEFINED, CREATE IT, ELSE MAKE THE INTERSECTION
			if (resultHash == null) {
				resultHash = valueContentHash;
			} else {
				resultHash.retainAll(valueContentHash);
			}
			
			// IF RESULT HAS NO ITEM, LEAVE
			if (resultHash.size() == 0)
				break;
		}

		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(result, resultHash);
	}

}
