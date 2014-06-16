package com.github.aiderpmsi.pims.treebrowser.actions;

import java.io.IOException;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;

import com.github.aiderpmsi.pims.treemodel.Node;

public class SwitchFactory implements ActionFactory<SwitchFactory.Switch> {

	@Override
	public Switch createAction(JexlEngine je, Argument[] arguments) throws IOException {
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
		
		return new Switch(je, cond);
	}

	public class Switch implements ActionFactory.Action {
		
		private Expression e; 
		
		public Switch(JexlEngine je, String cond) {
			e = je.createExpression(cond);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Node<Action> execute(Node<Action> node,
				JexlContext jc) throws IOException {

			Object result = e.evaluate(jc);
	        
	        if (result != null && result instanceof Boolean) {
	        	Boolean resultB = (Boolean) result;
	        	if (resultB)
	        		return (Node<ActionFactory.Action>) node.firstChild;
	        	else
	        		return (Node<ActionFactory.Action>) node.nextSibling;
	        } else {
	        	throw new IOException(result == null ? "Null" : result.toString() + " is not of boolean type");
	        }
		}
	}
	
}
