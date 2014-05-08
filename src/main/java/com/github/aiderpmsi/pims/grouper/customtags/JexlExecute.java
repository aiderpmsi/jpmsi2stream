package com.github.aiderpmsi.pims.grouper.customtags;

import java.util.Collection;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.logging.Log;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.env.jexl.JexlEvaluator;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

public class JexlExecute extends Action {

	private static final long serialVersionUID = 3615764243955863213L;

	private String expr, result;
	
	@Override
	@SuppressWarnings({ "rawtypes" })
	public void execute(EventDispatcher evtDispatcher, ErrorReporter errRep,
			SCInstance scInstance, Log appLog, Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {

		// GETS EVALUATOR AND CONTEXT
		JexlEvaluator eval = (JexlEvaluator) scInstance.getEvaluator();
		JexlContext ctx = (JexlContext) scInstance.getContext(getParentTransitionTarget());

		Object resObject = eval.eval((Context) ctx, expr);

		// WRITES THE RESULT
		scInstance.getContext(getParentTransitionTarget()).setLocal(result, resObject);
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
