package com.github.aiderpmsi.pims.treebrowser;

import java.io.IOException;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

import com.github.aiderpmsi.pims.treebrowser.actions.Argument;

public class Assign extends Action {

	@Override
	public String executeAction(Node node, JexlContext jc, JexlEngine jexl, Argument[] args) throws IOException {
		// GETS ARGUMENTS
		String expr = "", var="";
		for (Argument arg : args) {
			switch (arg.key) {
			case "expr":
				expr = arg.value; break;
			case "var":
				var = arg.value; break;
			default:
				throw new IOException("Argument " + arg.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
        // CREATE THE EXPRESSION
        Expression e = jexl.createExpression(expr);
		jc.set(var, e.evaluate(jc));
		return "child(1)|sibling(1)";
	}

}
