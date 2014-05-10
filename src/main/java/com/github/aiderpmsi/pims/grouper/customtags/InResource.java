package com.github.aiderpmsi.pims.grouper.customtags;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

import com.github.aiderpmsi.pims.grouper.model.BaseSimpleDictionary;
import com.github.aiderpmsi.pims.grouper.model.Resource;

public class InResource extends Action {
	
	private static final long serialVersionUID = 6879554289822402659L;

	private String resource, key, value, result;

	public InResource() {
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
		Integer matchResult = 0;
		
		// GETS THE RESOURCE
		Resource resourceEnum = Resource.createResource(resource);
		if (resourceEnum != null) {
			switch (resourceEnum) {
			case UNCLASSIFIED:
			case ACTECLASSANT:
			case CLASSEACTE:
				BaseSimpleDictionary dico =
				(BaseSimpleDictionary) scInstance.getRootContext().get("_" + resourceEnum.getName() + "_dictionary");
				matchResult = isInList(dico, key, varContent);
				break;
			default:
				throw new ModelException(resource + " is not a possible resource for " + getClass().toString());
			}
		} else {
			throw new ModelException("Resource " + resource + " does not exist");
		}

		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(result, matchResult);
	}

	private Integer isInList(BaseSimpleDictionary dico, String key, Object value) throws ModelException {
		// GETS THE DEFINITION IN DICO
		Set<String> dicoContent = dico.getDefintion(key);

		// RESULT
		Integer matches = 0;
		
		// IF VALUE IS NULL, WE RETURN FALSE
		if (value == null) {
			// DO NOTHING, MATCHES IS ALREADY FALSE
		}
		// IF WE HAVE TO CHECK AGAINST A COLLECTION, CHECK EACH ITEM
		else if (value instanceof Collection<?>) {
			for (Object element : (Collection<?>) value) {
				if (element instanceof String) {
					if (dicoContent.contains(((String) element).trim())) {
						matches++;
					}
				}
			}
		}
		// IF WE HAVE TO CHECK AGAINST A STRING, CHECK THIS STRING
		else if (value instanceof String) {
			if (dicoContent.contains(((String) value).trim())) {
				matches++;
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

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

}
