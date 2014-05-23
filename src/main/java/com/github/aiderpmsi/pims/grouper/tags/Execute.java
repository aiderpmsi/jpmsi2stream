package com.github.aiderpmsi.pims.grouper.tags;

import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.Script;
import org.w3c.dom.Node;

public class Execute extends BaseAction {

	private String expr;
	
	private HashMap<String, Script> scripts = new HashMap<>();
	
	@Override
	public String executeAction(Node node, JexlContext jc, JexlEngine jexl) {
        // CREATE OR RETRIEVE THE EXPRESSION
        Script e;
        if ((e = scripts.get(expr)) == null) { 
        	e = jexl.createScript(expr);
        	scripts.put(expr, e);
        }
		// EXECUTE EXPRESSION
        e.execute(jc);
		// GO TO NEXT CHILD ELEMENT OR NEXT SIBLING
        return "child|sibling";
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	@Override
	public void init() {
		expr = "";
	}

	@Override
	public void cleanout() {
	}

}
