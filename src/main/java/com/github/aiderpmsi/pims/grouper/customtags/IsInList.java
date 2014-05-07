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

import com.github.aiderpmsi.pims.grouper.model.GroupDictionary;

public class IsInList extends Action {
	
	private static final long serialVersionUID = 6879554289822402659L;

	private String list, varname, result;

	public IsInList() {
		super();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {

		// GETS THE LIST IN DICTIONNARY
		GroupDictionary dico =
				(GroupDictionary) scInstance.getRootContext().get("_dictionary");
		Set<String> dicoContent = dico.getDefintion(list);
		
		// GETS THE VARIABLE CONTENT
		Object varContent = scInstance.getContext(getParentTransitionTarget()).get(varname);

		// RESULT
		Boolean matches = false;
		
		// IF WE HAVE TO CHECK AGAINST A LIST, CHECK EACH ITEM
		if (varContent instanceof List<?>) {
			for (Object element : (List) varContent) {
				if (element instanceof String) {
					if (dicoContent.contains(((String) element).trim())) {
						matches = true;
						break;
					}
				}
			}
		}
		// IF WE HAVE TO CHECK AGAINST A STRING, CHECK THIS STRING
		else if (varContent instanceof String) {
			if (dicoContent.contains(((String) varContent).trim())) {
				matches = true;
			}
		}
		
		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(result, matches);
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public String getVarname() {
		return varname;
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
