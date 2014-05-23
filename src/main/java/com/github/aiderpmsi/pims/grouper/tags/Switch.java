package com.github.aiderpmsi.pims.grouper.tags;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

public class Switch extends BaseAction {

	private String cond;
	
	private HashMap<String, Expression> expressions = new HashMap<>();
	
	@Override
	public String executeAction(Node node, JexlContext jc, JexlEngine jexl) throws IOException {
        // CREATE Or RETRIEVE THE EXPRESSION
        Expression e;
        if ((e = expressions.get(cond)) == null) {
        	e = jexl.createExpression(cond);
        	expressions.put(cond, e);
        }
        Object result = e.evaluate(jc);
        if (result instanceof Boolean) {
        	Boolean resultB = (Boolean) result;
        	if (resultB)
        		return "child";
        	else
        		return "sibling";
        } else {
        	throw new IOException(cond + " is not of boolean type");
        }
	}

	public void setCond(String cond) {
		this.cond = cond;
	}

	@Override
	public void init() {
		cond = "";
	}

	@Override
	public void cleanout() {
		// DO NOTHING
	}
}
