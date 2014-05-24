package com.github.aiderpmsi.pims.treebrowser;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

public class Switch extends Action {

	private HashMap<String, Expression> expressions = new HashMap<>();
	
	public String executeAction(Node node, JexlContext jc, JexlEngine jexl, Argument[] args) throws IOException {
		// GETS ARGUMENTS
		String cond = "";
		for (Argument arg : args) {
			switch (arg.key) {
			case "cond":
				cond = arg.value; break;
			default:
				throw new IOException("Argument " + arg.key + " unknown for " + getClass().getSimpleName());
			}
		}
        // CREATE OR RETRIEVE THE EXPRESSION
        Expression e;
        if ((e = expressions.get(cond)) == null) {
        	e = jexl.createExpression(cond);
        	expressions.put(cond, e);
        }
        Object result = e.evaluate(jc);
        if (result instanceof Boolean) {
        	Boolean resultB = (Boolean) result;
        	if (resultB)
        		return "child(1)";
        	else
        		return "sibling(1)";
        } else {
        	throw new IOException(cond + " is not of boolean type");
        }
	}

}
