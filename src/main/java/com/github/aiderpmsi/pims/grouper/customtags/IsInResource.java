package com.github.aiderpmsi.pims.grouper.customtags;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

import com.github.aiderpmsi.pims.grouper.model.Resource;
import com.github.aiderpmsi.pims.grouper.model.UnclassifiedDictionary;

public class IsInResource extends Action {
	
	private static final long serialVersionUID = 6879554289822402659L;

	private String resource, key, value, result;

	public IsInResource() {
		super();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {

		// GETS THE VARIABLE CONTENT
		Object varContent = scInstance.getContext(getParentTransitionTarget()).get(value);

		// BOOLEAN TO WRITE IN THE RESULT
		Boolean matchResult = false;
		
		// GETS THE RESOURCE
		Resource resourceEnum = Resource.createResource(resource);
		if (resourceEnum != null) {
			switch (resourceEnum) {
			case UNCLASSIFIED:
				UnclassifiedDictionary dico =
				(UnclassifiedDictionary) scInstance.getRootContext().get("_unclassified_dictionary");
				matchResult = isInList(dico, key, varContent);
			default:
				break;
			}
		} else {
			throw new ModelException("Resource " + resource + " does not exist");
		}

		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(result, matchResult);
	}

	private Boolean isInList(UnclassifiedDictionary dico, String key, Object value) {
		// GETS THE DEFINITION IN DICO
		Set<String> dicoContent = dico.getDefintion(key);
		// RESULT
		Boolean matches = false;
		
		// IF WE HAVE TO CHECK AGAINST A LIST, CHECK EACH ITEM
		if (value instanceof List<?>) {
			for (Object element : (List<?>) value) {
				if (element instanceof String) {
					if (dicoContent.contains(((String) element).trim())) {
						matches = true;
						break;
					}
				}
			}
		}
		// IF WE HAVE TO CHECK AGAINST A STRING, CHECK THIS STRING
		else if (value instanceof String) {
			if (dicoContent.contains(((String) value).trim())) {
				matches = true;
			}
		}
		
		return matches;

	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDictionary() {
		return resource;
	}

	public void setDictionary(String dictionary) {
		this.resource = dictionary;
	}

}
