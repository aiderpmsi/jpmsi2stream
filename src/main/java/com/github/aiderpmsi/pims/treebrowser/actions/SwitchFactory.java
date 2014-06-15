package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public class SwitchFactory implements ActionFactory<SwitchFactory.Switch> {

	@Override
	public Switch createAction(Argument[] arguments) throws IOException {
		// GETS ARGUMENTS
		String cond = null;
		for (Argument argument : arguments) {
			switch (argument.key) {
			case "cond":
				cond = argument.value; break;
			default:
				throw new IOException("Argument " + argument.key + " unknown for " + getClass().getSimpleName());
			}
		}
		
		// CHECK ARGUMENTS
		if (cond == null) {
			throw new IOException("cond parameter has to be set in " + getClass().getSimpleName());
		}
		
		return new Switch(cond);
	}

	public class Switch implements ActionFactory.Action {
		
		private String cond;
		
		private Expression e = null; 
		
		public Switch(String cond) {
			this.cond = cond;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Node<Action> execute(Node<Action> node,
				HashMap<String, Node<Action>> labels, JexlContext jc,
				JexlEngine jexl) throws IOException {
			if (e == null)
				e = jexl.createExpression(cond);

	        Object result = e.evaluate(jc);
	        
	        if (result != null && result instanceof Boolean) {
	        	Boolean resultB = (Boolean) result;
	        	if (resultB)
	        		return (Node<ActionFactory.Action>) node.firstChild;
	        	else
	        		return (Node<ActionFactory.Action>) node.nextSibling;
	        } else {
	        	throw new IOException(cond + " is not of boolean type");
	        }
		}
	}
	
}
