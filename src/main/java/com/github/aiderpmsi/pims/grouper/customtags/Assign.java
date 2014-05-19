package com.github.aiderpmsi.pims.grouper.customtags;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

public class Assign extends BaseAction {

	private String var, expr;
	
	@Override
	public String executeAction(Node node, JexlContext jc, JexlEngine jexl) {
        // CREATE THE EXPRESSION
        Expression e = jexl.createExpression(expr);
		jc.set(var, e.evaluate(jc));
		return "child|sibling";
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

}
