package com.github.aiderpmsi.pims.grouper.customtags;

import java.util.Collection;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

import com.github.aiderpmsi.pims.grouper.model.BaseAbstractDictionary;

public class GetResource extends Action {
	
	private static final long serialVersionUID = 6879554289822402659L;

	private static HashSet<String> resources = new HashSet<>();
	
	static {
		resources.add("unclassified");
		resources.add("acteclassant");
	}
	
	private String resource, key, result;

	public GetResource() {
		super();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {

		// OBJECT TO SET IN RESULT
		Object resultObject = null;
			
		// GETS THE DICTIONARY IF THIS RESOURCE IS ACCEPTED
		if (resources.contains(resource)) {
			BaseAbstractDictionary<?, ?> dico = 
					(BaseAbstractDictionary<?, ?>) scInstance.getRootContext().get("_" + resource + "_dictionary");
			resultObject = dico.getDefintion(key);
		}

		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(result, resultObject);
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
