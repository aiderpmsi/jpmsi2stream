package com.github.aiderpmsi.pims.grouper.customtags;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.w3c.dom.Node;

public class Execute extends BaseAction {

	private String expr;
	
	@Override
	public String executeAction(Node node, JexlContext jc, JexlEngine jexl) {
        // CREATE THE EXPRESSION
        Expression e = jexl.createExpression(expr);
		// EXECUTE EXPRESSION
        e.evaluate(jc);
		// GO TO NEXT CHILD ELEMENT OR NEXT SIBLING
        return "child|sibling";
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

}
