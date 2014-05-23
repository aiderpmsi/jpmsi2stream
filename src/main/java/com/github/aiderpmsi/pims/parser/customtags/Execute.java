package com.github.aiderpmsi.pims.parser.customtags;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.Script;
import org.apache.commons.logging.Log;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

public class Execute extends Action {

	/** Computed serialId */
	private static final long serialVersionUID = -7737060216919680913L;

	private String expr;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, @SuppressWarnings("rawtypes") Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {
		// GETS THE CACHED EVALUATOR
		JexlEngine jexl2 = (JexlEngine) scInstance.getRootContext().get("jexl2");
		// GETS THE CACHED SCRIPT IF EXISTS
		HashMap<String, Script> scripts = (HashMap<String, Script>) scInstance.getRootContext().get("scripts");
		Script e;
		if ((e = scripts.get(expr)) == null) {
        	e = jexl2.createScript(expr);
        	scripts.put(expr, e);
		}
		
		Context currentContext = scInstance.getContext(getParentTransitionTarget());
		Map<String, Object> context = new HashMap<>(currentContext.getVars());
		while (true) {
			// WHILE WE HAVE A CONTEXT, ADD ITS VARIABLES (IF NOT CACHED BY OTHERS)
			if ((currentContext = currentContext.getParent()) != null) {
				Map<String, Object> currentvars = currentContext.getVars();
				currentvars.putAll(context);
				context = currentvars;
			} else {
				break;
			}
		}
		// EXECUTE EXPRESSION
		e.execute(new MapContext(context));

		// RETURN NOTHING
	}
	
	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}
}
