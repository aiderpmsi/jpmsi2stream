package com.github.aiderpmsi.pims.grouper.customtags;

import java.io.IOException;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

public class Switch extends BaseAction {

	private String cond;
	
	@Override
	public String executeAction(Node node, JexlContext jc, JexlEngine jexl) throws IOException {
        // CREATE THE EXPRESSION
        Expression e = jexl.createExpression(cond);
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
}
