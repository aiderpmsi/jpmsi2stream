package com.github.aiderpmsi.pims.treebrowser;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.Script;
import org.w3c.dom.Node;

public class Execute extends Action {

	private HashMap<String, Script> scripts = new HashMap<>();
	
	public String executeAction(Node node, JexlContext jc, JexlEngine jexl, Argument[] args) throws IOException {
		// GETS ARGUMENTS
		String expr = "";
		for (Argument arg : args) {
			switch (arg.key) {
			case "expr":
				expr = arg.value; break;
			default:
				throw new IOException("Argument " + arg.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
        // CREATE OR RETRIEVE THE EXPRESSION
        Script e;
        if ((e = scripts.get(expr)) == null) { 
        	e = jexl.createScript(expr);
        	scripts.put(expr, e);
        }
        
		// EXECUTE EXPRESSION
        e.execute(jc);
        
		// GO TO NEXT CHILD ELEMENT OR NEXT SIBLING
        return "child(1)|sibling(1)";
	}

}
